package baseframes.base.rxtext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhanghs on 2017/11/30/030.
 */

//简单的自定义的进度条画法，我叫它空调view - -
public class KongTiaoView extends View {
    //画布的大小
    private int mHeight,mWidth;
    //矩阵
    private Matrix matrix;
    //三个圆弧半径
    private float bigRadio=150f,normalRadio=130f,lineRadio=140;
    //四种绘制路径
    private Path pathLeft,pathRight,pathCircle,pathLine;
    //一些记录点的变量
    private float [] circleXY=new float[2],touchXY= new float[2],curentXY=new float[2],startXY=new float[2],endXY=new float[2];
    //画笔
    private Paint paintCircle,paintLeft,paintRight;
    //画出来的区域以及截取的区域
    private Region regionCircle,region;
    //当前圆心坐标距离最左边的距离
    private float whereLen=-1f;
    //标记的总横坐标长度
    private float xLength=0f;
    //小圆是否被点击
    private boolean isClick=false;
    //测量路径的方法
    private PathMeasure measure;

    public KongTiaoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        matrix=new Matrix();
        initPaint();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchXY[0]=event.getRawX();
                touchXY[1]=event.getRawY();
                matrix.mapPoints(touchXY);
                if(regionCircle.contains((int) touchXY[0],(int) touchXY[1])){
                    isClick=true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                touchXY[0]=event.getRawX();
                touchXY[1]=event.getRawY();
                matrix.mapPoints(touchXY);
                if(isClick){
                    curentXY=touchXY;
                    synchronized (this) {
                        if (curentXY[0] > endXY[0]) {
                            whereLen = endXY[0] - startXY[0];
                        } else if (curentXY[0] >=  startXY[0] && curentXY[0] <= endXY[0]) {
                            whereLen = curentXY[0] -  startXY[0];
                        } else {
                            whereLen = 0;
                        }
                    }

                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isClick=false;
                break;
            default:break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth/2,mHeight/2);
        matrix.reset();
        if(matrix.isIdentity()){
            canvas.getMatrix().invert(matrix);
        }
        initPath(region);
        drawLeft(canvas);
        drawRight(canvas);
        drawCircle(canvas);

    }
    //画圆
    private void drawCircle(Canvas canvas) {
        canvas.drawPath(pathCircle,paintCircle);
    }
    //画右边的圆弧
    private void drawRight(Canvas canvas) {
       canvas.drawPath(pathRight,paintRight);
    }
    //画左边的
    private void drawLeft(Canvas canvas) {
        canvas.drawPath(pathLeft,paintLeft);
    }
    //初始化画笔
    private void initPaint() {

        paintCircle=new Paint();
        paintCircle.setStyle(Paint.Style.FILL);
        paintCircle.setAntiAlias(true);
        paintCircle.setColor(Color.RED);
        paintCircle.setStrokeWidth(2);

        paintLeft=new Paint();
        paintLeft.setColor(Color.YELLOW);
        paintLeft.setAntiAlias(true);
        paintLeft.setStyle(Paint.Style.FILL);
        paintLeft.setStrokeWidth(2);

        paintRight=new Paint();
        paintRight.setColor(Color.BLUE);
        paintRight.setStyle(Paint.Style.FILL);
        paintRight.setAntiAlias(true);
        paintRight.setStrokeWidth(2);

    }
    //初始化路径
    private void initPath(Region region) {

        pathLine=new Path();
        pathLine.addArc(new RectF(-lineRadio,-lineRadio,lineRadio,lineRadio),225,90);
        measure = new PathMeasure(pathLine,false);
        startXY[0]= (float) (-Math.sqrt(2)/2*140);
        startXY[1]= startXY[0];
        endXY[0]= -startXY[0];
        endXY[1]=startXY[0];
        xLength=  endXY[0]-startXY[0];

        System.out.println("---------------------------"+whereLen);

        if(whereLen!=-1){
            measure.getPosTan(whereLen/xLength* measure.getLength(),circleXY,null);
        }else {
            //circleXY=startXY这里为什么不这样？！骚年你去试试
            circleXY=new float[]{startXY[0],startXY[1]};
        }

        pathCircle=new Path();
        pathCircle.addCircle(circleXY[0],circleXY[1],10, Path.Direction.CW);
        regionCircle=new Region();
        regionCircle.setPath(pathCircle,region);


        pathLeft=new Path();
        double dege_x;//扫描的边与x正轴的夹角
        double dege_y;//扫描边与y负轴的夹角
        double sweetRange;//与左边边界的角度
        if(circleXY[0]>0){//圆已经走到右边
            dege_x=Math.toDegrees(Math.atan(Math.abs(circleXY[1]/circleXY[0])));
            dege_y=Math.toDegrees(Math.atan(Math.abs(circleXY[0]/circleXY[1])));
            sweetRange=dege_y+45;
        }else if (circleXY[0]<0){//圆在左边
            dege_y=Math.toDegrees(Math.atan(Math.abs(circleXY[0]/circleXY[1])));
            dege_x=dege_y+90;
            sweetRange=45-dege_y;
        }else {//圆在0
            dege_x=90;
            dege_y=0;
            sweetRange=45;
        }

        pathLeft.addArc(new RectF(-bigRadio,-bigRadio,bigRadio,bigRadio),(float)-dege_x,-(float)sweetRange);
        pathLeft.arcTo(new RectF(-normalRadio,-normalRadio,normalRadio,normalRadio),225,(float)sweetRange);
        pathLeft.close();

        pathRight=new Path();
        pathRight.addArc(new RectF(-bigRadio,-bigRadio,bigRadio,bigRadio),-45, (float) (sweetRange-90));
        pathRight.arcTo(new RectF(-normalRadio,-normalRadio,normalRadio,normalRadio),(float)-dege_x, (float) (90-sweetRange));
        pathRight.close();


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight=h;
        mWidth=w;
        //区域的截取大小就为画布的大小
        region=new Region(-w,-h,w,h);

    }
}
