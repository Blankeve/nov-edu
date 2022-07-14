package com.novedu.nov.common.base;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class BaseEntity {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
