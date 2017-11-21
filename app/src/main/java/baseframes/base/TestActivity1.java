package baseframes.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @OnClick({R.id.make,R.id.next})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.make:
                System.out.println(1/0);
                break;
            case R.id.next:
                startActivity(new Intent(this,TestActivity2.class));
                break;
        }
    }

    @Override
    public void initView(UiReinstateActivityLifecycleCallback callback) {
        callback.tvCenter.setText("哈哈哈 我改变了");
    }

    @Override
    public void initEvent(UiReinstateActivityLifecycleCallback callback) {

    }

}
