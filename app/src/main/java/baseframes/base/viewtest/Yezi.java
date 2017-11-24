package baseframes.base.viewtest;

/**
 * Created by zhanghs on 2017/11/24/024.
 */

public class Yezi {
    public float x,y;
    public StartType type;
    public int rotateAngle;
    public int rotateDirection;
    public long startTime;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public StartType getType() {
        return type;
    }

    public void setType(StartType type) {
        this.type = type;
    }

    public int getRotateAngle() {
        return rotateAngle;
    }

    public void setRotateAngle(int rotateAngle) {
        this.rotateAngle = rotateAngle;
    }

    public int getRotateDirection() {
        return rotateDirection;
    }

    public void setRotateDirection(int rotateDirection) {
        this.rotateDirection = rotateDirection;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
