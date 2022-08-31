package com.novedu.nov.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.system.entity.SysOperLog;
import com.novedu.nov.system.entity.dto.SysOperLogDTO;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author juam
 * @since 2022-06-16
 */
public interface SysOperLogService extends IService<SysOperLog> {

    IPage<SysOperLog> getOperLogPage(Page page, SysOperLogDTO sysOperLog);
}
