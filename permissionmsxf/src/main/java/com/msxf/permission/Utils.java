package com.msxf.permission;

import java.util.ArrayList;
import java.util.List;

public class Utils {
  public static String[] transferPermBodylist(List<PermissionRationaleBody> permNecessarys,
      List<PermissionRationaleBody> permNonNecessays) {
    int a = permNecessarys.size();
    int b = permNonNecessays.size();
    String[] permsArray = new String[a + b];
    List<String> perms = new ArrayList<>();
    for (PermissionRationaleBody permNecessary : permNecessarys) {
      perms.add(permNecessary.getPermission());
    }
    for (PermissionRationaleBody permNonNecessary : permNonNecessays) {
      perms.add(permNonNecessary.getPermission());
    }
    return perms.toArray(permsArray);
  }

}
