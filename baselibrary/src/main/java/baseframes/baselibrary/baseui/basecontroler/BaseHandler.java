package baseframes.baselibrary.baseui.basecontroler;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

/**
 * Created by zhanghs on 2017/11/21/021.
 * 防止内存泄漏的handler
 */

public abstract class BaseHandler extends Handler {
    protected WeakReference<Activity> activityWeakReference;
    protected WeakReference<Fragment> fragmentWeakReference;
    /** 标记异步操作返回时目标界面已经消失时的处理状态 */
    public static final int ACTIVITY_GONE = -1;
    protected BaseHandler(){}
    public BaseHandler(Activity activity){
        this.activityWeakReference=new WeakReference<Activity>(activity);
    }
    public BaseHandler(Fragment fragment){
        this.fragmentWeakReference=new WeakReference<Fragment>(fragment);
    }

    @Override
    public void handleMessage(Message msg) {
        if (activityWeakReference != null&& activityWeakReference.get() != null&& (!activityWeakReference.get().isFinishing())) {
            // 确认Activity是否可用
            handleMessage(msg,  msg.what);
            return;
        } else if(fragmentWeakReference != null && fragmentWeakReference.get() != null &&(! fragmentWeakReference.get().isRemoving())){
            handleMessage(msg,  msg.what);
            return;
        }else {
            handleMessage(msg,ACTIVITY_GONE);
        }
    }
    /**
     * 抽象方法用户实现,用来处理具体的业务逻辑
     *
     * @param msg
     * @param what
     */
    public abstract void handleMessage(Message msg, int what);
}
