package baseframes.baselibrary.crashlib.callback;

/**
 * Created by zhengxiaoyong on 16/8/29.
 * 恢复的回掉
 */
public interface ReinstateCallback {

    void stackTrace(String stackTrace);

    void cause(String cause);

    void exception(String throwExceptionType, String throwClassName, String throwMethodName, int throwLineNumber);

    void throwable(Throwable throwable);
}
