package com.penny.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.penny.permission.helper.PermissionHelper;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EasyPermissions {
  private static final String TAG = "EasyPermissions";
  private static PermissionRequest permissionRequest ;

  /**
   * Callback interface to receive the results of {@code EasyPermissions.requestPermissions()}
   * calls.
   */
  public interface PermissionCallbacks extends ActivityCompat.OnRequestPermissionsResultCallback {

    void onPermissionsGranted(int requestCode, List<PermissionRationaleBody> perms);

    void onPermissionsDenied(int requestCode, List<PermissionRationaleBody> perms);
  }

  public static void requestPermission(Activity activity,
      ArrayList<PermissionRationaleBody> permNecessary, ArrayList<PermissionRationaleBody> permNonNecessary,
      int requestCode) {
    requestPermission(PermissionHelper.newInstance(activity), permNecessary, permNonNecessary,
        requestCode);
  }

  private static void requestPermission(PermissionHelper helper,
      ArrayList<PermissionRationaleBody> permNecessary, ArrayList<PermissionRationaleBody> permNonNecessary,
      int requestCode) {
    permissionRequest = new PermissionRequest(UUID.randomUUID().toString(),requestCode,permNecessary,permNonNecessary);
    String[] perms = Utils.transferPermBodylist(permNecessary, permNonNecessary);
    if (hasPermissions(helper.getContext(), perms)) {
      notifyAlreadyHasPermissions(helper.getHost(), requestCode, perms);
      return;
    }
    helper.requestPermissions(permNecessary, permNonNecessary, requestCode);
  }

  private static void notifyAlreadyHasPermissions(@NonNull Object object,
      int requestCode,
      @NonNull String[] perms) {
    int[] grantResults = new int[perms.length];
    for (int i = 0; i < perms.length; i++) {
      grantResults[i] = PackageManager.PERMISSION_GRANTED;
    }

    onRequestPermissionsResult(object,requestCode,perms, grantResults,  object);
  }

  public static void onRequestPermissionsResult(Object host, int requestCode, String[] permissions,
      int[] grantResults,
      Object... receivers) {
    // Make a collection of granted and denied permissions from the request.
    List<String> granted = new ArrayList<>();
    List<String> denied = new ArrayList<>();
    for (int i = 0; i < permissions.length; i++) {
      String perm = permissions[i];
      if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
        PermissionManager.updatePermShouldShowRationale(perm,false);
        granted.add(perm);
      } else {
        PermissionManager.updatePermShouldShowRationale(perm,true);
        denied.add(perm);
      }
    }
    PermissionRationaleBody body = shouldShowDialog(denied,permissionRequest);
    if(body != null){
      showDialog(host,body);
      return;
    }
    onRequestPermissionsResult(requestCode,granted,denied,receivers);

  }

  private static PermissionRationaleBody shouldShowDialog(List<String> denied,PermissionRequest permissionRequest){
    for (int i = 0 ;i < denied.size();i++){
      for(int j = 0;j< permissionRequest.getPermNecessarys().size();j++){
        if(denied.get(i).equalsIgnoreCase(permissionRequest.getPermNecessarys().get(j).getPermission())){
          return permissionRequest.getPermNecessarys().get(j);
        }
      }
    }
    return null;
  }

  private static void showDialog(Object host,PermissionRationaleBody body){
    if (host instanceof Activity) {
      PermissionHelper.newInstance((Activity) host).showDialog(body);
    } else if (host instanceof Fragment) {

    } else if (host instanceof android.app.Fragment) {

    } else {
      throw new RuntimeException("无法识别的宿主类型");
    }
  }

  private static void onRequestPermissionsResult(int requestCode,List<String> granted,List<String> denied,
       Object... receivers) {
    for (Object object : receivers) {
      // If 100% successful, call annotated methods
      if (!granted.isEmpty() && denied.isEmpty()) {
        runAllPermGrantedAnnotatedMethods(object, requestCode);
      } else {
        runNecessaryPermGrantedAnnotatedMethods(object, requestCode);
      }
    }
  }

  /**
   * Check if the calling context has a set of permissions.
   *
   * @param context the calling context.
   * @param perms one ore more permissions, such as {@link Manifest.permission#CAMERA}.
   * @return true if all permissions are already granted, false if at least one permission is not
   * yet granted.
   * @see Manifest.permission
   */
  public static boolean hasPermissions(Context context, @NonNull String... perms) {
    // Always return true for SDK < M, let the system deal with the permissions
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      Log.w(TAG, "hasPermissions: API version < M, returning true by default");

      // DANGER ZONE!!! Changing this will break the library.
      return true;
    }

    // Null context may be passed if we have detected Low API (less than M) so getting
    // to this point with a null context should not be possible.
    if (context == null) {
      throw new IllegalArgumentException("Can't check permissions for null context");
    }

    for (String perm : perms) {
      if (ContextCompat.checkSelfPermission(context, perm)
          != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }


  /**
   * Find all methods annotated with {@link AfterAllPermGranted} on a given object with the
   * correc requestCode argument.
   * @param object the object with annotated methods.
   * @param requestCode the requestCode passed to the annotation.
   */
  private static void runAllPermGrantedAnnotatedMethods(@NonNull Object object, int requestCode) {
    Class clazz = object.getClass();
    if (isUsingAndroidAnnotations(object)) {
      clazz = clazz.getSuperclass();
    }

    while (clazz != null) {
      for (Method method : clazz.getDeclaredMethods()) {
        if (method.isAnnotationPresent(AfterAllPermGranted.class)) {
          // Check for annotated methods with matching request code.
          AfterAllPermGranted ann = method.getAnnotation(AfterAllPermGranted.class);
          if (ann.value() == requestCode) {
            // Method must be void so that we can invoke it
            if (method.getParameterTypes().length > 0) {
              throw new RuntimeException(
                  "Cannot execute method " + method.getName() + " because it is non-void method and/or has input parameters.");
            }

            try {
              // Make method accessible if private
              if (!method.isAccessible()) {
                method.setAccessible(true);
              }
              method.invoke(object);
            } catch (IllegalAccessException e) {
              Log.e(TAG, "runDefaultMethod:IllegalAccessException", e);
            } catch (InvocationTargetException e) {
              Log.e(TAG, "runDefaultMethod:InvocationTargetException", e);
            }
          }
        }
      }
      clazz = clazz.getSuperclass();
    }
  }
  /**
   * Find all methods annotated with {@link AfterNecessaryPermGranted} on a given object with the
   * correc requestCode argument.
   * @param object the object with annotated methods.
   * @param requestCode the requestCode passed to the annotation.
   */
  private static void runNecessaryPermGrantedAnnotatedMethods(@NonNull Object object, int requestCode) {
    Class clazz = object.getClass();
    if (isUsingAndroidAnnotations(object)) {
      clazz = clazz.getSuperclass();
    }

    while (clazz != null) {
      for (Method method : clazz.getDeclaredMethods()) {
        if (method.isAnnotationPresent(AfterNecessaryPermGranted.class)) {
          // Check for annotated methods with matching request code.
          AfterNecessaryPermGranted ann = method.getAnnotation(AfterNecessaryPermGranted.class);
          if (ann.value() == requestCode) {
            // Method must be void so that we can invoke it
            if (method.getParameterTypes().length > 0) {
              throw new RuntimeException(
                  "Cannot execute method " + method.getName() + " because it is non-void method and/or has input parameters.");
            }

            try {
              // Make method accessible if private
              if (!method.isAccessible()) {
                method.setAccessible(true);
              }
              method.invoke(object);
            } catch (IllegalAccessException e) {
              Log.e(TAG, "runDefaultMethod:IllegalAccessException", e);
            } catch (InvocationTargetException e) {
              Log.e(TAG, "runDefaultMethod:InvocationTargetException", e);
            }
          }
        }
      }
      clazz = clazz.getSuperclass();
    }
  }

  /**
   * Determine if the project is using the AndroidAnnoations library.
   */
  private static boolean isUsingAndroidAnnotations(@NonNull Object object) {
    if (!object.getClass().getSimpleName().endsWith("_")) {
      return false;
    }
    try {
      Class clazz = Class.forName("org.androidannotations.api.view.HasViews");
      return clazz.isInstance(object);
    } catch (ClassNotFoundException e) {
      return false;
    }
  }
}
