package baseframes.baselibrary.baseadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

/**
 * Created by zhanghs on 2017/11/22/022.
 * 只负责业务层的adapter
 */

public abstract class BaseRecyclerAdapter<E> extends BaseArrayRecyclerAdapter<E,BaseViewHolder> {
    protected Context bContext;
    protected RequestManager bGlide;
    private boolean many;
    protected BaseRecyclerAdapter(Context context,boolean many){
        this.bContext=context;
        bGlide= Glide.with(context);
        this.many=many;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(bContext).inflate(getLayout(viewType),parent,false);
        return  BaseViewHolder.getViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        onBind(holder,position);
    }

    @Override
    public int getItemViewType(int position) {
        if(!many){
            return super.getItemViewType(position);
        }else {
            return getType(position);
        }
    }


    //根据viewType来返回不同的布局，如果只有种，直接返回
    protected abstract int getLayout(int viewType);

    public abstract void onBind(BaseViewHolder holder,int position);
    //多布局的type
    protected abstract int getType(int position);
}
