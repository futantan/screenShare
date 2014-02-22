package util;

import android.util.Log;

/**
 * 包装系统提供的Log类，主要为其添加开关。
 */
public class LogUtil {
    private static boolean isLogOpen = false;

    public static void log(String tag, String msg) {
        if (isIsLogOpen()) {
            Log.d(tag, msg);
        }
    }

    public static void log(String msg) {
        log("screen", msg);
    }

    public static boolean isIsLogOpen() {
        return isLogOpen;
    }

    public static void setIsLogOpen(boolean isLogOpen) {
        LogUtil.isLogOpen = isLogOpen;
    }
}
