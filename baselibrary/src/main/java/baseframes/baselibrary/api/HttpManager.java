package baseframes.baselibrary.api;

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
 */

public class HttpManager {
    private static String baseUrl=ContenUrl.URL_BASE;
    private static final int DEFAULT_TIMEOUT = 5;
    private Retrofit retrofit;
    private ApiRequest request;
    private static class SingletonHolder{
        private static final HttpManager INSTANCE = new HttpManager();
    }
    private HttpManager(){
        retrofit=new Retrofit.Builder()
                .client(initOkhttpClien().build())
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory( GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        request = retrofit.create(ApiRequest.class);
    }

    public  static HttpManager init(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 创建okhttp的构建
     * @return
     */
    private OkHttpClient.Builder initOkhttpClien(){
        OkHttpClient.Builder httpclient=new OkHttpClient().newBuilder();
        httpclient.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return httpclient;
    }
    public void getMovie(int strat, int count, BaseObserver<List<Subject>> subscriber){
        request.getMovie(2,20).compose(new BaseObservableTransFormer<BaseBean<List<Subject>>>()).subscribe( subscriber);
    }

}
