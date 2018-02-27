package baseframes.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Scene;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionInflater;
import com.transitionseverywhere.TransitionManager;

import baseframes.baselibrary.baseui.baseannotation.LayoutId;
import baseframes.baselibrary.baseui.baseannotation.LeftText;
import baseframes.baselibrary.baseui.baseannotation.RightText;
import baseframes.baselibrary.baseui.baseannotation.TitleText;
import baseframes.baselibrary.baseui.baseannotation.UiActivityAnnitation;
import baseframes.baselibrary.baseui.baseui.UiReinstateActivityLifecycleCallback;
import butterknife.BindView;

/**
 * Created by zhanghs on 2017/12/27/027.
 */
@LayoutId(R.layout.activity_test3)
@TitleText("第4页")
@LeftText("返回")
@RightText("你好啊2")
public class TestActivity3 extends AppCompatActivity implements UiActivityAnnitation {
    private Scene scene2;
    private Scene scene1;
    @BindView(R.id.rootView)
    FrameLayout rootView;
    @BindView(R.id.tv_click)
    TextView textView;
    @BindView(R.id.bt_next)
    Button bt_next;
    private boolean isClick=false;
    private Transition transition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transition= TransitionInflater.from(TestActivity3.this).inflateTransition(R.transition.tansition1);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TestActivity3.this,TestActivity4.class));
            }
        });
    }

    @Override
    public void initView(UiReinstateActivityLifecycleCallback callback) {
        scene1=Scene.getSceneForLayout(rootView,R.layout.test_scen1,this);
        scene2 = Scene.getSceneForLayout(rootView,R.layout.test_scen2,this);
        TransitionManager.go(scene1);
      /*  slide = new Slide();
        slide.excludeTarget(iv_1,true);

        slide.excludeTarget(iv_2,true);
        slide.excludeTarget(iv_3,true);
        slide.excludeTarget(iv_4,true);*/

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isClick){
                    goTransition(view);
                }else {
                    goTransition2(view);
                }
                isClick=!isClick;
            }
        });
    }

    @Override
    public void initEvent(UiReinstateActivityLifecycleCallback callback) {

    }
    public void goTransition(View v){
//        TransitionManager.go(scene2,transition);

        TransitionManager.go(scene2,new ChangeBounds());

    }
    public void goTransition2(View v){
        TransitionManager.go(scene1,new ChangeBounds());
    }

}
