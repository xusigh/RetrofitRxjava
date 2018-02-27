package baseframes.base;

import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import baseframes.base.adapter.Bean_Adapter;
import baseframes.base.adapter.RvTestAdapter1;
import baseframes.base.adapter.RvTestAdapter2;
import baseframes.baselibrary.baseui.baseannotation.LayoutId;
import baseframes.baselibrary.baseui.baseannotation.LeftText;
import baseframes.baselibrary.baseui.baseannotation.RightText;
import baseframes.baselibrary.baseui.baseannotation.TitleText;
import baseframes.baselibrary.baseui.baseannotation.UiActivityAnnitation;
import baseframes.baselibrary.baseui.baseui.UiReinstateActivityLifecycleCallback;
import butterknife.BindView;

/**
 * Created by zhanghs on 2018/1/15/015.
 */
@LayoutId(R.layout.activity_test4)
@TitleText("第5页")
@LeftText("返回")
@RightText("你好啊")
public class TestActivity4 extends AppCompatActivity implements UiActivityAnnitation {
    @BindView(R.id.rv_horizontal)
    RecyclerView recyclerView_horizontal;
    @BindView(R.id.rv_vertical)
    RecyclerView recyclerView_vertical;
    @BindView(R.id.bt_dian)
    Button bt_dian;
    @BindView(R.id.rl_content)
    RelativeLayout rl_content;
    private RvTestAdapter1 adapter1;
    private RvTestAdapter2 adapter2;
    private List<Bean_Adapter> list;
    private boolean isHorizontal=true;
    private String [] urlImage={
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1515993549991&di=d9881ee05f6cc86b5ce1e325179a9c0e&imgtype=0&src=http%3A%2F%2F58pic.ooopic.com%2F58pic%2F15%2F54%2F07%2F84H58PIC7Mi.png",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1515993550340&di=fcb47c1241eb44d36ee335945a337057&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F15%2F68%2F42%2F10Y58PICdet_1024.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1515993550340&di=c65d534590020a7d139e5a714fe7f827&imgtype=0&src=http%3A%2F%2Fimg.taopic.com%2Fuploads%2Fallimg%2F130225%2F234609-13022502110138.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1515993550339&di=9ff78967e61dbf18ad06331c169ae751&imgtype=0&src=http%3A%2F%2Fpic18.nipic.com%2F20111216%2F9077814_121041715159_2.png",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1515993550339&di=589fea238e0bb86aa858c3537a6e77a0&imgtype=0&src=http%3A%2F%2Fpic13.nipic.com%2F20110426%2F7012137_171213515153_2.png",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1515993550339&di=870787d627431062567300622353c793&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F15%2F54%2F06%2F30J58PICqMA_1024.png"
    };
    private int[] start=new int[2],end=new int[2];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(UiReinstateActivityLifecycleCallback callback) {
        recyclerView_horizontal.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView_vertical.setLayoutManager(new LinearLayoutManager(this));
        adapter1=new RvTestAdapter1(this);
        adapter2=new RvTestAdapter2(this);
        recyclerView_horizontal.setAdapter(adapter1);
        recyclerView_vertical.setAdapter(adapter2);
        list=new ArrayList<>();
        downData();
        bt_dian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < list.size(); i++) {
                    if(isHorizontal){
                        showAnimator(recyclerView_horizontal.getChildAt(i).findViewById(R.id.iv_show),recyclerView_vertical.getChildAt(i).findViewById(R.id.iv_show));
                    }else {
                        showAnimator(recyclerView_vertical.getChildAt(i).findViewById(R.id.iv_show),recyclerView_horizontal.getChildAt(i).findViewById(R.id.iv_show));
                    }
                }
                isHorizontal=!isHorizontal;
            }

        });
    }

    private void downData() {
        for (int i = 0; i <urlImage.length ; i++) {
            Bean_Adapter b=new Bean_Adapter("title"+i,urlImage[i],"time"+i);
            list.add(b);
        }
        adapter1.addAll(list);
        adapter2.addAll(list);
    }

    @Override
    public void initEvent(UiReinstateActivityLifecycleCallback callback) {

    }


    private void showAnimator(View view_start,View view_end){
        View temp=view_start;
        rl_content.addView(temp);
        view_start.getLocationInWindow(start);
        view_end.getLocationInWindow(end);
        Path p=new Path();
        p.moveTo(start[0],start[1]);
        p.lineTo(end[0],end[1]);
        float[] mcurentFloat=new float[2];
        PathMeasure pathmeasure=new PathMeasure(p,false);
        ValueAnimator value=ValueAnimator.ofFloat(pathmeasure.getLength());
        value.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float num= (float) valueAnimator.getAnimatedValue();
                pathmeasure.getPosTan(num,mcurentFloat,null);
                temp.setTranslationX(mcurentFloat[0]);
                temp.setTranslationY(mcurentFloat[1]);
            }
        });
        value.setDuration(2000);
        value.setRepeatCount(0);
        value.start();

    }
}
