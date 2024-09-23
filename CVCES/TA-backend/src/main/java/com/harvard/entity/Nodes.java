package com.harvard.entity;

import lombok.Data;

/**
 * @Author liqing
 * @Description TODO
 * @Date 2022/12/6 下午5:19
 * @Version 1.0
 */
@Data
public class Nodes {

    private String id;
    private String type;
    private int x;
    private int y;
    private Properties properties;
    private Text text;
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

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

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    public Properties getProperties() {
        return properties;
    }

    public void setText(Text text) {
        this.text = text;
    }
    public Text getText() {
        return text;
    }

}
