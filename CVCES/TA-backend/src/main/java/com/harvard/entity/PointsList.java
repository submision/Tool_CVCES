package com.harvard.entity;

import lombok.Data;

/**
 * @Author liqing
 * @Description TODO
 * @Date 2022/12/6 下午5:22
 * @Version 1.0
 */
@Data
public class PointsList {

    private int x;
    private int y;
    public void setX(int x) {
        this.x = x;
    }
    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }
    public int getY() {
        return y;
    }

}
