package com.penny.permission;

import android.os.Parcel;
import android.os.Parcelable;

public class PermissionRationaleBody implements Parcelable {
  private String permission;
  private String rationaleContent;
  private String title;

  public PermissionRationaleBody(String perm,String rationaleContent,String title){
    this.permission = perm;
    this.rationaleContent = rationaleContent;
    this.title = title;
  }

  protected PermissionRationaleBody(Parcel in) {
    permission = in.readString();
    rationaleContent = in.readString();
    title = in.readString();
  }

  public static final Creator<PermissionRationaleBody> CREATOR =
      new Creator<PermissionRationaleBody>() {
        @Override
        public PermissionRationaleBody createFromParcel(Parcel in) {
          return new PermissionRationaleBody(in);
        }

        @Override
        public PermissionRationaleBody[] newArray(int size) {
          return new PermissionRationaleBody[size];
        }
      };

  public String getPermission(){
    return permission;
  }

  public String getRationaleContent(){
    return rationaleContent;
  }
  public String getTitle(){
    return title;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(permission);
    dest.writeString(rationaleContent);
    dest.writeString(title);
  }

}
