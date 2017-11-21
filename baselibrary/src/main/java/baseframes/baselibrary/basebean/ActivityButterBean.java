package baseframes.baselibrary.basebean;


import java.io.Serializable;

import butterknife.Unbinder;


/**
 * Created by zhanghs on 2017/11/21/021.
 * 对butternife的封装
 */

public class ActivityButterBean implements Serializable {
    public ActivityButterBean(Unbinder unbinder) {
        this.unbinder = unbinder;
    }

    private Unbinder unbinder;

    public Unbinder getUnbinder() {
        return unbinder;
    }

    public void setUnbinder(Unbinder unbinder) {
        this.unbinder = unbinder;
    }

}
