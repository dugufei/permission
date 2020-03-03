package com.penny.permission;

import androidx.collection.ArrayMap;

public class PermissionManager {
  private static ArrayMap<String,Boolean> permissionList = new ArrayMap<>();

  public static void updatePermShouldShowRationale(String perm,Boolean state){
    permissionList.put(perm,state);
  }

  public static boolean shouldShowRationale(String perm){
    return permissionList.getOrDefault(perm,true);
  }
}
