package baseframes.base.viewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhanghs on 2017/11/24/024.
 */

public class MyTextView extends View {

    private Paint paint;

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTextNormal(canvas);
        drawPostText(canvas);
    }
    private void drawTextNormal(Canvas canvas){
        String s="大家耗啊";
        canvas.drawText(s,0,2,100,100,paint);
    }
    private void drawPostText(Canvas canvas){
        String text="大家好啊";
        canvas.drawPosText(text,new float[]{20,20,40,40,80,80,120,120},paint);
    }
}
