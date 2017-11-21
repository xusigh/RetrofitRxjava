package baseframes.baselibrary.baseui.baseannotation;

import baseframes.baselibrary.baseui.baseui.UiReinstateActivityLifecycleCallback;

/**
 * Created by zhanghs on 2017/11/21/021.
 * 对注解的提示，以及对ui的封装
 */

public interface UiActivityAnnitation {
    void initView(UiReinstateActivityLifecycleCallback callback);
    void initEvent(UiReinstateActivityLifecycleCallback callback);
}
