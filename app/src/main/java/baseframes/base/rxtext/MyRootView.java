package baseframes.base.rxtext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by zhanghs on 2017/11/29/029.
 */

public class MyRootView extends LinearLayout {
    public MyRootView(Context context) {
        super(context);
    }

    public MyRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        System.out.println("MyRootView    ---------------------    dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }
    //拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        System.out.println("MyRootView    ---------------------    onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }
    //处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        System.out.println("MyRootView    ---------------------    onTouchEvent");
        return super.onTouchEvent(event);
    }
}
