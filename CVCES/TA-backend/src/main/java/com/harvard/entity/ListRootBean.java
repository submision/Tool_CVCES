package com.harvard.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author liqing
 * @Description TODO
 * @Date 2022/12/25 上午12:14
 * @Version 1.0
 */
@Data
public class ListRootBean {
    private String declaration;
    private List<JsonRootBean> jsonRootBeans;
}
