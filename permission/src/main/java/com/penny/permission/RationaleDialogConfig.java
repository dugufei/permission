package com.penny.permission;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.penny.permission.UI.PermissionBaseDialog;
import java.util.ArrayList;

/**
 * Configuration for either {@link RationaleDialogFragment} or {@link RationaleDialogFragmentCompat}.
 */
class RationaleDialogConfig {

  private static final String KEY_POSITIVE_BUTTON = "positiveButton";
  private static final String KEY_NEGATIVE_BUTTON = "negativeButton";
  private static final String KEY_RATIONALE_BODY = "rationaleBody";
  private static final String KEY_REQUEST_CODE = "requestCode";
  private static final String KEY_PERMISSIONS = "permsNecessary";
  private static final String KEY_PERMISSIONS_NONNECESSARY = "permsNonNecessary";

  int positiveButton;
  int negativeButton;
  int requestCode;
  PermissionRationaleBody body;
  ArrayList<PermissionRationaleBody> permsNecessary;
  ArrayList<PermissionRationaleBody> permsNonNecessary;

  RationaleDialogConfig(@StringRes int positiveButton, @StringRes int negativeButton,
      @NonNull PermissionRationaleBody body, int requestCode,
      @NonNull ArrayList<PermissionRationaleBody> permsNecessary,
      @NonNull ArrayList<PermissionRationaleBody> permsNonNecessary) {

    this.positiveButton = positiveButton;
    this.negativeButton = negativeButton;
    this.body = body;
    this.requestCode = requestCode;
    this.permsNecessary = permsNecessary;
    this.permsNonNecessary = permsNonNecessary;
  }

  RationaleDialogConfig(Bundle bundle) {
    positiveButton = bundle.getInt(KEY_POSITIVE_BUTTON);
    negativeButton = bundle.getInt(KEY_NEGATIVE_BUTTON);
    body = bundle.getParcelable(KEY_RATIONALE_BODY);
    requestCode = bundle.getInt(KEY_REQUEST_CODE);
    permsNecessary = bundle.getParcelableArrayList(KEY_PERMISSIONS);
    permsNonNecessary = bundle.getParcelableArrayList(KEY_PERMISSIONS_NONNECESSARY);
  }

  Bundle toBundle() {
    Bundle bundle = new Bundle();
    bundle.putInt(KEY_POSITIVE_BUTTON, positiveButton);
    bundle.putInt(KEY_NEGATIVE_BUTTON, negativeButton);
    bundle.putParcelable(KEY_RATIONALE_BODY,body);
    bundle.putInt(KEY_REQUEST_CODE, requestCode);
    bundle.putParcelableArrayList(KEY_PERMISSIONS, permsNecessary);
    bundle.putParcelableArrayList(KEY_PERMISSIONS_NONNECESSARY, permsNonNecessary);
    return bundle;
  }

  static Dialog createNewDialog(final Context context,PermissionRationaleBody body) {
     final Dialog dialog = new PermissionBaseDialog(context);
     TextView surelTv;
     TextView cancelTv;
    dialog.setContentView(R.layout.necessary_dialog);
    ((TextView) dialog.findViewById(R.id.tv_permission_dialog_title)).setText(body.getTitle());
    ((TextView) dialog.findViewById(R.id.tv_permission_dialog_content))
        .setText(body.getRationaleContent());
    surelTv = dialog.findViewById(R.id.tv_permission_sure);
    surelTv.setText(R.string.plz_open_perm_sure);
    surelTv.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dialog.dismiss();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
      }
    });
    cancelTv = dialog.findViewById(R.id.tv_permission_cancel);
    cancelTv.setText(R.string.plz_open_perm_cancel);
    cancelTv.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dialog.dismiss();
      }
    });
    return dialog;
  }

}
