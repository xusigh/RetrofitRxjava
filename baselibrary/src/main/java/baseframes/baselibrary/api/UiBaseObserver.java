package baseframes.baselibrary.api;

import android.content.Context;

/**
 * Created by zhanghs on 2017/11/17/017.
 */

public class UiBaseObserver<T> extends BaseObserver<T> {
    protected UiBaseObserver(Context context) {
        super(context);
    }

    protected UiBaseObserver(Context context, boolean showProgress) {
        super(context, showProgress);
    }

    @Override
    protected void onHandleSuccess(T t) {

    }
}
