package baseframes.baselibrary.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import baseframes.baselibrary.basebean.BaseBean;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zhanghs on 2017/11/17/017.
 * 对返回值的封装，以及对请求效果的封装
 */
public abstract class BaseObserver<T> implements Observer<BaseBean<T>> {
    private Context mContext;
    private ProgressDialog progressDialog;
    private Disposable disposable;

    //默认无效果的请求
    protected BaseObserver(Context context){
        this.mContext=context.getApplicationContext();
    }

    //带进度条的请求
    protected BaseObserver(Context context,boolean showProgress){
        this.mContext=context.getApplicationContext();
        if(showProgress){
            progressDialog=new ProgressDialog(context);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    disposable.dispose();
                }
            });
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable=d;
    }

    @Override
    public void onNext(BaseBean<T> value) {
        //这里对数据bean的封装
        if (value.getTotal()>0) {
            T t = value.getSubject();
            onHandleSuccess(t);
        } else {
            onHandleError(value.getTitle());
        }

    }

    @Override
    public void onError(Throwable e) {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onComplete() {
        if(progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    protected abstract void onHandleSuccess(T t);

    protected void onHandleError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
