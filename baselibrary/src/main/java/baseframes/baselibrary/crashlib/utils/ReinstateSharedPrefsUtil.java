package baseframes.baselibrary.crashlib.utils;

import baseframes.baselibrary.crashlib.exception.ReinstateException;
import baseframes.baselibrary.crashlib.rulers.CrashData;
import baseframes.baselibrary.crashlib.rulers.Reinstate;

/**
 * Created by zhengxiaoyong on 16/8/27.
 */
public class ReinstateSharedPrefsUtil {

    private static final long DEFAULT_TIME_INTERVAL = 60 * 1000;

    private static final int DEFAULT_MAX_COUNT = 3;

    private static final String SHARED_PREFS_NAME = "recovery_info";

    private static final String CRASH_COUNT = "crash_count";

    private static final String CRASH_TIME = "crash_time";

    private static final String SHOULD_RESTART_APP = "should_restart_app";

    private ReinstateSharedPrefsUtil() {
        throw new ReinstateException("Stub!");
    }

    public static void recordCrashData() {
        int count = 0;
        long time = 0L;
        boolean shouldRestart = false;
        try {
            count = Integer.parseInt(get(CRASH_COUNT, String.valueOf(0)));
            time = Long.parseLong(get(CRASH_TIME, String.valueOf(0L)));
        } catch (Exception e) {
            count = 0;
            time = 0L;
            ReinstateLog.e(e.getMessage());
        }
        count = Math.max(count, 0);
        time = Math.max(time, 0L);

        count += 1;
        time = time == 0L ? System.currentTimeMillis() : time;

        long interval = System.currentTimeMillis() - time;
        if (count >= DEFAULT_MAX_COUNT && interval <= DEFAULT_TIME_INTERVAL) {
            shouldRestart = true;
            count = 0;
            time = 0L;
        } else {
            if (count >= DEFAULT_MAX_COUNT || interval > DEFAULT_TIME_INTERVAL) {
                count = 1;
                time = System.currentTimeMillis();
            }
        }
        CrashData data = CrashData.newInstance()
                .count(count)
                .time(time)
                .restart(shouldRestart);
        ReinstateLog.e(data.toString());
        saveCrashData(data);
    }

    private static void saveCrashData(CrashData data) {
        if (data == null)
            return;
        SharedPreferencesCompat.newBuilder(Reinstate.getInstance().getContext(), SHARED_PREFS_NAME)
                .put(CRASH_COUNT, String.valueOf(data.crashCount))
                .put(CRASH_TIME, String.valueOf(data.crashTime))
                .put(SHOULD_RESTART_APP, String.valueOf(data.shouldRestart))
                .apply();
    }

    public static boolean shouldRestartApp() {
        return Boolean.parseBoolean(get(SHOULD_RESTART_APP, String.valueOf(false)));
    }

    public static void clear() {
        SharedPreferencesCompat.clear(Reinstate.getInstance().getContext(), SHARED_PREFS_NAME);
    }

    private static void put(String key, String value) {
        SharedPreferencesCompat.put(Reinstate.getInstance().getContext(), SHARED_PREFS_NAME, key, value);
    }

    private static String get(String key, String defValue) {
        return SharedPreferencesCompat.get(Reinstate.getInstance().getContext(), SHARED_PREFS_NAME, key, defValue);
    }

}