package com.example.myapplication;

import android.Manifest;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.penny.permission.AfterAllPermGranted;
import com.penny.permission.AfterNecessaryPermGranted;
import com.penny.permission.EasyPermissions;
import com.penny.permission.PermissionRationaleBody;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

  Button button ;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    button = findViewById(R.id.button);
  }

  public void button(View v){
    checkPermission1(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE);
  }

  public void button2(View v){
    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
  }
  public void button3(View v){
    //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
  }

  public void checkPermission1(@NonNull String... perms){
    //ActivityCompat.requestPermissions(this,perms,0);
    ArrayList<PermissionRationaleBody> bodyList = new ArrayList<>();
    ArrayList<PermissionRationaleBody> bodyList1 = new ArrayList<>();
    bodyList.add(new PermissionRationaleBody(Manifest.permission.ACCESS_FINE_LOCATION,"请允许定位权限","title"));
    bodyList.add(new PermissionRationaleBody(Manifest.permission.WRITE_EXTERNAL_STORAGE,"请允许存储权限","title"));
    bodyList1.add(new PermissionRationaleBody(Manifest.permission.CAMERA,"请允许相机权限","title"));
    EasyPermissions.requestPermission(this,bodyList,bodyList1,1000);
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    EasyPermissions.onRequestPermissionsResult(this,requestCode,permissions,grantResults,this);
  }

  @AfterAllPermGranted(1000)
  public void allPermAllow(){
    Log.e("===","所有权限允许");
  }
  @AfterNecessaryPermGranted(1000)
  public void allNecessaryPermAllow(){
    Log.e("===","必要权限允许");

  }
}
