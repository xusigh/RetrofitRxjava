package baseframes.baselibrary.crashlib.exception;

/**
 * Created by zhengxiaoyong on 16/8/28.
 */
public class ReinstateException extends RuntimeException {
    public ReinstateException(String message) {
        super(message);
    }

    public ReinstateException(String message, Throwable cause) {
        super(message, cause);
    }
}
