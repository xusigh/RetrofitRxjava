package baseframes.base.rxtext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhanghs on 2017/11/30/030.
 */

public class ClickDrawView extends View {
    private int mHeight,mWidth;
    private Paint paint;
    private Matrix matrix;
    private int type=0;
    private float radio=50f;
    private Region circleRegion;
    private Path circlePath;
    private float xTouch,yTouch;
    public ClickDrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        matrix=new Matrix();
        circlePath=new Path();
        circleRegion=new Region();
    }

    private void initPaint() {
        paint=new Paint();
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#ff0000"));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xTouch= event.getRawX();
                yTouch=  event.getRawY();
                float [] touch={xTouch,yTouch};
                matrix.mapPoints(touch);
                System.out.println("    x      "+touch[0]+"    y   "+touch[1]);
                if(circleRegion.contains((int) touch[0],(int) touch[1])){
                    type=1;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                type=0;
                invalidate();
                break;
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=h;
        circlePath.addCircle(0,0,radio, Path.Direction.CW);
        Region regon=new Region(-w,-h,w,h);
        circleRegion.setPath(circlePath,regon);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth/2,mHeight/2);
        canvas.getMatrix().invert(matrix);

        if(type!=0){
            paint.setColor(Color.BLUE);
        }else {
            paint.setColor(Color.RED);
        }
        canvas.drawPath(circlePath,paint);
    }
}
