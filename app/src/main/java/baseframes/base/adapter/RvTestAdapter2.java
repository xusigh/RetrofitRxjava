package baseframes.base.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import baseframes.base.R;
import baseframes.baselibrary.baseadapter.BaseRecyclerAdapter;
import baseframes.baselibrary.baseadapter.BaseViewHolder2;

/**
 * Created by zhanghs on 2018/1/15/015.
 */

public class RvTestAdapter2 extends BaseRecyclerAdapter<Bean_Adapter> {
    private ImageView iv_show;
    private TextView tv_title,tv_time;

    public RvTestAdapter2(Context context) {
        super(context, false);
    }

    @Override
    protected int getLayout(int viewType) {
        return R.layout.rv_item_vertical;
    }

    @Override
    public void onBind(BaseViewHolder2 holder, int position) {
        iv_show=holder.get(R.id.iv_show);
        tv_title=holder.get(R.id.tv_title);
        tv_time=holder.get(R.id.tv_time);
        bGlide.load(get(position).getImgUrl()).into(iv_show);
        tv_time.setText(get(position).getTime());
        tv_title.setText(get(position).getTitle());
    }

    @Override
    protected int getType(int position) {
        return 0;
    }
}
