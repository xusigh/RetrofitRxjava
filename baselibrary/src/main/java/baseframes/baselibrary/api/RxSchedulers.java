package baseframes.baselibrary.api;

import android.content.Context;
import android.widget.Toast;

import baseframes.baselibrary.utils.NetUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhanghs on 2017/11/17/017.
 * 设置同一的拦截，对每个订阅做相同的标准，这个比另一个要好，可以添加自己想要的compose
 */
public class RxSchedulers {
    public static <T> ObservableTransformer<T, T> compose(final Context context) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if(!NetUtils.isNetworkConnected(context)){
                                    Toast.makeText(context,"网络异常",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


}
