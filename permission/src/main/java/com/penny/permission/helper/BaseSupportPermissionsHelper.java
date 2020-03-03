package com.penny.permission.helper;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import com.penny.permission.PermissionRationaleBody;
import com.penny.permission.R;
import com.penny.permission.RationaleDialogFragmentCompat;
import java.util.ArrayList;

/**
 * Implementation of {@link PermissionHelper} for Support Library host classes.
 */
abstract class BaseSupportPermissionsHelper<T> extends PermissionHelper<T> {

  public BaseSupportPermissionsHelper(@NonNull T host) {
    super(host);
  }

  public abstract FragmentManager getSupportFragmentManager();

  @Override
  @SuppressLint("NewApi")
  public void showRequestPermissionRationale(@NonNull PermissionRationaleBody body,
      @NonNull ArrayList<PermissionRationaleBody> permNecessarys,
      @NonNull ArrayList<PermissionRationaleBody> permNonNecessays,
      int requestCode) {
    RationaleDialogFragmentCompat
        .newInstance(R.string.permissionModule_Dialog_OK, R.string.permissionModule_Dialog_cancel,
            body, requestCode, permNecessarys, permNonNecessays)
        .show(getSupportFragmentManager(), RationaleDialogFragmentCompat.TAG);
  }
}
