package baseframes.baselibrary.baseadapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zhanghs on 2017/11/22/022.
 */

public  class BaseViewHolder2 extends RecyclerView.ViewHolder{
    private SparseArray<View> viewHolder;
    public View itemView;

    public static BaseViewHolder2 getViewHolder(View view){
        BaseViewHolder2 baseViewHolder2 = (BaseViewHolder2) view.getTag();
        if(baseViewHolder2 ==null){
            baseViewHolder2 =new BaseViewHolder2(view);
            view.setTag(baseViewHolder2);
        }
        return baseViewHolder2;
    }
    private BaseViewHolder2(View itemView) {
        super(itemView);
        this.itemView=itemView;
        viewHolder=new SparseArray<>();
    }
    public <T extends View> T get(int id) {
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = itemView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public View getConvertView() {
        return itemView;
    }

    public TextView getTextView(int id) {

        return get(id);
    }

    public Button getButton(int id) {

        return get(id);
    }

    public ImageView getImageView(int id) {
        return get(id);
    }
}
