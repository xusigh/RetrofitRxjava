package baseframes.baselibrary.api;

import android.content.Context;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhanghs on 2017/11/17/017.
 * token拦截器
 */

public class TokenInterceptor implements Interceptor {
    private Context context;
    public TokenInterceptor(Context context){
        this.context=context;
    }
    private static final Charset UTF8 = Charset.forName("UTF-8");
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        //判断网络
        if(!NetUtils.isNetworkConnected(context)){
            //在请求头中加入：强制使用缓存，不访问网络
            // 无网络时，在响应头中加入：设置超时为4周
            int maxStale = 60 * 60 * 24 * 28;
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        Response response = chain.proceed(request);

        if(NetUtils.isNetworkConnected(context)){
            int maxAge = 0;
            // 有网络时 在响应头中加入：设置缓存超时时间0个小时
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        }

/*
        // try the request
        Response originalResponse = chain.proceed(request);

        *//**通过如下的办法曲线取到请求完成的数据
         * 原本想通过  originalResponse.body().string()
         * 去取到请求完成的数据,但是一直报错,不知道是okhttp的bug还是操作不当
         * 然后去看了okhttp的源码,找到了这个曲线方法,取到请求完成的数据后,根据特定的判断条件去判断token过期
         *//*
        ResponseBody responseBody = originalResponse.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }
        String bodyString = buffer.clone().readString(charset);
        System.out.println(bodyString);*/

        /***************************************/

       /* if (response shows expired token){//根据和服务端的约定判断token过期

            //取出本地的refreshToken
            String refreshToken = "sssgr122222222";

             通过一个特定的接口获取新的token，此处要用到同步的retrofit请求
            ApiService service = ServiceManager.getService(ApiService.class);
            Call<String> call = service.refreshToken(refreshToken);

            要用retrofit的同步方式
            String newToken = call.execute().body();


             create a new request and modify it accordingly using the new token
            Request newRequest = request.newBuilder().header("token", newToken)
                    .build();

            // retry the request

            originalResponse.body().close();
            return chain.proceed(newRequest);
        }*/

        // otherwise just pass the original response on
        return response;
    }
}
