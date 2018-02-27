package baseframes.base;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import baseframes.base.rxtext.TestBean;
import baseframes.baselibrary.baseadapter.BaseRecyclerAdapter;
import baseframes.baselibrary.baseadapter.BaseViewHolder2;

/**
 * Created by zhanghs on 2017/11/22/022.
 */

public class TestAdapter extends BaseRecyclerAdapter<TestBean> {
    protected TestAdapter(Context context) {
        super(context,true);
    }
    @Override
    protected int getLayout(int viewType) {
        switch (viewType){
            case 0:
                return R.layout.item1_layout;
            case 1:
                return R.layout.item2_layout;
            default:return -10;
        }
    }

    @Override
    public void onBind(BaseViewHolder2 holder, int position) {
        switch (getType(position)){
            case 0:
                TextView Item1tv1=holder.get(R.id.tv_1);
                if(get(position).getName()!=null){
                    Item1tv1.setText(get(position).getName());
                }
                TextView Item1tv2=holder.get(R.id.tv_2);
                if(get(position).getContent()!=null){
                    Item1tv2.setText(get(position).getContent());
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("点击type1");
                    }
                });
                break;
            case 1:
                TextView Item2tv1=holder.get(R.id.tv_1);
                if(get(position).getName()!=null){
                    Item2tv1.setText(get(position).getName());
                }
                TextView Item2tv2=holder.get(R.id.tv_2);
                if(get(position).getContent()!=null){
                    Item2tv2.setText(get(position).getContent());
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("点击type2");
                    }
                });
                break;
            default:break;
        }
    }
    @Override
    protected int getType(int position) {
        return get(position).getType();
    }

}
