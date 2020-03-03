package com.penny.permission.helper;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.penny.permission.PermissionRationaleBody;
import com.penny.permission.R;
import com.penny.permission.UI.PermissionBaseDialog;
import com.penny.permission.Utils;
import java.util.List;

public class ActivityPermissionHelper extends BaseFrameworkPermissionsHelper<Activity> {

  private Dialog dialog;
  private TextView surelTv;
  private TextView cancelTv;

  public ActivityPermissionHelper(Activity host) {
    super(host);
  }

  @Override public FragmentManager getFragmentManager() {
    return getHost().getFragmentManager();
  }

  @Override public Context getContext() {
    return getHost();
  }

  @Override public void showDialog(@NonNull PermissionRationaleBody body) {
    dialog = new PermissionBaseDialog(getHost());
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
        Uri uri = Uri.fromParts("package", getHost().getPackageName(), null);
        intent.setData(uri);
        getHost().startActivity(intent);
      }
    });
    cancelTv = dialog.findViewById(R.id.tv_permission_cancel);
    cancelTv.setText(R.string.plz_open_perm_cancel);
    cancelTv.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dialog.dismiss();
      }
    });
    dialog.show();
  }

  @Override
  public PermissionRationaleBody shouldShowRequestPermissionRationale(
      @NonNull PermissionRationaleBody perm) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(getHost(), perm.getPermission())) {
      return perm;
    } else {
      return null;
    }
  }

  @Override public void directRequestPermissions(int requestCode,
      @NonNull List<PermissionRationaleBody> permNecessarys,
      @NonNull List<PermissionRationaleBody> permNonNecessays) {
    ActivityCompat.requestPermissions(getHost(),
        Utils.transferPermBodylist(permNecessarys, permNonNecessays), requestCode);
  }
}
