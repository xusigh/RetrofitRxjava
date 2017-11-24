package baseframes.base.viewtest;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by zhanghs on 2017/11/24/024.
 */

public class YeziFactory {
    private static final int MAX_NUM=6;
    // 叶子飘动一个周期所花的时间
    private static final long LEAF_FLOAT_TIME = 3000;

    private int mAddTime;
    Random random=new Random();
    public Yezi getYezi(){
        Yezi yezi=new Yezi();
        int randomType=random.nextInt(3);
        StartType type=StartType.MIDDLE;
        switch (randomType){
            case 0:
                break;
            case 1:
                type=StartType.LITTLE;
                break;
            case 2:
                type=StartType.BIG;
                break;
            default:break;
        }
        yezi.type=type;
        yezi.rotateAngle=random.nextInt(360);
        yezi.rotateDirection=random.nextInt(2);
        mAddTime += random.nextInt((int) (LEAF_FLOAT_TIME * 1.5));
        yezi.startTime = System.currentTimeMillis() + mAddTime;
        return yezi;
    }
    // 根据最大叶子数产生叶子信息
    public List<Yezi> generateLeafs() {
        return generateLeafs(MAX_NUM);
    }

    // 根据传入的叶子数量产生叶子信息
    public List<Yezi> generateLeafs(int leafSize) {
        List<Yezi> leafs = new LinkedList<Yezi>();
        for (int i = 0; i < leafSize; i++) {
            leafs.add(getYezi());
        }
        return leafs;
    }
}
