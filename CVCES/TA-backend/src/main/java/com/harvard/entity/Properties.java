package com.harvard.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @Author liqing
 * @Description TODO
 * @Date 2022/12/6 下午5:20
 * @Version 1.0
 */
@Data
public class Properties {
    private String select;
    private String guard;
    private String synchronisation;
    private String assignment;
    @JSONField(name = "isActived")
    private boolean isActived;

}
