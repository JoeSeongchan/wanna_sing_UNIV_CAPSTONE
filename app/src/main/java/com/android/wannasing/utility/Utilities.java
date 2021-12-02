package com.android.wannasing.utility;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public final class Utilities {

  // function to log.
  public static void log(LogType type, String msg) {
    String callerClassName = Thread.currentThread().getStackTrace()[3].getClassName();
    int lastIndexOfDot = callerClassName.lastIndexOf(".");
    int endIdx = callerClassName.length();
    final String TAG = callerClassName.substring(lastIndexOfDot, endIdx) + "_R";
    String callerFuncName = Thread.currentThread().getStackTrace()[3].getMethodName();
    final String FUNC_TAG = "FN : " + callerFuncName + "\n";
    final String MSG = "MSG : " + msg;
    switch (type) {
      case d:
      default:
        Log.d(TAG, FUNC_TAG + MSG);
        break;
      case i:
        Log.i(TAG, FUNC_TAG + MSG);
        break;
      case w:
        Log.w(TAG, FUNC_TAG + MSG);
        break;
    }
  }

  public static void toast(Activity activity, String message) {
    activity.runOnUiThread(
        () -> Toast.makeText(activity.getBaseContext(), message, Toast.LENGTH_SHORT).show());
  }

  public static void logThread() {
    String currentThreadName = Thread.currentThread().getName();
    String callerClassName = Thread.currentThread().getStackTrace()[3].getClassName();
    int lastIndexOfDot = callerClassName.lastIndexOf(".");
    int endIdx = callerClassName.length();
    final String TAG = callerClassName.substring(lastIndexOfDot, endIdx) + "_R";
    String callerFuncName = Thread.currentThread().getStackTrace()[3].getMethodName();
    final String FUNC_TAG = "FN : " + callerFuncName + "\n";
    final String MSG = "THREAD : " + currentThreadName;
    Log.d(TAG, FUNC_TAG + MSG);
  }

  // function to make log string.
  public static String makeLog(String msg) {
    String callerClassName = Thread.currentThread().getStackTrace()[3].getClassName();
    int lastIndexOfDot = callerClassName.lastIndexOf(".");
    int endIdx = callerClassName.length();
    final String TAG = callerClassName.substring(lastIndexOfDot, endIdx) + "_R\n";
    String callerFuncName = Thread.currentThread().getStackTrace()[3].getMethodName();
    final String FUNC_TAG = "FN : " + callerFuncName + "\n";
    final String MSG = "MSG : " + msg;
    return TAG + FUNC_TAG + MSG;
  }

  public enum LogType {i, d, w}
}
