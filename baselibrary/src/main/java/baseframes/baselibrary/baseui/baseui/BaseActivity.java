package baseframes.baselibrary.baseui.baseui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import baseframes.baselibrary.R;
import baseframes.baselibrary.api.HttpManager;
import baseframes.baselibrary.basebean.BaseEventBean;
import baseframes.baselibrary.baseinterface.BaseEventEnter;
import baseframes.baselibrary.baseui.baseannotation.CenterImag;
import baseframes.baselibrary.baseui.baseannotation.LayoutId;
import baseframes.baselibrary.baseui.baseannotation.LeftImag;
import baseframes.baselibrary.baseui.baseannotation.LeftText;
import baseframes.baselibrary.baseui.baseannotation.RightImag;
import baseframes.baselibrary.baseui.baseannotation.RightText;
import baseframes.baselibrary.baseui.baseannotation.TitleText;
import baseframes.baselibrary.baseui.basecontroler.BaseHandler;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhanghs on 2017/11/21/021.
 */

public abstract class BaseActivity<T> extends AppCompatActivity implements View.OnClickListener, BaseEventEnter {
    private TextView tvCenter, tvLeft, tvRight;
    private ImageView imgCenter, imgLeft, imgRight;
    private RelativeLayout rlLeft, rlRight;
    private LinearLayout llCenter;
    private Toolbar toolbar;
    private Unbinder unbinder;
    protected static BaseHandler baseHandler ;
    protected Context bContext;
    protected LayoutInflater bInflater;
    protected View bContentView;
    protected HttpManager bApi;
    protected RequestManager bGlide;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseHandler=new MyHandler(this) {
            @Override
            public void handleMessage(Message msg, int what) {
                handleMessageThis(msg,what);
            }
        };
        bContext=this;
        bInflater=LayoutInflater.from(this);
        bApi=HttpManager.init(this);
        bGlide=Glide.with(this);
        LayoutId layoutId = getClass().getAnnotation(LayoutId.class);
        if (layoutId != null && layoutId.value() != 0) {
            setContentView(layoutId.value());
        } else {
            setContentView(contentLayoutId());
        }
    }
    public static abstract class MyHandler extends BaseHandler{
        public MyHandler(Activity activity) {
            super(activity);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEvent();
        if(unbinder!=null){
            unbinder.unbind();
            unbinder=null;
        }
    }

    protected   void handleMessageThis(Message msg, int what){};
    public void setContentView(@LayoutRes int layoutResID) {
        if(layoutResID==0)return;
        bContentView=bInflater.inflate(layoutResID,null);
        if(bContentView==null)return;
        super.setContentView(bContentView);
        registerEvent();
        //这里保存unbinder 千万别用Serializable的序列化
        unbinder=ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            tvCenter = (TextView) findViewById(R.id.base_center_tv);
            tvLeft = (TextView) findViewById(R.id.base_left_tv);
            tvRight = (TextView) findViewById(R.id.base_right_tv);
            imgLeft = (ImageView) findViewById(R.id.base_left_img);
            imgCenter = (ImageView) findViewById(R.id.base_center_img);
            imgRight = (ImageView) findViewById(R.id.base_right_img);
            rlLeft = (RelativeLayout) findViewById(R.id.base_left_rl_content);
            rlRight = (RelativeLayout) findViewById(R.id.base_right_rl_content);
            llCenter = (LinearLayout) findViewById(R.id.base_center_ll_content);

            Class mClass = getClass();
            LeftImag leftImag = (LeftImag) mClass.getAnnotation(LeftImag.class);
            LeftText leftText = (LeftText) mClass.getAnnotation(LeftText.class);
            TitleText titleText = (TitleText) mClass.getAnnotation(TitleText.class);
            CenterImag centerImag = (CenterImag) mClass.getAnnotation(CenterImag.class);
            RightImag rightImag = (RightImag) mClass.getAnnotation(RightImag.class);
            RightText rightText = (RightText) mClass.getAnnotation(RightText.class);
            if (titleText != null && titleText.value().length() != 0) {
                tvCenter.setVisibility(View.VISIBLE);
                tvCenter.setText(titleText.value());
            }
            if (leftText != null && leftText.value().length() != 0) {
                tvLeft.setVisibility(View.VISIBLE);
                tvLeft.setText(leftText.value());
            }
            if (rightText != null && rightText.value().length() != 0) {
                tvRight.setVisibility(View.VISIBLE);
                tvRight.setText(rightText.value());
            }
            if (leftImag != null && leftImag.value() != 0) {
                imgLeft.setVisibility(View.VISIBLE);
                imgLeft.setImageResource(leftImag.value());
            }
            if (rightImag != null && rightImag.value() != 0) {
                imgRight.setVisibility(View.VISIBLE);
                imgRight.setImageResource(rightImag.value());
            }
            if (centerImag != null && centerImag.value() != 0) {
                imgCenter.setVisibility(View.VISIBLE);
                imgCenter.setImageResource(centerImag.value());
            }
            rlLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }



    @Override
    public void registerEvent() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unregisterEvent() {
        EventBus.getDefault().unregister(this);
    }

    public abstract void initView();

    public abstract void initEvent();

    protected  int contentLayoutId(){return 0;}

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataSynEvent(BaseEventBean<T> event) {
        onDataEvent(event);
    }

    protected void onDataEvent(BaseEventBean<T> event){

    }
    /**
     * 中间的点击事件
     *
     * @param onClickListener
     */
    public void setCenterClick(View.OnClickListener onClickListener) {
        llCenter.setOnClickListener(onClickListener);
    }

    /**
     * 左边的点击事件
     *
     * @param onClickListener
     */
    public void setLeftClick(View.OnClickListener onClickListener) {
        rlLeft.setOnClickListener(onClickListener);
    }

    /**
     * 右边的点击事件
     *
     * @param onClickListener
     */
    public void setRightClick(View.OnClickListener onClickListener) {
        rlRight.setOnClickListener(onClickListener);
    }

    /**
     * 左边的文字
     *
     * @param text
     */
    public void setTvLeftText(String text) {
        tvLeft.setText(text);
    }

    /**
     * 中间的文字
     *
     * @param text
     */
    public void setTvCenterText(String text) {
        tvCenter.setText(text);
    }

    /**
     * 右边的文字
     *
     * @param text
     */
    public void setTvRightText(String text) {
        tvRight.setText(text);
    }



}
