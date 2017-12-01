package baseframes.base.rxtext;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import baseframes.base.R;
import baseframes.base.viewtest.DataWang;

import static baseframes.base.rxtext.PathTestView.TYPE.END;
import static baseframes.base.rxtext.PathTestView.TYPE.NONE;
import static baseframes.base.rxtext.PathTestView.TYPE.SEARCH;
import static baseframes.base.rxtext.PathTestView.TYPE.STARING;

/**
 * Created by zhanghs on 2017/11/27/027.
 */

public class PathTestView extends View{
    private  Context context;
    private float RULE=0.551915024494f;
    private DataWang data;
    private Paint paint;
    private int mWidth,mHeight;
    private float min;
    private PoinrF startP,endP,controlP;
    private float radio=150f;
    private int bowenW=50;//波纹的长度
    private float bowenY=100f;//波纹的水平轴的高度
    private float bh=60f;//波纹的起伏高度
    private Integer offset=0;
    private float change=0.000f;
    private Integer offset2=0,offset3=0;
    private AnimatorSet set;
    private ValueAnimator animator;
    private ValueAnimator animator2;
    private ValueAnimator animator3;
    private ValueAnimator animator4;
    private Path path_search;
    private Path path_circle;
    private TYPE curentType= NONE;
    private ValueAnimator animate_starting;
    private ValueAnimator animate_searching;
    private ValueAnimator.AnimatorUpdateListener updateListener;
    private Animator.AnimatorListener listener;
    private ValueAnimator animator_end;

    enum TYPE{
        NONE,STARING,SEARCH,END
    }
    private Handler handler;
    public PathTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPain();
        startP=new PoinrF(0,0);
        endP=new PoinrF(0,0);
        controlP=new PoinrF(0,0);
        this.context = context;
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (curentType){
                    case NONE:
                        animate_starting.start();
                        curentType=STARING;
                        break;
                    case STARING:
                        animate_searching.start();
                        curentType=SEARCH;
                        break;
                    case SEARCH:
                        animator_end.start();
                        curentType=END;
                        break;
                    case END:
                        curentType=NONE;
                        break;
                    default:break;
                }
            }
        };
    }
    public void startSearch(){
        if(curentType==NONE){
            handler.sendEmptyMessage(0);
        }
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=h;
        startP.x=50;
        endP.x=250;
        startP.y=mHeight/2;
        endP.y=mHeight/2;
        bowenW=mWidth/4;
        radio=bowenW/2;
    }

    private void initPain() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.translate(mWidth/2,mHeight/2);
//        testPath1(canvas);
        /*drawWang(canvas);
        if (data!=null){
            drawBg(canvas);
        }*/
//        drawBei(canvas);
//        drawCircle(canvas);
//        drawCircle2(canvas);
//        drawBowen(canvas);
//        drawCircle3(canvas);
//        drawYu(canvas);
//        drawJian(canvas);
        /*initpath();
        initAnimate();
        drawSearch(canvas);*/
        drawRotate(canvas);
    }
    //波纹动画
    public void startAnimate4() {
        ValueAnimator animator = ValueAnimator.ofInt(0,360); //mWL是一段波纹的长度
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (Integer) animation.getAnimatedValue(); //offset 的值的范围在[0,mWL]之间。
                invalidate();
            }
        });
        animator.start();
    }
    private void drawRotate(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);
        canvas.drawRect(new RectF(0,0,100,100),paint);
        Camera camera=new Camera();
        camera.rotate(offset,offset,offset);
        camera.applyToCanvas(canvas);

    }



    private void initAnimate() {
        updateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                change = (float) valueAnimator.getAnimatedValue(); //offset 的值的范围在[0,mWL]之间。
                invalidate();
            }
        };
        listener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        };

        animate_starting = ValueAnimator.ofFloat(0,1);
        animate_starting.setDuration(1000).setInterpolator(new LinearInterpolator());
        animate_starting.setRepeatCount(0);
        animate_starting.addUpdateListener(updateListener);
        animate_starting.addListener(listener);

        animate_searching = ValueAnimator.ofFloat(0,1);
        animate_searching.setDuration(2000).setRepeatCount(2);
        animate_searching.setInterpolator(new LinearInterpolator());
        animate_searching.addUpdateListener(updateListener);
        animate_searching.addListener(listener);

        animator_end = ValueAnimator.ofFloat(1,0);
        animator_end.setDuration(1000).setRepeatCount(0);
        animator_end.setInterpolator(new LinearInterpolator());
        animator_end.addUpdateListener(updateListener);
        animator_end.addListener(listener);

    }

    private void initpath() {
        path_search = new Path();
        path_search.addArc(new RectF(-50,-50,50,50),45, (float) 359.9);




        path_circle = new Path();
        path_circle.addArc(new RectF(-100,-100,100,100),45, (float) 359.9);

        PathMeasure measure=new PathMeasure();
        float[] pos = new float[2];
        measure.setPath(path_circle, false);               // 放大镜把手的位置
        measure.getPosTan(0, pos, null);
        path_search.lineTo(pos[0], pos[1]);                 // 放大镜把手
    }

    //画搜索的旋转控件
    private void drawSearch(Canvas canvas){
        PathMeasure measure=new PathMeasure();
        canvas.translate(mWidth/2,mHeight/2);
        float length;
        Path drawPath;
        switch (curentType){
            case NONE:
                canvas.drawPath(path_search,paint);
                break;
            case STARING:
                measure.setPath(path_search,false);
                length=measure.getLength();
                drawPath=new Path();
                measure.getSegment(change*length,length,drawPath,true);
                canvas.drawPath(drawPath,paint);
                break;
            case SEARCH:
                measure.setPath(path_circle,false);
                length=measure.getLength();
                float stop = length * change;
                float start = (float) (stop - ((0.5 - Math.abs(change - 0.5)) * 200f));
                drawPath=new Path();
                measure.getSegment(start,stop,drawPath,true);
                canvas.drawPath(drawPath,paint);
                break;
            case END:
                measure.setPath(path_search,false);
                length=measure.getLength();
                drawPath=new Path();
                measure.getSegment(change*length,length,drawPath,true);
                canvas.drawPath(drawPath,paint);
                break;
            default:break;
        }
    }

    //画旋转箭头
    private void drawJian(Canvas canvas){
        canvas.translate(mWidth/2,mHeight/2);//将画布居中
        Path path=new Path();
        path.addCircle(0,0,100, Path.Direction.CW);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap=BitmapFactory.decodeResource(context.getResources(), R.mipmap.jiantou,options);
        PathMeasure measure=new PathMeasure(path,true);
        float distance=measure.getLength();
        Matrix matrix=new Matrix();
        measure.getMatrix(distance*offset/1000,matrix,PathMeasure.POSITION_MATRIX_FLAG|PathMeasure.TANGENT_MATRIX_FLAG);
        matrix.preTranslate(-bitmap.getWidth()/2,-bitmap.getHeight()/2);
        canvas.drawPath(path,paint);
        canvas.drawBitmap(bitmap,matrix,paint);
    }

    //画八卦
    private void drawYu(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);//将画布居中
//        canvas.scale(1,-1);//y轴对调
        //注意将y轴对调的话，会让矩形的上下坐标对调
        Path path=new Path();
        float r=50f;
        path.addArc(new RectF(-r,-2*r,r,0),-90,180);
        path.addArc(new RectF(-2*r,-2*r,2*r,2*r),90,180);
        path.addArc(new RectF(-r,0,r,2*r),90,180);
        path.setFillType(Path.FillType.EVEN_ODD);
        path.close();
        canvas.drawPath(path,paint);
    }
    //波纹动画
    public void startAnimate3() {
        ValueAnimator animator = ValueAnimator.ofInt(0,250); //mWL是一段波纹的长度
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (Integer) animation.getAnimatedValue(); //offset 的值的范围在[0,mWL]之间。
                invalidate();
            }
        });

        animator.start();
    }
    //画弹性圆
    private void drawCircle3(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);//将画布居中
        canvas.scale(1,-1);//y轴对调
        Path path=new Path();
        path.moveTo(0+offset3,radio);
        path.cubicTo(radio*RULE+offset3,radio,  radio+offset+offset3,radio*RULE, radio+offset+offset3,0);
        path.cubicTo(radio+offset+offset3,-radio*RULE,radio*RULE+offset3,-radio,0+offset3,-radio);
        path.cubicTo(-radio*RULE+offset3,-radio,-radio-offset2+offset3,-radio*RULE,-radio-offset2+offset3,0);
        path.cubicTo(-radio-offset2+offset3,radio*RULE,-radio*RULE+offset3,radio,0+offset3,radio);
        canvas.drawPath(path,paint);
    }
    //波纹动画
    public void startAnimate() {
        ValueAnimator animator = ValueAnimator.ofInt(0,  bowenW*4); //mWL是一段波纹的长度
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (Integer) animation.getAnimatedValue(); //offset 的值的范围在[0,mWL]之间。
                invalidate();
            }
        });

        animator.start();
    }
    //弹性球的动画
    public void startAnimate2() {
        //mWL是一段波纹的长度
        animator = ValueAnimator.ofInt(0,  bowenW/4);
        animator.setDuration(300);
        animator.setRepeatCount(0);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (Integer) animation.getAnimatedValue(); //offset 的值的范围在[0,mWL]之间。
                invalidate();
            }
        });

        animator2 = ValueAnimator.ofInt(0,bowenW/4);
        animator2.setDuration(300);
        animator2.setRepeatCount(0);
        animator2.setInterpolator(new LinearInterpolator());
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset2 = (Integer) animation.getAnimatedValue(); //offset 的值的范围在[0,mWL]之间。
                invalidate();
            }
        });

        animator3 = ValueAnimator.ofInt(bowenW/4,0);
        animator3.setDuration(150);
        animator3.setRepeatCount(0);
        animator3.setInterpolator(new LinearInterpolator());
        animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (Integer) animation.getAnimatedValue(); //offset 的值的范围在[0,mWL]之间。
                invalidate();
            }
        });

        animator4 = ValueAnimator.ofInt(bowenW/4,0);
        animator4.setDuration(150);
        animator4.setRepeatCount(0);
        animator4.setInterpolator(new LinearInterpolator());
        animator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset2 = (Integer) animation.getAnimatedValue(); //offset 的值的范围在[0,mWL]之间。
                invalidate();
            }
        });

        ValueAnimator animator5 = ValueAnimator.ofInt(0,bowenW);
        animator5.setDuration(900);
        animator5.setRepeatCount(0);
        animator5.setInterpolator(new LinearInterpolator());
        animator5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset3 = (Integer) animation.getAnimatedValue(); //offset 的值的范围在[0,mWL]之间。
                invalidate();
            }
        });
        set = new AnimatorSet();
        set.play(animator).with(animator5);
        set.play(animator).before(animator2);
        set.play(animator2).before(animator3);
        set.play(animator3).before(animator4);
        set.start();
    }


    //画波纹
    private void drawBowen(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);//将画布居中
//        canvas.scale(1,-1);//y轴对调
        Path path=new Path();
        path.moveTo(-6*bowenW+offset,-bowenY);
        path.quadTo(-5*bowenW+offset,-bowenY+bh,-4*bowenW+offset,-bowenY);
        path.quadTo(-3*bowenW+offset,-bowenY-bh,-2*bowenW+offset,-bowenY);
        path.quadTo(-bowenW+offset,-bowenY+bh,0+offset,-bowenY);
        path.quadTo(bowenW+offset,-bowenY-bh,2*bowenW+offset,-bowenY);
        path.lineTo(2*bowenW+offset,2*bowenY);
        path.lineTo(-6*bowenW+offset,2*bowenY);
        path.close();
      /*  path.moveTo(-4*bowenW+offset,bowenY);
        path.quadTo(-3*bowenW+offset,bowenY+bh,-2*bowenW+offset,bowenY);
        path.quadTo(-bowenW+offset,bowenY-bh,0+offset,bowenY);
        path.quadTo(bowenW+offset,bowenY+bh,2*bowenW+offset,bowenY);
        path.quadTo(3*bowenW+offset,bowenY+bh,4*bowenW+offset,bowenY);*/
        canvas.drawPath(path,paint);
    }

    private void drawCircle2(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);//将画布居中
        canvas.scale(1,-1);//y轴对调
        Path path=new Path();
        path.moveTo(0,radio);
        path.cubicTo(radio*RULE,radio, (float) (radio*1.3),radio*RULE, (float) (radio*1.3),0);
        path.cubicTo((float) (radio*1.3),-radio*RULE,radio*RULE,-radio,0,-radio);
        path.cubicTo(-radio*RULE,-radio,-radio,-radio*RULE,-radio,0);
        path.cubicTo(-radio,radio*RULE,-radio*RULE,radio,0,radio);
        canvas.drawPath(path,paint);
    }

    private void drawCircle(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);
        Paint paint1=new Paint();
        paint1.setColor(Color.parseColor("#ff0000"));
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(5);
        Path path=new Path();
        path.moveTo(0,-radio);
        path.cubicTo(radio*RULE,-radio,radio,-radio*RULE,radio,0);
        path.cubicTo(radio,radio*RULE,radio*RULE,radio,0,radio);
        path.cubicTo(-radio*RULE,radio,-radio,radio*RULE,-radio,0);
        path.cubicTo(-radio,-radio*RULE,-radio*RULE,-radio,0,-radio);
        canvas.drawPath(path,paint1);
    }
    //画雷达图的网
    private void drawWang(Canvas canvas) {
        Path path=new Path();
        min = 50;
        paint.setColor(Color.parseColor("#8a8a8a"));
        for (int i = 1; i < 6; i++) {
            path.reset();
            path.moveTo(min *i,0);
            path.lineTo(min *i/2,- (float) (Math.sqrt(3)* min *i/2));
            path.lineTo(-min *i/2,- (float) (Math.sqrt(3)* min *i/2));
            path.lineTo(-min *i,0);
            path.lineTo(-min /2*i,(float) (Math.sqrt(3)* min *i/2));
            path.lineTo(min *i/2,(float) (Math.sqrt(3)* min *i/2));
            path.close();
            canvas.drawPath(path,paint);
        }
        canvas.drawLine(0,0,5* min,0,paint);
        canvas.drawLine(0,0,5* min /2,-(float) (5* min /2*Math.sqrt(3)),paint);
        canvas.drawLine(0,0,-5* min /2,-(float) (5* min /2*Math.sqrt(3)),paint);
        canvas.drawLine(0,0,-5* min,0,paint);
        canvas.drawLine(0,0,-5* min /2,(float) (5* min /2*Math.sqrt(3)),paint);
        canvas.drawLine(0,0,5* min /2+1,(float) (5* min /2*Math.sqrt(3)),paint);
    }

    private void testPath1(Canvas canvas) {
        Path p=new Path();
        p.lineTo(100,100);
        p.lineTo(100,0);
        p.setLastPoint(100,50);
        canvas.drawPath(p,paint);
    }
    public void setData(DataWang data){
        this.data=data;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controlP.x= (int) event.getX();
        System.out.println("--------------------   "+controlP.x);
        controlP.y= (int) event.getY();
        System.out.println("--------------------   "+controlP.y);
        invalidate();
        return true;
    }

    private void drawBg(Canvas canvas){
        Paint p=new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.argb(50,255,0,0));
        Path path=new Path();
        System.out.println(5*min*data.getA()/200+"                ==================");
        path.moveTo(5*min*data.getA()/100,0);
        path.lineTo(5*min/2*data.getB()/100,-(float) (5*min/2*Math.sqrt(3)*data.getB()/100));
        path.lineTo(-5*min/2*data.getC()/100,-(float) (5*min/2*Math.sqrt(3)*data.getC()/100));
        path.lineTo(-5*min*data.getD()/100,0);
        path.lineTo(-5*min/2*data.getE()/100,(float) (5*min/2*Math.sqrt(3)*data.getE()/100));
        path.lineTo(5*min/2*data.getF()/100,(float) (5*min/2*Math.sqrt(3)*data.getF()/100));
        path.lineTo(5*min*data.getA()/100,0);

        canvas.drawPath(path,p);
    }
    private void drawBei(Canvas canvas){
        Path path=new Path();
        canvas.drawPoint(startP.x,startP.y,paint);
        canvas.drawPoint(endP.x,endP.y,paint);
        canvas.drawPoint(controlP.x,controlP.y,paint);
        canvas.drawLine(startP.x,startP.y,controlP.x,controlP.y,paint);
        canvas.drawLine(endP.x,endP.y,controlP.x,controlP.y,paint);
        Paint paint1=new Paint();
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(5);
        paint1.setColor(Color.parseColor("#ff0000"));
        path.moveTo(startP.x,startP.y);
        path.quadTo(controlP.x,controlP.y,endP.x,endP.y);
        canvas.drawPath(path,paint1);
    }
}
