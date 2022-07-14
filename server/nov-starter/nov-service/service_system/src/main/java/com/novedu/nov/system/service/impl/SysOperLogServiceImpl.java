package com.novedu.nov.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.common.base.BaseResult;


import com.novedu.nov.system.entity.SysOperLog;
import com.novedu.nov.system.mapper.SysOperLogMapper;
import com.novedu.nov.system.service.SysOperLogService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-06-16
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService {

    @Override
    public BaseResult<List<SysOperLog>> getOperLogPage(Page page, SysOperLog sysOperLog) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(sysOperLog.getOperName()))
        queryWrapper.like("oper_name",sysOperLog.getOperName());
        if(!StringUtils.isEmpty(sysOperLog.getOperIp()))
        queryWrapper.like("oper_ip",sysOperLog.getOperIp());
        if(!StringUtils.isEmpty(sysOperLog.getOperAddr()))
        queryWrapper.like("oper_addr",sysOperLog.getOperAddr());
        if(!StringUtils.isEmpty(sysOperLog.getMethod()))
        queryWrapper.like("method",sysOperLog.getMethod());
        if(!StringUtils.isEmpty(sysOperLog.getReqUrl()))
            queryWrapper.like("req_url",sysOperLog.getReqUrl());
        Date start = sysOperLog.getStartTime();
        Date end = sysOperLog.getEndTime();
        if (start != null && end != null && end.getTime() > start.getTime())
            queryWrapper.apply("req_time > date_format({0},'%Y-%m-%d %H:%i:%s') and req_time < date_format({1},'%Y-%m-%d %H:%i:%s')", start, end);
        queryWrapper.orderByDesc("req_time");
        return BaseResult.success(page(page,queryWrapper));
    }
}
