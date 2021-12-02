package com.android.wannasing.home.fragment.map.permissioncheck;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import java.util.List;

public class PermissionChecker {

  PermissionListener permissionlistener;

  private PermissionChecker(Runnable runnableWhenGranted, Runnable runnableWhenDenied) {
    permissionlistener = new PermissionListener() {
      @Override
      public void onPermissionGranted() {
        runnableWhenGranted.run();
      }

      @Override
      public void onPermissionDenied(List<String> deniedPermissions) {
        runnableWhenDenied.run();
      }
    };
  }

  public static PermissionChecker create(Runnable runnableWhenGranted,
      Runnable runnableWhenDenied) {
    return new PermissionChecker(runnableWhenGranted, runnableWhenDenied);
  }

  public void requestPermissions(String... permissions) {
    TedPermission.create()
        .setPermissionListener(permissionlistener)
        .setDeniedMessage(
            "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
        .setPermissions(permissions)
        .check();
  }
}
