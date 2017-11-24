package baseframes.base.viewtest;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by zhanghs on 2017/11/23/023.
 */

public class TestView extends View {
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    private Paint paint;
    private RectF rectf;
    private List<Bean> list;
    private int mWidth,mHeight;
    private int startRen=0;
    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        paint.setStyle(Paint.Style.FILL);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mHeight / 2);                // 将画布坐标原点移动到中心位置
        float r = (float) (Math.min(mWidth, mHeight) / 2 * 0.8);  // 饼状图半径
        rectf = new RectF(-r, -r, r, r);
        if(list==null) return;
        for (int i = 0; i < list.size(); i++) {
            paint.setColor(list.get(i).getColor());
            System.out.println("-------------------   getangle"+list.get(i).getAngle());
            canvas.drawArc(rectf,startRen,list.get(i).getAngle(),true,paint);
            startRen+=list.get(i).getAngle();
        }

    }

    private void initData() {
        if(list!=null){
            float valueCount=0;
            for (int i = 0; i <list.size() ; i++) {
                valueCount+=list.get(i).getValue();
                list.get(i).setColor(mColors[i]);
            }
            float sumAgle=0;
            float pagecount=0;
            for (int j = 0; j < list.size(); j++) {
                float precentage=list.get(j).getValue()/valueCount;
                pagecount+=precentage;
                list.get(j).setPercentage(precentage);
                list.get(j).setAngle(precentage*360);
                sumAgle+=list.get(j).getAngle();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=h;
    }
    public void setData(List<Bean> list){
        System.out.println("                    set                 ");
        this.list=list;
        initData();
        invalidate();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

}
