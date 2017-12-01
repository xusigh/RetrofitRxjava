package baseframes.base.rxtext;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by zhanghs on 2017/11/29/029.
 */

public class GroupViewA extends LinearLayout {
    public GroupViewA(Context context) {
        super(context);
    }

    public GroupViewA(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GroupViewA(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        System.out.println("GroupViewA    ---------------------    dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }
    //拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        System.out.println("GroupViewA    ---------------------    onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }
    //处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        System.out.println("GroupViewA    ---------------------    onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
