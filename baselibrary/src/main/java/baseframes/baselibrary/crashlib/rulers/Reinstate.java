package baseframes.baselibrary.crashlib.rulers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import baseframes.baselibrary.baseui.baseui.UiReinstateActivityLifecycleCallback;
import baseframes.baselibrary.crashlib.callback.ReinstateCallback;
import baseframes.baselibrary.crashlib.exception.ReinstateException;
import baseframes.baselibrary.crashlib.utils.ReinstateLog;
import baseframes.baselibrary.crashlib.utils.ReinstateUtil;

/**
 * Created by zhengxiaoyong on 16/8/26.
 * 整体的恢复管理工具类，实现对数据的设置以及初始化
 */
public class Reinstate {

    private volatile static Reinstate sInstance;

    private static final Object LOCK = new Object();

    private Context mContext;

    private boolean isDebug = false;

    /**
     * The default to restore the stack.
     */
    private boolean isRecoverStack = true;

    /**
     * The default is not restore the background process.
     */
    private boolean isRecoverInBackground = false;

    private Class<? extends Activity> mMainPageClass;

    private ReinstateCallback mCallback;

    private boolean isSilentEnabled = false;

    private SilentMode mSilentMode = SilentMode.RECOVER_ACTIVITY_STACK;
    //不恢复的activity
    private List<Class<? extends Activity>> mSkipActivities = new ArrayList<>();

    /**
     * Whether to enter recovery mode.
     */
    private boolean isRecoverEnabled = true;

    private Reinstate() {
    }

    public static Reinstate getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new Reinstate();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context) {
        if (context == null)
            throw new ReinstateException("Context can not be null!");
        if (!(context instanceof Application))
            context = context.getApplicationContext();
        mContext = context;
        if (!ReinstateUtil.isMainProcess(context))
            return;
        registerRecoveryHandler();
        registerRecoveryLifecycleCallback();
    }

    public Reinstate debug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

    public Reinstate recoverStack(boolean isRecoverStack) {
        this.isRecoverStack = isRecoverStack;
        return this;
    }

    public Reinstate recoverInBackground(boolean isRecoverInBackground) {
        this.isRecoverInBackground = isRecoverInBackground;
        return this;
    }

    public Reinstate mainPage(Class<? extends Activity> clazz) {
        this.mMainPageClass = clazz;
        return this;
    }

    public Reinstate callback(ReinstateCallback callback) {
        this.mCallback = callback;
        return this;
    }

    public Reinstate silent(boolean enabled, SilentMode mode) {
        this.isSilentEnabled = enabled;
        this.mSilentMode = mode == null ? SilentMode.RECOVER_ACTIVITY_STACK : mode;
        return this;
    }

    @SafeVarargs
    public final Reinstate skip(Class<? extends Activity>... activities) {
        if (activities == null)
            return this;
        mSkipActivities.addAll(Arrays.asList(activities));
        return this;
    }

    public Reinstate recoverEnabled(boolean enabled) {
        this.isRecoverEnabled = enabled;
        return this;
    }

    private void registerRecoveryHandler() {
        ReinstateHandler.newInstance(Thread.getDefaultUncaughtExceptionHandler()).setCallback(mCallback).register();
    }

    private void registerRecoveryLifecycleCallback() {
        //封装了crash以及ui的annotation
        ((Application) mContext).registerActivityLifecycleCallbacks(new UiReinstateActivityLifecycleCallback());
    }

    private void registerRecoveryProxy() {
        //OS version in the 5.0 ~ 6.0 don`t register agent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;
        if (mMainPageClass == null)
            return;
        if (!ReinstateUtil.isMainProcess(ReinstateUtil.checkNotNull(mContext, "The context is not initialized")))
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    boolean isSuccess = ReinstateComponentHook.hookActivityManagerProxy();
                    ReinstateLog.e("hook is success:" + isSuccess);
                    if (isSuccess)
                        break;
                }
            }
        }).start();
    }

    public Context getContext() {
        return ReinstateUtil.checkNotNull(mContext, "The context is not initialized");
    }

    public boolean isDebug() {
        return isDebug;
    }

    boolean isRecoverInBackground() {
        return isRecoverInBackground;
    }

    boolean isRecoverStack() {
        return isRecoverStack;
    }

    boolean isRecoverEnabled() {
        return isRecoverEnabled;
    }

    Class<? extends Activity> getMainPageClass() {
        return mMainPageClass;
    }

    boolean isSilentEnabled() {
        return isSilentEnabled;
    }

    SilentMode getSilentMode() {
        return mSilentMode;
    }

    public List<Class<? extends Activity>> getSkipActivities() {
        return mSkipActivities;
    }

    public enum SilentMode {
        RESTART(1),
        RECOVER_ACTIVITY_STACK(2),
        RECOVER_TOP_ACTIVITY(3),
        RESTART_AND_CLEAR(4);

        int value;

        SilentMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static SilentMode getMode(int value) {
            switch (value) {
                case 1:
                    return RESTART;
                case 2:
                    return RECOVER_ACTIVITY_STACK;
                case 3:
                    return RECOVER_TOP_ACTIVITY;
                case 4:
                    return RESTART_AND_CLEAR;
                default:
                    return RECOVER_ACTIVITY_STACK;
            }
        }
    }
}
