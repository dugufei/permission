package com.penny.permission;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.penny.permission.helper.PermissionHelper;

/**
 * Click listener for either {@link RationaleDialogFragment} or {@link
 * RationaleDialogFragmentCompat}.
 */
class RationaleDialogClickListener implements Dialog.OnClickListener {

  private Object mHost;
  private RationaleDialogConfig mConfig;
  private EasyPermissions.PermissionCallbacks mCallbacks;

  RationaleDialogClickListener(RationaleDialogFragmentCompat compatDialogFragment,
      RationaleDialogConfig config,
      EasyPermissions.PermissionCallbacks callbacks) {

    mHost = compatDialogFragment.getParentFragment() != null
        ? compatDialogFragment.getParentFragment()
        : compatDialogFragment.getActivity();

    mConfig = config;
    mCallbacks = callbacks;
  }

  @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB) RationaleDialogClickListener(
      RationaleDialogFragment dialogFragment,
      RationaleDialogConfig config,
      EasyPermissions.PermissionCallbacks callbacks) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      mHost = dialogFragment.getParentFragment() != null ?
          dialogFragment.getParentFragment() :
          dialogFragment.getActivity();
    } else {
      mHost = dialogFragment.getActivity();
    }

    mConfig = config;
    mCallbacks = callbacks;
  }

  @Override
  public void onClick(DialogInterface dialog, int which) {
    if (which == Dialog.BUTTON_POSITIVE) {
      if (mHost instanceof Fragment) {
        PermissionHelper helper = PermissionHelper.newInstance((Fragment) mHost);
        if (helper.somePermissionPermanentlyDenied(mConfig.permsNecessary)) {
          // todo  展示自定义弹框
          Log.e("=====","此处需展示自定义弹框");
        } else {
          helper.directRequestPermissions(
              mConfig.requestCode, mConfig.permsNecessary, mConfig.permsNonNecessary);
        }
      } else if (mHost instanceof android.app.Fragment) {
        PermissionHelper helper = PermissionHelper.newInstance((android.app.Fragment) mHost);
        if (helper.somePermissionPermanentlyDenied(mConfig.permsNecessary)) {
          // todo  展示自定义弹框
          Log.e("=====","此处需展示自定义弹框");
        } else {
          helper.directRequestPermissions(
              mConfig.requestCode, mConfig.permsNecessary, mConfig.permsNonNecessary);
        }
      } else if (mHost instanceof Activity) {
        PermissionHelper helper = PermissionHelper.newInstance((Activity) mHost);
        if(helper.somePermissionPermanentlyDenied(mConfig.permsNecessary)){
          // todo  展示自定义弹框
          Log.e("=====","此处需展示自定义弹框");
        }else{
          helper.directRequestPermissions(mConfig.requestCode, mConfig.permsNecessary,
              mConfig.permsNonNecessary);
        }
      } else {
        throw new RuntimeException("Host must be an Activity or Fragment!");
      }
    } else {
      notifyPermissionDenied();
    }
  }

  private void notifyPermissionDenied() {
    if (mCallbacks != null) {
      mCallbacks.onPermissionsDenied(mConfig.requestCode,
          mConfig.permsNecessary);
    }
  }
}

