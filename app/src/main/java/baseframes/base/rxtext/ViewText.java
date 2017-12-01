package baseframes.base.rxtext;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by zhanghs on 2017/11/29/029.
 */

public class ViewText extends TextView {
    public ViewText(Context context) {
        super(context);
    }

    public ViewText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        System.out.println("ViewText    ---------------------    dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }
    //处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        System.out.println("ViewText    ---------------------    onTouchEvent");
        return super.onTouchEvent(event);
    }
}
