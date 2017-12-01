package baseframes.base.viewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



/**
 * Created by zhanghs on 2017/11/29/029.
 */

public class YaoKongQi extends View {
    private final Matrix mMapMatrix;
    private int mWidth,mHeight;
    private Paint normalPaint;
    private Paint selectPaint;
    private  float RADIO=50f,FEN=25f;
    private float event_x=0,event_y=0;
    private int clickType=-1;//0是中间，1是上边，2是左边，3是下边，4是右边
    private Region regionCircle,regionTop,regionLeft,regionRight,regionBottom;
    private Path pathCircle,pathTop,pathLeft,pathRight,pathBottom;
    public YaoKongQi(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        mMapMatrix = new Matrix();
    }

    private void initPaint() {
        normalPaint=new Paint();
        normalPaint.setStyle(Paint.Style.FILL);
        normalPaint.setColor(Color.BLACK);
        normalPaint.setAntiAlias(true);
        normalPaint.setStrokeWidth(2);

        selectPaint=new Paint();
        selectPaint.setColor(Color.RED);
        selectPaint.setAntiAlias(true);
        selectPaint.setStyle(Paint.Style.FILL);
        selectPaint.setStrokeWidth(2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=h;

        Region region=new Region(-w,-h,w,h);
        initPath(region);
    }

    private void initPath(Region region) {
        pathCircle=new Path();
        regionCircle=new Region();
        pathCircle.addCircle(0,0,RADIO, Path.Direction.CW);
        pathCircle.close();
        regionCircle.setPath(pathCircle,region);

        pathTop=new Path();
        regionTop=new Region();
        pathTop.addArc(new RectF(-RADIO,-RADIO-FEN,RADIO,RADIO-FEN),225,90);
        pathTop.arcTo(new RectF(-RADIO-60,-RADIO-60-FEN,60+RADIO,RADIO+60-FEN),-45,-90);
        pathTop.close();
        regionTop.setPath(pathTop,region);

        pathBottom=new Path();
        regionBottom=new Region();
        pathBottom.addArc(new RectF(-RADIO,-RADIO+FEN,RADIO,RADIO+FEN),45,90);
        pathBottom.arcTo(new RectF(-RADIO-60,-RADIO-60+FEN,60+RADIO,RADIO+60+FEN),135,-90);
        pathBottom.close();
        regionBottom.setPath(pathBottom,region);

        pathLeft=new Path();
        regionLeft=new Region();
        pathLeft.addArc(new RectF(-RADIO-FEN,-RADIO,FEN,RADIO),135,90);
        pathLeft.arcTo(new RectF(-RADIO-FEN-60,-RADIO-60,FEN+60,RADIO+60),-135,-90);
        pathLeft.close();
        regionLeft.setPath(pathLeft,region);

        pathRight=new Path();
        regionRight=new Region();
        pathRight.addArc(new RectF(-FEN,-RADIO,FEN+RADIO,RADIO),45,-90);
        pathRight.arcTo(new RectF(-FEN-60,-RADIO-60,FEN+60+RADIO,RADIO+60),-45,90);
        pathRight.close();
        regionRight.setPath(pathRight,region);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth/2,mHeight/2);
        mMapMatrix.reset();
        // 获取测量矩阵(逆矩阵)
        if (mMapMatrix.isIdentity()) {
            System.out.println("转换了");
            canvas.getMatrix().invert(mMapMatrix);
        }

        drawCircle(canvas);
        drawTop(canvas);
        drawBottom(canvas);
        drawLeft(canvas);
        drawRight(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                event_x=event.getRawX();
                event_y=event.getRawY();
                System.out.println("      x    "+event_x+"       y        "+event_y);
                float [] dpt={event_x,event_y};
                mMapMatrix.mapPoints(dpt);
                getType((int)dpt[0],(int) dpt[1]);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                clickType=-1;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                clickType=-1;
                invalidate();
                break;
            default:break;
        }
        return true;
    }
    private void getType(int x,int y){
            System.out.println("              x              "+x+"                  y                   "+y);
            if(regionCircle.contains(x,y)){
                System.out.println("圆");
                clickType=0;
            }
            if(regionTop.contains(x,y)){
                clickType=1;
            }
            if(regionLeft.contains(x,y)){
                clickType=2;
            }
            if(regionBottom.contains(x,y)){
                clickType=3;
            }
            if(regionRight.contains(x,y)){
                clickType=4;
            }

    }


    private void drawCircle(Canvas canvas) {
        if(clickType!=-1&&clickType==0){
            canvas.drawPath(pathCircle,selectPaint);
        }else {
            canvas.drawPath(pathCircle,normalPaint);
        }
    }

    private void drawRight(Canvas canvas) {
        if(clickType!=-1&&clickType==4){
            canvas.drawPath(pathRight,selectPaint);
        }else {
            canvas.drawPath(pathRight,normalPaint);
        }
    }

    private void drawLeft(Canvas canvas) {
        if(clickType!=-1&&clickType==2){
            canvas.drawPath(pathLeft,selectPaint);
        }else {
            canvas.drawPath(pathLeft,normalPaint);
        }
    }

    private void drawBottom(Canvas canvas) {
        if(clickType!=-1&&clickType==3){
            canvas.drawPath(pathBottom,selectPaint);
        }else {
            canvas.drawPath(pathBottom,normalPaint);
        }
    }

    private void drawTop(Canvas canvas) {
        if(clickType!=-1&&clickType==1){
            canvas.drawPath(pathTop,selectPaint);
        }else {
            canvas.drawPath(pathTop,normalPaint);
        }
    }
}
