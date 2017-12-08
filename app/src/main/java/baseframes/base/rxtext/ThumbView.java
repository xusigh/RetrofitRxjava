package baseframes.base.rxtext;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import baseframes.base.R;

/**
 * Created by zhanghs on 2017/12/7/007.
 */

public class ThumbView extends View {
    private int mWidth,mHeight;
    private Paint painText;
    private Region regionThumb;
    private Matrix matrix;
    private Rect rectPic,rectDraw;
    private TYPE type=TYPE.NORMAL;
    private Bitmap bitmapNormal,bitmapSelect;
    private String text="123";
    private float[] listFloat;
    private float textStartX,textStartY;
    private int varySet=0;
    private ValueAnimator change;
    private ValueAnimator.AnimatorUpdateListener uplistenner;
    private boolean isClick=false,thumb=false,lengthChange=false;
    private float[] dst=new float[2];
    private Animator.AnimatorListener listener;

    enum TYPE{
        NORMAL,Thumb,UNThumb
    }
    public ThumbView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initBitmap(context);
        initRectF();
        initText();
        initValueAnimator();
    }



    private void initPaint() {
        painText=new Paint();
        painText.setTextSize(20);
        painText.setStyle(Paint.Style.FILL);
        painText.setColor(Color.RED);
        painText.setAntiAlias(true);
        matrix=new Matrix();

        textStartX=3f;
        textStartY=0f;

    }

    private void initBitmap(Context context) {
        bitmapNormal= BitmapFactory.decodeResource(context.getResources(), R.mipmap.zan_normal);
        bitmapSelect=BitmapFactory.decodeResource(context.getResources(),R.mipmap.zan_select);
    }

    private void initRectF() {
        rectPic=new Rect(0,0,bitmapNormal.getWidth(),bitmapNormal.getHeight());
        rectDraw=new Rect(-bitmapNormal.getWidth()-varySet,-bitmapNormal.getHeight()/2-varySet,0+varySet,bitmapNormal.getHeight()/2+varySet);
        regionThumb=new Region();
        regionThumb.set(rectDraw);
    }


    private void initText() {
        if(text!=null&&text.length()>0){
            listFloat=new float[text.length()];
            for (int i=0;i<text.length();i++){
                listFloat[i]=painText.measureText(text,0,i+1);
            }
        }
    }

    private void initValueAnimator() {
        uplistenner = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                varySet= (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        };
        listener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                type= TYPE.NORMAL;
                varySet=0;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        };
        change = ValueAnimator.ofInt(0,8,0);
        change.setDuration(300);
        change.setInterpolator(new OvershootInterpolator());
        change.setRepeatCount(0);
        change.addUpdateListener(uplistenner);
        change.addListener(listener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                dst[0]=event.getRawX();
                dst[1]=event.getRawY();
                matrix.mapPoints(dst);
                isClick=regionThumb.contains((int) dst[0],(int) dst[1]);
                break;
            case MotionEvent.ACTION_UP:
                dst[0]=event.getRawX();
                dst[1]=event.getRawY();
                matrix.mapPoints(dst);
                if(isClick&&regionThumb.contains((int) dst[0],(int) dst[1])){
                    switch (type){
                        case NORMAL:
                            if(thumb){
                                type=TYPE.UNThumb;
                            }else {
                                type=TYPE.Thumb;
                            }
                            thumb=!thumb;
                            change.start();
                            break;
                        case Thumb:
                            break;
                        case UNThumb:
                            break;
                        default:break;
                    }
                    isClick=false;
                }
                break;
            default:break;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight=h;
        mWidth=w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth/2,mHeight/2);
        matrix.reset();
        if(matrix.isIdentity()){
            canvas.getMatrix().invert(matrix);
        }
        initRectF();
        drawThumb(canvas);
        drawText(canvas);

    }



    private void drawThumb(Canvas canvas) {
        switch (type){
            case NORMAL:
                if(thumb){
                    canvas.drawBitmap(bitmapSelect,rectPic,rectDraw,painText);
                }else {
                    canvas.drawBitmap(bitmapNormal,rectPic,rectDraw,painText);
                }
                break;
            case Thumb:
                canvas.drawBitmap(bitmapSelect,rectPic,rectDraw,painText);
                break;
            case UNThumb:
                canvas.drawBitmap(bitmapNormal,rectPic,rectDraw,painText);
                break;
            default:break;
        }

    }

    private void drawText(Canvas canvas) {
        canvas.drawText(text.toCharArray(),0,text.toCharArray().length-1,textStartX,textStartY,painText);
//        canvas.drawText("1",textStartX+listFloat[listFloat.length-2],
//                textStartY,painText);
    }

    public void addNum(){
        if(text==null)return;
        if(text.length()==0){
            text="1";
            lengthChange=true;
        }

    }

    public void cutNum(){

    }

}
