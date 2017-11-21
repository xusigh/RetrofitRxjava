package baseframes.baselibrary.api;

import android.content.Context;

import java.util.List;
import java.util.concurrent.TimeUnit;

import baseframes.baselibrary.basebean.BaseBean;
import baseframes.baselibrary.basebean.Subject;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by zhanghs on 2017/11/16/016.
 * 对网络请求的配置值，以及应用层的网络方法的封装
 */

public class HttpManager {
    private static String baseUrl=ContenUrl.URL_BASE;
    private static final int DEFAULT_TIMEOUT = 5;
    private Retrofit retrofit;
    private ApiRequest request;
    private Context mContext;
    private static class SingleHolder{
        private static  HttpManager httpManager=null;
        public static HttpManager getInstance(Context context){
            if(httpManager==null){
                httpManager=new HttpManager(context);

            }
            return httpManager;
        }
    }

    private HttpManager(Context context){
        this.mContext=context;
        retrofit=new Retrofit.Builder()
                .client(initOkhttpClien().build())
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory( GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        request = retrofit.create(ApiRequest.class);
    }

    public static HttpManager init(Context context){
        return SingleHolder.getInstance(context);
    }
    /**
     * 创建okhttp的构建
     * @return
     */
    private OkHttpClient.Builder initOkhttpClien(){
        //日志显示级别
        OkHttpClient.Builder httpclient=new OkHttpClient().newBuilder();
        httpclient.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //添加 token 过期拦截,和日志打印
        httpclient.addInterceptor(new TokenInterceptor());
        return httpclient;
    }

    /**
     * 请求方法
     * @param strat
     * @param count
     * @param subscriber
     */
    public void getMovie(int strat, int count, BaseObserver<List<Subject>> subscriber){
        request.getMovie(strat,count).compose(RxSchedulers.<BaseBean<List<Subject>>>compose(mContext)).subscribe( subscriber);
    }

}
