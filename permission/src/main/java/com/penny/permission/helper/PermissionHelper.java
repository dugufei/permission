package com.penny.permission.helper;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.penny.permission.PermissionRationaleBody;
import java.util.ArrayList;
import java.util.List;

public abstract class PermissionHelper<T> {
  private T host;

  public static PermissionHelper newInstance(Activity host) {
    if (Build.VERSION.SDK_INT < 23) {
      return new LowApiPermissionsHelper(host);
    }
    return new ActivityPermissionHelper(host);
  }

  public static PermissionHelper newInstance(Fragment host) {
    if (Build.VERSION.SDK_INT < 23) {
      return new LowApiPermissionsHelper(host);
    }
    return new SupportFragmentPermissionHelper(host);
  }

  public static PermissionHelper newInstance(android.app.Fragment host) {
    if (Build.VERSION.SDK_INT < 23) {
      return new LowApiPermissionsHelper(host);
    }
    return new FrameworkFragmentPermissionHelper(host);
  }

  public PermissionHelper(@NonNull T host1) {
    host = host1;
  }

  //public abstract boolean checkPemission(T t, @NonNull PermissionRationaleBody perm);

  public abstract Context getContext();

  public abstract void showRequestPermissionRationale(@NonNull PermissionRationaleBody body,
      @NonNull ArrayList<PermissionRationaleBody> permNecessarys,
      @NonNull ArrayList<PermissionRationaleBody> permNonNecessays,
      int requestCode);

  public void showDialog(@NonNull PermissionRationaleBody body){
    // todo
    Log.e("=====","展示挽留框"+body.getRationaleContent());
  };

  public abstract PermissionRationaleBody shouldShowRequestPermissionRationale(
      @NonNull PermissionRationaleBody perm);

  public abstract void directRequestPermissions(int requestCode,
      @NonNull List<PermissionRationaleBody> permNecessary,
      @NonNull List<PermissionRationaleBody> permNonNecessay);

  public void requestPermissions(@NonNull ArrayList<PermissionRationaleBody> permNecessary,
      @NonNull ArrayList<PermissionRationaleBody> permNonNecessay, int requestCode) {
    PermissionRationaleBody body = shouldShowRationale(permNecessary);
    if (body != null) {
      showRequestPermissionRationale(body, permNecessary, permNonNecessay, requestCode);
    } else {
      directRequestPermissions(requestCode, permNecessary, permNonNecessay);
    }
  }

  @NonNull
  public T getHost() {
    return host;
  }

  public boolean somePermissionPermanentlyDenied(@NonNull List<PermissionRationaleBody> perms) {
    for (PermissionRationaleBody deniedPermission : perms) {
      if (permissionPermanentlyDenied(deniedPermission)) {
        return true;
      }
    }
    return false;
  }

  private boolean permissionPermanentlyDenied(@NonNull PermissionRationaleBody perm) {
    return shouldShowRequestPermissionRationale(perm) != null;
  }

  private PermissionRationaleBody shouldShowRationale(
      @NonNull List<PermissionRationaleBody> permsNecessarys) {
    if (permsNecessarys.size() == 0) {
      return null;
    } else {
      for (int i = 0; i < permsNecessarys.size(); i++) {
        PermissionRationaleBody body = shouldShowRequestPermissionRationale(permsNecessarys.get(i));
        if (body != null) {
          return body;
        }
      }
      return null;
    }
  }
}
