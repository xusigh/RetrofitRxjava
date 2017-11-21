package baseframes.baselibrary.crashlib.callback;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import baseframes.baselibrary.crashlib.rulers.Reinstate;
import baseframes.baselibrary.crashlib.rulers.ReinstateActivity;
import baseframes.baselibrary.crashlib.rulers.ReinstateStore;
import baseframes.baselibrary.crashlib.utils.Reflect;

/**
 * Created by zhanghs on 2017/11/20/020.
 * 对activity的全局管理，在框架设计中通常都要对activity生命周期进行相应的处理记录
 */

public class ReinstateActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        //判断是否是应该跳过的activity或者是crash的显示activity
        boolean isLegal = ReinstateStore.getInstance().verifyActivity(activity);
        if (!isLegal)
            return;

        if (activity.getIntent().getBooleanExtra(ReinstateActivity.RECOVERY_MODE_ACTIVE, false)) {
            Reflect.on(Reinstate.class).method("registerRecoveryProxy").invoke(Reinstate.getInstance());
        }

        if (ReinstateStore.getInstance().contains(activity))
            return;

        Window window = activity.getWindow();
        if (window != null) {
            View decorView = window.getDecorView();
            if (decorView == null)
                return;
            decorView.post(new Runnable() {
                @Override
                public void run() {
                    ReinstateStore.getInstance().putActivity(activity);
                    Object o = activity.getIntent().clone();
                    ReinstateStore.getInstance().setIntent((Intent) o);
                }
            });
        }

    }
    @Override
    public void onActivityResumed(Activity activity) {
    }
    @Override
    public void onActivityPaused(Activity activity) {
    }
    @Override
    public void onActivityStopped(Activity activity) {
    }
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }
    @Override
    public void onActivityDestroyed(Activity activity) {
        ReinstateStore.getInstance().removeActivity(activity);
    }
}
