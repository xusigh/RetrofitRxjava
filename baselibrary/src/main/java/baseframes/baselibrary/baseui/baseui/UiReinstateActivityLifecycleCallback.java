package baseframes.baselibrary.baseui.baseui;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import baseframes.baselibrary.R;
import baseframes.baselibrary.baseui.baseannotation.CenterImag;
import baseframes.baselibrary.baseui.baseannotation.LayoutId;
import baseframes.baselibrary.baseui.baseannotation.LeftImag;
import baseframes.baselibrary.baseui.baseannotation.LeftText;
import baseframes.baselibrary.baseui.baseannotation.RightImag;
import baseframes.baselibrary.baseui.baseannotation.RightText;
import baseframes.baselibrary.baseui.baseannotation.TitleText;
import baseframes.baselibrary.baseui.baseannotation.UiActivityAnnitation;
import baseframes.baselibrary.crashlib.rulers.Reinstate;
import baseframes.baselibrary.crashlib.rulers.ReinstateActivity;
import baseframes.baselibrary.crashlib.rulers.ReinstateStore;
import baseframes.baselibrary.crashlib.utils.Reflect;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhanghs on 2017/11/21/021.
 * crash 的初始化以及对activity的butternife的初始化
 * 这里需要activity 实现一个接口UiActivityAnnitation，就会自动为实现接口的activity增加toolbar
 * 或者使用baseactivity，
 */

public class UiReinstateActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {
    public TextView tvCenter, tvLeft, tvRight;
    public ImageView imgCenter, imgLeft, imgRight;
    public RelativeLayout rlLeft, rlRight;
    public LinearLayout llCenter;
    private Unbinder unbinder;

    @Override
    public void onActivityCreated(final Activity activity, Bundle bundle) {
        Class mClass = activity.getClass();
        if (activity instanceof UiActivityAnnitation) {
            LayoutId layoutId = (LayoutId) mClass.getAnnotation(LayoutId.class);
            if (layoutId != null && layoutId.value() != 0) {
//                activity.getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
                activity.setContentView(layoutId.value());
            }
            unbinder = ButterKnife.bind(activity);
            if (activity.findViewById(R.id.toolbar) != null) { //找到 Toolbar 并且替换 Actionbar
                if (activity instanceof AppCompatActivity) {
                    ((AppCompatActivity) activity).setSupportActionBar((Toolbar) activity.findViewById(R.id.toolbar));
                    ((AppCompatActivity) activity).getSupportActionBar().setDisplayShowTitleEnabled(false);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        activity.setActionBar((android.widget.Toolbar) activity.findViewById(R.id.toolbar));
                        activity.getActionBar().setDisplayShowTitleEnabled(false);
                    }
                }
                tvCenter = activity.findViewById(R.id.base_center_tv);
                tvLeft = activity.findViewById(R.id.base_left_tv);
                tvRight = activity.findViewById(R.id.base_right_tv);
                imgLeft = activity.findViewById(R.id.base_left_img);
                imgCenter = activity.findViewById(R.id.base_center_img);
                imgRight = activity.findViewById(R.id.base_right_img);
                rlLeft = activity.findViewById(R.id.base_left_rl_content);
                rlRight = activity.findViewById(R.id.base_right_rl_content);
                llCenter = activity.findViewById(R.id.base_center_ll_content);
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
                        activity.onBackPressed();
                    }
                });
            }
            //将这个callback回掉回去，既可以满足每个activity的初始化，又可以对toolbar的控件进行相关操作
            ((UiActivityAnnitation) activity).initView(this);
            ((UiActivityAnnitation) activity).initEvent(this);
        }
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        //判断是否是应该跳过的activity或者是crash的显示activity
        boolean isLegal = ReinstateStore.getInstance().verifyActivity(activity);
        if (!isLegal)
            return;

        if (activity.getIntent().getBooleanExtra(ReinstateActivity.RECOVERY_MODE_ACTIVE, false)) {
            Reflect.on(Reinstate.class).method("registerRecoveryProxy").invoke(Reinstate.getInstance());
        }

        if (ReinstateStore.getInstance().contains(activity))
            return;

        Window window = activity.getWindow();
        if (window != null) {
            View decorView = window.getDecorView();
            if (decorView == null)
                return;
            decorView.post(new Runnable() {
                @Override
                public void run() {
                    ReinstateStore.getInstance().putActivity(activity);
                    Object o = activity.getIntent().clone();
                    ReinstateStore.getInstance().setIntent((Intent) o);
                }
            });
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ReinstateStore.getInstance().removeActivity(activity);
        if(unbinder!=null){
            unbinder.unbind();
            unbinder=null;
        }

    }
}
