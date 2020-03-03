package com.penny.permission.UI;

import android.app.Dialog;
import android.content.Context;
import com.penny.permission.R;

public class PermissionBaseDialog extends Dialog {
  public PermissionBaseDialog(Context context) {
    super(context, R.style.permission_dialog);
    setCancelable(false);
  }
}