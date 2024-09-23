package com.harvard.entity;

import lombok.Data;

/**
 * @Author liqing
 * @Description TODO
 * @Date 2022/12/6 下午5:21
 * @Version 1.0
 */
@Data
public class Text {

    private int x;
    private int y;
    private String value;
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

    public void setValue(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

}
