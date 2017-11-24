package baseframes.base.viewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhanghs on 2017/11/23/023.
 */

public class ScaleView extends View {
    private Paint paint;
    private int mWidth,mHeight;
    private int curent=0;
    private final Handler handler;

    public ScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(curent<30){
//                    this.sendEmptyMessageDelayed(0,500);
                    invalidate();
                    System.out.println("   send   =================");
                    curent+=1;
                }
            }
        };
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth/2,mHeight/2);
        RectF r1=new RectF(-300,-300,300,300);
        RectF r2=new RectF(-294,-294,294,294);
        canvas.drawArc(r1,0,360,false,paint);
        canvas.drawArc(r2,0,360,false,paint);
        canvas.drawLine(-300,0,-294,0,paint);
        System.out.println("---------------------------ondraw");
        canvas.rotate(12);
        handler.sendEmptyMessageDelayed(0, 500);
        for (int i = 0; i < curent; i++) {
            canvas.drawLine(-300, 0, -294, 0, paint);
            canvas.rotate(12);
        }

    }
}
