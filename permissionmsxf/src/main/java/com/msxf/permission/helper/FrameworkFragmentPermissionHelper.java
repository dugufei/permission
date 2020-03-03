package com.msxf.permission.helper;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.msxf.permission.PermissionRationaleBody;
import com.msxf.permission.Utils;
import java.util.List;

/**
 * Permissions helper for {@link Fragment} from the framework.
 */
class FrameworkFragmentPermissionHelper extends BaseFrameworkPermissionsHelper<Fragment> {

  public FrameworkFragmentPermissionHelper(@NonNull Fragment host) {
    super(host);
  }

  @Override
  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
  public FragmentManager getFragmentManager() {
    return getHost().getChildFragmentManager();
  }

  @Override
  @SuppressLint("NewApi")
  public void directRequestPermissions(int requestCode,
      @NonNull List<PermissionRationaleBody> permNecessarys,
      @NonNull List<PermissionRationaleBody> permNonNecessays) {
    getHost().requestPermissions(Utils.transferPermBodylist(permNecessarys, permNonNecessays),
        requestCode);
  }

  @Override
  @SuppressLint("NewApi")
  public PermissionRationaleBody shouldShowRequestPermissionRationale(@NonNull PermissionRationaleBody perm) {
    if(getHost().shouldShowRequestPermissionRationale(perm.getPermission())){
      return perm;
    } else {
      return null;
    }
  }

  @Override
  @SuppressLint("NewApi")
  public Context getContext() {
    return getHost().getActivity();
  }
}
