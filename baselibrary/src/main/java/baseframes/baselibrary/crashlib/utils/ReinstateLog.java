package baseframes.baselibrary.crashlib.utils;

import android.util.Log;

import baseframes.baselibrary.crashlib.rulers.Reinstate;

/**
 * Created by zhengxiaoyong on 16/8/26.
 */
public class ReinstateLog {

    private static final String TAG = "Reinstate";

    public static void e(String message) {
        if (Reinstate.getInstance().isDebug())
            Log.e(TAG, message);
    }
}
