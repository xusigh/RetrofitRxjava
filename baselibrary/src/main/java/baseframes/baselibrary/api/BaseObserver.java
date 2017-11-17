package baseframes.baselibrary.api;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import baseframes.baselibrary.basebean.BaseBean;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zhanghs on 2017/11/17/017.
 */

public abstract class BaseObserver<T> implements Observer<BaseBean<T>> {
    private static final String TAG = "BaseObserver";
    private Context mContext;
    private ProgressDialog progressDialog;
    private Disposable disposable;
    protected BaseObserver(Context context){
        this.mContext=context.getApplicationContext();
    }
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
            onHandleSuccess((T )t);
        } else {
            onHandleError(value.getTitle());
        }

    }

    @Override
    public void onError(Throwable e) {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
        System.out.println("==================error"+e.getMessage());
        if(e instanceof NetworkErrorException){
            Toast.makeText(mContext, "网络异常，请稍后再试", Toast.LENGTH_LONG).show();
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
