package baseframes.base.rxtext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import baseframes.base.R;
import baseframes.base.viewtest.StartType;
import baseframes.base.viewtest.Yezi;

/**
 * Created by zhanghs on 2017/11/24/024.
 */

public class LoadingView extends View {
    private int mHeight,mWidth;//画布的宽高
    private float drawWidth,drawHeight;//控件真正展示的宽高
    private Paint paint;
    private int progress=0;
    private float arcR=0f;


    // 不同类型之间的振幅差距
    private static final int AMPLITUDE_DISPARITY = 5;
    // 叶子飘动一个周期所花的时间
    private static final long LEAF_FLOAT_TIME = 2000;
    // 叶子旋转一周需要的时间

    // 叶子旋转一周需要的时间
    private static final long LEAF_ROTATE_TIME = 2000;
    // 中等振幅大小
    private static final int MIDDLE_AMPLITUDE = 18;
    // 中等振幅大小
    private int mMiddleAmplitude = MIDDLE_AMPLITUDE;
    // 振幅差
    private int mAmplitudeDisparity = AMPLITUDE_DISPARITY;
    private List<Yezi> mLeafInfos;
    // 叶子飘动一个周期所花的时间
    private long mLeafFloatTime = LEAF_FLOAT_TIME;
    // 叶子旋转一周需要的时间
    private long mLeafRotateTime = LEAF_ROTATE_TIME;

    private Paint mBitmapPaint;
    private Bitmap mLeafBitmap;
    private int mLeftMargin, mRightMargin;
    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mLeafInfos= new YeziFactory().generateLeafs();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setFilterBitmap(true);
        mLeafBitmap= BitmapFactory.decodeResource(context.getResources(),R.mipmap.yezi);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight=h;
        drawWidth=mHeight*0.8f;
        mWidth=w;
        drawHeight=drawWidth*0.2f;
        arcR=drawHeight/2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        //将画布坐标轴归零
        canvas.translate(mWidth/2,mHeight/2);
        if(progress!=0) {
            drawArc(canvas);
            drawRect(canvas);
            drawLeafs(canvas);
        }
    }

    private void initPaint() {
        paint=new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#ea7e13"));
        paint.setAntiAlias(true);
    }

    //10以内，画圆弧
    private void drawArc(Canvas canvas){
        if(progress>0&&progress<=10) {
            RectF rectf = new RectF(-drawWidth / 2, -arcR, -3 * arcR, arcR);
            int startAngle = 180 - 9 * progress;
            int sweetAngle = 18 * progress;
            canvas.drawArc(rectf, startAngle, sweetAngle, false, paint);
        }else {
            RectF rectf = new RectF(-drawWidth / 2, -arcR, -3 * arcR, arcR);
            canvas.drawArc(rectf, 90, 180, false, paint);
        }
    }
    //10以外，画矩形
    private void drawRect(Canvas canvas){
        if(progress>=10) {
            float left = -4 * arcR + drawWidth / 100 * (progress - 10);
            RectF rectf = new RectF(-4 * arcR, -arcR, left, arcR);
            canvas.drawRect(rectf, paint);
            System.out.println("===================================        "+progress);
        }
    }
    public void setProgress(int progress){
        this.progress=progress;
        if(progress>100){
            return;
        }else {
            invalidate();
        }
    }


    private void getLeafLocation(Yezi leaf, long currentTime) {
        long intervalTime = currentTime - leaf.startTime;
        mLeafFloatTime = mLeafFloatTime <= 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
        if (intervalTime < 0) {
            return;
        } else if (intervalTime > mLeafFloatTime) {
            leaf.startTime = System.currentTimeMillis()
                    + new Random().nextInt((int) mLeafFloatTime);
        }

        float fraction = (float) intervalTime / mLeafFloatTime;
        leaf.x = (int) (drawWidth - drawWidth * fraction)/2;
        leaf.y = getLocationY(leaf);
    }

    private void getLeafLocation2(Yezi leaf, long currentTime) {
        long intervalTime = currentTime - leaf.startTime;
        System.out.println("--------------------"+intervalTime);
        mLeafFloatTime = mLeafFloatTime <= 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
        if (intervalTime < 0) {
            return;
        } else if (intervalTime > mLeafFloatTime) {
            leaf.startTime = System.currentTimeMillis()
                    + new Random().nextInt((int) mLeafFloatTime);
        }
        leaf.x = (int)drawWidth/mLeafFloatTime*(1-intervalTime);
        leaf.y = getLocationY(leaf);
    }

    private int getLocationY(Yezi leaf) {
        // y = A(wx+Q)+h
        float w = (float) ((float) 2 * Math.PI / drawWidth);
        float a = mMiddleAmplitude;
        switch (leaf.type) {
            case LITTLE:
                // 小振幅 ＝ 中等振幅 － 振幅差
                a = mMiddleAmplitude - mAmplitudeDisparity;
                break;
            case MIDDLE:
                a = mMiddleAmplitude;
                break;
            case BIG:
                // 小振幅 ＝ 中等振幅 + 振幅差
                a = mMiddleAmplitude + mAmplitudeDisparity;
                break;
            default:
                break;
        }

        return (int) ((int) (a * Math.sin(w * leaf.x)) -arcR/2);
    }

    private void drawLeafs(Canvas canvas) {
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < mLeafInfos.size(); i++) {
            Yezi leaf = mLeafInfos.get(i);
            if (currentTime > leaf.startTime && leaf.startTime != 0) {
                // 绘制叶子－－根据叶子的类型和当前时间得出叶子的（x，y）
                getLeafLocation(leaf, currentTime);
                // 根据时间计算旋转角度
                canvas.save();
                // 通过Matrix控制叶子旋转
                Matrix matrix = new Matrix();
                float transX = mLeftMargin + leaf.x;
                float transY = mLeftMargin + leaf.y;
                matrix.postTranslate(transX, transY);
                // 通过时间关联旋转角度，则可以直接通过修改LEAF_ROTATE_TIME调节叶子旋转快慢
                float rotateFraction = ((currentTime - leaf.startTime) % LEAF_ROTATE_TIME)
                        / (float) LEAF_ROTATE_TIME;
                int angle = (int) (rotateFraction * 360);
                // 根据叶子旋转方向确定叶子旋转角度
                int rotate = leaf.rotateDirection == 0 ? angle + leaf.rotateAngle : -angle
                        + leaf.rotateAngle;
                matrix.postRotate(rotate, transX
                        + 10 / 2, transY + 10 / 2);
                canvas.drawBitmap(mLeafBitmap, matrix, mBitmapPaint);
                canvas.restore();
            } else {
                continue;
            }
        }
    }

    public class YeziFactory {
        private static final int MAX_NUM=6;
        // 叶子飘动一个周期所花的时间
        private static final long LEAF_FLOAT_TIME = 2000;
        private int mAddTime;
        Random random=new Random();
        public Yezi getYezi(){
            Yezi yezi=new Yezi();
            int randomType=random.nextInt(3);
            StartType type=StartType.MIDDLE;
            switch (randomType){
                case 0:
                    break;
                case 1:
                    type=StartType.LITTLE;
                    break;
                case 2:
                    type= StartType.BIG;
                    break;
                default:break;
            }
            yezi.type=type;
            yezi.rotateAngle=random.nextInt(360);
            yezi.rotateDirection=random.nextInt(2);
            mAddTime += random.nextInt((int) (LEAF_FLOAT_TIME * 1.5));
            yezi.startTime = System.currentTimeMillis() + mAddTime;
            return yezi;
        }
        // 根据最大叶子数产生叶子信息
        public List<Yezi> generateLeafs() {
            return generateLeafs(MAX_NUM);
        }

        // 根据传入的叶子数量产生叶子信息
        public List<Yezi> generateLeafs(int leafSize) {
            List<Yezi> leafs = new LinkedList<Yezi>();
            for (int i = 0; i < leafSize; i++) {
                leafs.add(getYezi());
            }
            return leafs;
        }
    }

}
