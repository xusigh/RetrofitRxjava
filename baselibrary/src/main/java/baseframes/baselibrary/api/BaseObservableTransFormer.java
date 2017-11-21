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
 * 设置一个拦截，对每一个订阅事件都设置统一的标准
 * @param <T>
 */
public class  BaseObservableTransFormer<T> implements ObservableTransformer<T,T>{
    private Context context;
    public BaseObservableTransFormer(Context context){
        this.context=context;
    }
    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        //判断网络
                        if(!NetUtils.isNetworkConnected(context)){
                            Toast.makeText(context,"网络异常",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}
