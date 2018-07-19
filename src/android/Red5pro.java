package org.apache.cordova.plugins.red5pro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaResourceApi;

import infrared5.com.red5proandroid.publish.Publish;
import infrared5.com.red5proandroid.subscribe.Subscribe;


public class Red5pro extends CordovaPlugin {

  public CallbackContext callbackContext;

  String[] permissions = {Manifest.permission_group.CAMERA, Manifest.permission_group.STORAGE, Manifest.permission_group.MICROPHONE};


  /**
   * Executes the request and returns a boolean.
   *
   * @param action          The action to execute.
   * @param args            JSONArry of arguments for the plugin.
   * @param callbackContext The callback context used when calling back into JavaScript.
   * @return boolean.
   */
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    this.callbackContext = callbackContext;
    //判断是否为android6.0系统版本，如果是，需要动态添加权限
    if (Build.VERSION.SDK_INT >= 23) {
      initPermission();
    }

    if (action.equals("record")) {
      //通过Intent绑定将要调用的Activity
      Intent intent = new Intent(this.cordova.getActivity(), Publish.class);
      //Intent intent=new Intent(this, Publish.class);
      //加入将要传输到activity中的参数
      // intent.putExtra("province", args.getString(0));
      //启动activity,带有返回结果，需要重写onActivityResult方法
      this.cordova.startActivityForResult(this, intent, 0);

    } else {
      JSONObject errorObj = new JSONObject();
      errorObj.put("status", PluginResult.Status.INVALID_ACTION.ordinal());
      errorObj.put("message", "Invalid action");
      callbackContext.error(errorObj);
    }
    return true;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    JSONObject successObj = new JSONObject();
    try {
      successObj.put("status", PluginResult.Status.OK);
      successObj.put("message", "success");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    callbackContext.success(successObj);

  }

  //获取权限
  public void initPermission() {
    cordova.requestPermissions(this, 0, permissions);
  }

  public void onRequestPermissionResult(int requestCode, String[] permissions,int[] grantResults) throws JSONException {
    for (int r : grantResults) {
      if (r == PackageManager.PERMISSION_DENIED) {
        this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, -1));
        return;
      }
    }
    switch (requestCode) {
      case 0:
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
        } else {
          // 没有获取到权限，做特殊处理
          Toast.makeText(this.cordova.getActivity(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
        }
        break;
    }
  }

}
