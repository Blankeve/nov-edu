package com.novedu.nov.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.system.entity.SysOperLog;

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
