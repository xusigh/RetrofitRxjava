package baseframes.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import baseframes.base.rxtext.ClickDrawView;
import baseframes.base.rxtext.KongTiaoView;
import baseframes.base.rxtext.PathTestView;
import baseframes.base.viewtest.Bean;
import baseframes.base.viewtest.YaoKongQi;
import baseframes.baselibrary.baseui.baseannotation.LayoutId;
import baseframes.baselibrary.baseui.baseannotation.LeftText;
import baseframes.baselibrary.baseui.baseannotation.RightText;
import baseframes.baselibrary.baseui.baseannotation.TitleText;
import baseframes.baselibrary.baseui.baseannotation.UiActivityAnnitation;
import baseframes.baselibrary.baseui.baseui.UiReinstateActivityLifecycleCallback;
import butterknife.BindView;
import butterknife.OnClick;

@LayoutId(R.layout.activity_test1)
@TitleText("第二页")
@LeftText("返回")
@RightText("你好啊1")
public class TestActivity1 extends AppCompatActivity implements UiActivityAnnitation{
    @BindView(R.id.make)
    Button make;
    @BindView(R.id.next)
    Button next;
//    @BindView(R.id.testView)
//    TestView testView;
/*    @BindView(R.id.scalview)
    ScaleView s;*/
  /*  @BindView(R.id.pictrueView)
PictureView pictureView;*/
  /*  @BindView(R.id.mytextview)
    MyTextView myTextView;*/
    private boolean play=false;
/*    @BindView(R.id.loadingView)
    LoadingView loadingView;*/
    int progress=0;
   /* private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress++;
            if(loadingView!=null) {
                loadingView.setProgress(progress);
                this.sendEmptyMessageDelayed(0, 100);
            }
        }
    };*/
    @BindView(R.id.pathview)
    PathTestView pathview;
    @BindView(R.id.yaokong)
    YaoKongQi yaoKongQi;
    @BindView(R.id.clickview)
    ClickDrawView clickDrawView;
    @BindView(R.id.kongtiao)
    KongTiaoView kongTiaoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @OnClick({R.id.make,R.id.next})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.make:
//                pictureView.setStart(true);
//                System.out.println(1/0);
//                pathview.startAnimate2();
//                pathview.startSearch();
                pathview.startAnimate4();
                break;
            case R.id.next:
                startActivity(new Intent(this,TestActivity2.class));
                break;
        }
    }

    @Override
    public void initView(UiReinstateActivityLifecycleCallback callback) {
        callback.tvCenter.setText("哈哈哈 我改变了");
        List<Bean> list=new ArrayList<>();
        for (int i = 1; i <=7 ; i++) {
            Bean b=new Bean();
            b.setName("name"+i);
            b.setValue(i*10);
            list.add(b);
        }
//        testView.setData(list);
//        handler.sendEmptyMessageDelayed(0,1000);
//        pathview.setData(new DataWang(20f,30f,40f,60f,10f,17.4f));
    }

    @Override
    public void initEvent(UiReinstateActivityLifecycleCallback callback) {

    }

}
