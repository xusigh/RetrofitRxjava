package baseframes.baselibrary.basebean;

/**
 * Created by zhanghs on 2017/11/21/021.
 */

public abstract class BaseEventBean<T>{
    private int code;
    private int message;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
