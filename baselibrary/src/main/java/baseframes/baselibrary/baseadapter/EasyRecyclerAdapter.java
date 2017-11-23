package baseframes.baselibrary.baseadapter;

import android.content.Context;

/**
 * Created by zhanghs on 2017/11/22/022.
 * 对单一布局的简单adapter的实现
 */

public abstract class EasyRecyclerAdapter<E> extends BaseRecyclerAdapter<E> {
    private int layoutId;
    public EasyRecyclerAdapter(Context context,int layoutId) {
        super(context,false);
        this.layoutId=layoutId;
    }
    @Override
    protected int getLayout(int viewType) {
        return layoutId;
    }

    @Override
    protected int getType(int position) {
        return 0;
    }
}
