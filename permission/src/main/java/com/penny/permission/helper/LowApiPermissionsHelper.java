package com.penny.permission.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import com.penny.permission.PermissionRationaleBody;
import java.util.ArrayList;
import java.util.List;

/**
 * Permissions helper for apps built against API < 23, which do not need runtime permissions.
 */
class LowApiPermissionsHelper extends PermissionHelper<Object> {

  public LowApiPermissionsHelper(@NonNull Object host) {
    super(host);
  }

  @Override
  public void directRequestPermissions(int requestCode,
      @NonNull List<PermissionRationaleBody> permNecessary,
      @NonNull List<PermissionRationaleBody> permNonNecessay) {
    throw new IllegalStateException("Should never be requesting permissions on API < 23!");
  }

  @Override
  public PermissionRationaleBody shouldShowRequestPermissionRationale(@NonNull PermissionRationaleBody perm) {
    return null;
  }

  @Override
  @SuppressLint("NewApi")
  public void showRequestPermissionRationale(@NonNull PermissionRationaleBody body,@NonNull
      ArrayList<PermissionRationaleBody> permNecessarys,
      @NonNull ArrayList<PermissionRationaleBody> permNonNecessays,
      int requestCode) {
    throw new IllegalStateException("Should never be requesting permissions on API < 23!");
  }

  @Override
  public Context getContext() {
    return null;
  }
}
