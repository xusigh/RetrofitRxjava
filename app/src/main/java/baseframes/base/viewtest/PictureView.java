package baseframes.base.viewtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import baseframes.base.R;

/**
 * Created by zhanghs on 2017/11/24/024.
 */

public class PictureView  extends View{
    private Picture picture=new Picture();
    private Paint paint;
    private Context context;
    private int curent=0;
    private final Handler handler;
    private boolean start;

    public PictureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
//        recoding();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(curent<10){
                    this.sendEmptyMessageDelayed(0,100);
                    curent++;
                    invalidate();
                }
            }
        };
    }
    public void recoding(){
        Canvas canvas=picture.beginRecording(500,500);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(200,200,100, paint);
        picture.endRecording();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        1这种方法在比较低版本的系统上绘制后可能会影响Canvas状态,一般不用
//        picture.draw(canvas);
        //2
//        canvas.drawPicture(picture,new RectF(100,100,300,300));
//        3
//        PictureDrawable drawable=new PictureDrawable(picture);
//        drawable.setBounds(100,100,300,300);
//        drawable.draw(canvas);
        if(start) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.dui);
            //前面是图片自身大小截取的矩阵，后面是在画布上显示的矩阵
            canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth() / 10 * (curent + 1), bitmap.getHeight()), new Rect(0, 0, bitmap.getWidth() / 10 * (curent + 1), bitmap.getHeight()), null);
            handler.sendEmptyMessageDelayed(0, 100);
        }
    }
    public void setStart(boolean start){
        this.start = start;
        if(this.start){
            curent=0;
            invalidate();
        }
    }
}
