package baseframes.base.adapter;

/**
 * Created by zhanghs on 2018/1/15/015.
 */

public class Bean_Adapter {
    private String title;
    private String imgUrl;
    private String time;

    public Bean_Adapter(String title, String imgUrl, String time) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.time = time;
    }

    public Bean_Adapter() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
