package com.penny.permission;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialogFragment;
import java.util.ArrayList;

/**
 * {@link AppCompatDialogFragment} to display rationale for permission requests when the request
 * comes from a Fragment or Activity that can host a Fragment.
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class RationaleDialogFragmentCompat extends AppCompatDialogFragment {

  public static final String TAG = "RationaleDialogFragmentCompat";

  private EasyPermissions.PermissionCallbacks mPermissionCallbacks;

  public static RationaleDialogFragmentCompat newInstance(
      @StringRes int positiveButton, @StringRes int negativeButton,
      @NonNull PermissionRationaleBody body, int requestCode, @NonNull
      ArrayList<PermissionRationaleBody> permissions,
      @NonNull ArrayList<PermissionRationaleBody> permNonNecessays) {

    // Create new Fragment
    RationaleDialogFragmentCompat dialogFragment = new RationaleDialogFragmentCompat();

    // Initialize configuration as arguments
    RationaleDialogConfig config = new RationaleDialogConfig(
        positiveButton, negativeButton, body,requestCode, permissions,permNonNecessays);
    dialogFragment.setArguments(config.toBundle());

    return dialogFragment;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (getParentFragment() != null
        && getParentFragment() instanceof EasyPermissions.PermissionCallbacks) {
      mPermissionCallbacks = (EasyPermissions.PermissionCallbacks) getParentFragment();
    } else if (context instanceof EasyPermissions.PermissionCallbacks) {
      mPermissionCallbacks = (EasyPermissions.PermissionCallbacks) context;
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mPermissionCallbacks = null;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Rationale dialog should not be cancelable
    setCancelable(false);

    // Get config from arguments, create click listener
    RationaleDialogConfig config = new RationaleDialogConfig(getArguments());
    RationaleDialogClickListener clickListener =
        new RationaleDialogClickListener(this, config, mPermissionCallbacks);

    // Create an Dialog
    return config.createNewDialog(getContext(), config.body);
  }
}
