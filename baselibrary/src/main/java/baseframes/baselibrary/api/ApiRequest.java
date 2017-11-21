package baseframes.baselibrary.api;

import java.util.List;

import baseframes.baselibrary.basebean.BaseBean;
import baseframes.baselibrary.basebean.Subject;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhanghs on 2017/11/17/017.
 * 添加网络的接口以及参数的入口
 */
public interface ApiRequest {
    @GET("top250")
    Observable<BaseBean<List<Subject>>> getMovie(@Query("start") int start, @Query("count") int count);
}
