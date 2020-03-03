package com.msxf.permission.helper;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.msxf.permission.PermissionRationaleBody;
import com.msxf.permission.Utils;
import java.util.List;

/**
 * Permissions helper for {@link Fragment} from the support library.
 */
class SupportFragmentPermissionHelper extends BaseSupportPermissionsHelper<Fragment> {

  public SupportFragmentPermissionHelper(@NonNull Fragment host) {
    super(host);
  }

  @Override
  public FragmentManager getSupportFragmentManager() {
    return getHost().getChildFragmentManager();
  }

  @Override
  public void directRequestPermissions(int requestCode,
      @NonNull List<PermissionRationaleBody> permNecessary,
      @NonNull List<PermissionRationaleBody> permNonNecessay) {
    getHost().requestPermissions(Utils.transferPermBodylist(permNecessary, permNonNecessay),
        requestCode);
  }

  @Override
  public PermissionRationaleBody shouldShowRequestPermissionRationale(
      @NonNull PermissionRationaleBody perm) {
    if (getHost().shouldShowRequestPermissionRationale(perm.getPermission())) {
      return perm;
    } else {
      return null;
    }
  }

  @Override
  public Context getContext() {
    return getHost().getActivity();
  }
}
