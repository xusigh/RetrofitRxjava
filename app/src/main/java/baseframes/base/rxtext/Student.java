package baseframes.base.rxtext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghs on 2017/11/16/016.
 */

public class Student {
    private List<Scort> scorts=new ArrayList<>();
    private String name;

    public List<Scort> getScorts() {
        return scorts;
    }

    public void setScorts(List<Scort> scorts) {
        this.scorts = scorts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
