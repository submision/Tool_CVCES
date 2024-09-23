package com.harvard.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @Author liqing
 * @Description TODO
 * @Date 2022/12/25 上午2:14
 * @Version 1.0
 */
@Data
public class Tree {
    @JSONField(ordinal = 1)
    private String name;
    @JSONField(ordinal = 2)
    private Integer level;
    @JSONField(ordinal = 3)
    private String id;
    @JSONField(ordinal = 4)
    private String pid;
    @JSONField(ordinal = 5)
    private List<Tree> children;

    public Tree(String name, Integer level, String id, String pid,List<Tree> children) {
        this.name = name;
        this.level = level;
        this.id = id;
        this.pid = pid;
        this.children = children;
    }

    public String findNameByPid(Tree all,Tree tree){
        String pid = tree.getPid();
        System.out.println("pid是"+pid);
        List<Tree> children = all.getChildren();
        for (int i = 0; i < children.size(); i++) {
            System.out.println("id是："+children.get(i).getId());
            if (children.get(i).getId().equals(pid)){
                return children.get(i).getName();
            }
        }
        return "";
    }
}
