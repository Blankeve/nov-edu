package com.novedu.nov.common.module.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.base.SysOperLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author juam
 * @since 2022-06-16
 */
public interface SysOperLogService extends IService<SysOperLog> {

    BaseResult<List<SysOperLog>> getOperLogPage(Page page, SysOperLog sysOperLog);
}
