package com.harvard.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author liqing
 * @Description TODO
 * @Date 2022/12/6 下午5:23
 * @Version 1.0
 */
@Data
public class Edges {

    private String id;
    private String type;
    private String sourceNodeId;
    private String targetNodeId;
    private StartPoint startPoint;
    private EndPoint endPoint;
    private Properties properties;
    private Text text;
    private List<PointsList> pointsList;
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

    public void setSourceNodeId(String sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }
    public String getSourceNodeId() {
        return sourceNodeId;
    }

    public void setTargetNodeId(String targetNodeId) {
        this.targetNodeId = targetNodeId;
    }
    public String getTargetNodeId() {
        return targetNodeId;
    }

    public void setStartPoint(StartPoint startPoint) {
        this.startPoint = startPoint;
    }
    public StartPoint getStartPoint() {
        return startPoint;
    }

    public void setEndPoint(EndPoint endPoint) {
        this.endPoint = endPoint;
    }
    public EndPoint getEndPoint() {
        return endPoint;
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

    public void setPointsList(List<PointsList> pointsList) {
        this.pointsList = pointsList;
    }
    public List<PointsList> getPointsList() {
        return pointsList;
    }

}
