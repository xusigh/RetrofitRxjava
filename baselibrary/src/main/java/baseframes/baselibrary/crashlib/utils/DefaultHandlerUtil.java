package baseframes.baselibrary.crashlib.utils;


import baseframes.baselibrary.crashlib.exception.ReinstateException;

/**
 * Created by zgxiaoyong on 16/8/28.
 */
public class DefaultHandlerUtil {

    private DefaultHandlerUtil() {
        throw new ReinstateException("Stub!");
    }

    private static Thread.UncaughtExceptionHandler getDefaultUncaughtExceptionHandler() {
        Object object = Reflect.on("com.android.internal.os.RuntimeInit$UncaughtHandler").constructor().newInstance();
        return object == null ? null : (Thread.UncaughtExceptionHandler) object;
    }

    public static boolean isSystemDefaultUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
        if (handler == null)
            return false;
        Thread.UncaughtExceptionHandler defHandler = getDefaultUncaughtExceptionHandler();
        return defHandler != null && defHandler.getClass().isInstance(handler);
    }

}
