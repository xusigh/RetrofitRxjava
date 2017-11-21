package baseframes.baselibrary.crashlib.rulers;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import baseframes.baselibrary.crashlib.utils.ReinstateLog;
import baseframes.baselibrary.crashlib.utils.ReinstateUtil;

/**
 * Created by zhengxiaoyong on 16/8/30.
 * 对activitymanager的动态代理委托
 */
class ActivityManagerDelegate implements InvocationHandler {

    private Object mBaseActivityManagerProxy;

    ActivityManagerDelegate(Object o) {
        this.mBaseActivityManagerProxy = o;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        switch (methodName) {
            case "finishActivity":
//            case "finishActivityAffinity":
                Class<? extends Activity> clazz = baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().getMainPageClass();
                if (clazz == null)
                    break;
                int count = ActivityStackCompat.getActivityCount(baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().getContext());
                String baseActivityName = ActivityStackCompat.getBaseActivityName(baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().getContext());
                if (TextUtils.isEmpty(baseActivityName))
                    break;
                ReinstateLog.e("currentActivityCount: " + count);
                ReinstateLog.e("baseActivityName: " + baseActivityName);
                if (count == 1 && !clazz.getName().equals(baseActivityName)) {
                    Intent intent = new Intent(baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().getContext(), clazz);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    if (ReinstateUtil.isIntentAvailable(baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().getContext(), intent))
                        baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().getContext().startActivity(intent);
                }
                break;
            default:
                break;

        }

        return method.invoke(mBaseActivityManagerProxy, args);
    }

}
