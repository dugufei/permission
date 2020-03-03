package com.msxf.permission.UI;

import android.app.Dialog;
import android.content.Context;
import com.msxf.permission.R;

public class PermissionBaseDialog extends Dialog {
  public PermissionBaseDialog(Context context) {
    super(context, R.style.permission_dialog);
    setCancelable(false);
  }
}