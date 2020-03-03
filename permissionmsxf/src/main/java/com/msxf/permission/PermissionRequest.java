package com.msxf.permission;

import java.util.ArrayList;

public class PermissionRequest {
  private String requestId = "";
  private int requestCode = 0;
  private ArrayList<PermissionRationaleBody> permNecessarys;
  private ArrayList<PermissionRationaleBody> permNonNecessarys;

  public PermissionRequest(String requestId, int requestCode,
      ArrayList<PermissionRationaleBody> permNecessarys,
      ArrayList<PermissionRationaleBody> permNonNecessarys) {
    this.requestId = requestId;
    this.requestCode = requestCode;
    this.permNecessarys = permNecessarys;
    this.permNonNecessarys = permNonNecessarys;
  }

  public ArrayList<PermissionRationaleBody> getPermNecessarys(){
    return permNecessarys;
  }
}
