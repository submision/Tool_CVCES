package com.harvard.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author liqing
 * @Description TODO
 * @Date 2022/12/6 下午5:18
 * @Version 1.0
 */
@Data
public class JsonRootBean {

    @JSONField(ordinal = 1)
    private String name;
    @JSONField(ordinal = 2)
    private String declaration;
    @JSONField(ordinal = 3)
    private List<Nodes> nodes;
    @JSONField(ordinal = 4)
    private List<Edges> edges;

    public JsonRootBean(String name, String declaration, List<Nodes> nodes, List<Edges> edges) {
        this.name = name;
        this.declaration = declaration;
        this.nodes = nodes;
        this.edges = edges;
    }

    public JsonRootBean() {

    }

    public void setNodes(List<Nodes> nodes) {
        this.nodes = nodes;
    }
    public List<Nodes> getNodes() {
        return nodes;
    }

    public void setEdges(List<Edges> edges) {
        this.edges = edges;
    }
    public List<Edges> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        return "JsonRootBean{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }
}
