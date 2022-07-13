package com.novedu.nov.common.module.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.module.entity.SysOperLog;
import com.novedu.nov.common.module.service.SysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author juam
 * @since 2022-06-16
 */
@RestController
@RequestMapping("/ucenter/oper-log")
public class SysOperLogController {

    @Autowired
    SysOperLogService sysOperLogService;

    @GetMapping("/page")
    public BaseResult<List<SysOperLog>> getOperLogPage(Page page, SysOperLog sysOperLog) {
        return sysOperLogService.getOperLogPage(page,sysOperLog);
    }
}

