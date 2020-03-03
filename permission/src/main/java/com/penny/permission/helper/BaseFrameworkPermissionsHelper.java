package com.penny.permission.helper;

import android.app.FragmentManager;
import androidx.annotation.NonNull;
import com.penny.permission.PermissionRationaleBody;
import com.penny.permission.R;
import com.penny.permission.RationaleDialogFragment;
import java.util.ArrayList;

public abstract class BaseFrameworkPermissionsHelper<T> extends PermissionHelper<T> {
  public BaseFrameworkPermissionsHelper(@NonNull T host1) {
    super(host1);
  }

  public abstract FragmentManager getFragmentManager();

  @Override
  public void showRequestPermissionRationale(@NonNull PermissionRationaleBody necessaryBody,
      @NonNull ArrayList<PermissionRationaleBody> permNecessarys,
      @NonNull ArrayList<PermissionRationaleBody> permNonNecessays,
      int requestCode) {
    RationaleDialogFragment
        .newInstance(R.string.permissionModule_Dialog_OK, R.string.permissionModule_Dialog_cancel,
            necessaryBody, requestCode, permNecessarys,permNonNecessays)
        .show(getFragmentManager(), RationaleDialogFragment.TAG);
  }
}
