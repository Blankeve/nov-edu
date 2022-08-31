package com.novedu.nov.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.common.constants.AuthConstant;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.system.entity.SysOperLog;
import com.novedu.nov.system.entity.dto.SysOperLogDTO;
import com.novedu.nov.system.mapper.SysOperLogMapper;
import com.novedu.nov.system.service.SysOperLogService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-06-16
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService {

    @Override
    public IPage<SysOperLog> getOperLogPage(Page page, SysOperLogDTO sysOperLog) {
        LambdaQueryWrapper<SysOperLog> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(!StringUtils.isEmpty(sysOperLog.getOperName()), SysOperLog::getOperName, sysOperLog.getOperName());
        queryWrapper.like(!StringUtils.isEmpty(sysOperLog.getOperIp()), SysOperLog::getOperIp, sysOperLog.getOperIp());
        queryWrapper.like(!StringUtils.isEmpty(sysOperLog.getOperAddr()), SysOperLog::getOperAddr, sysOperLog.getOperAddr());
        queryWrapper.like(!StringUtils.isEmpty(sysOperLog.getMethod()), SysOperLog::getMethod, sysOperLog.getMethod());
        queryWrapper.like(!StringUtils.isEmpty(sysOperLog.getReqUrl()), SysOperLog::getReqUrl, sysOperLog.getReqUrl());
        Date start = sysOperLog.getStartTime();
        Date end = sysOperLog.getEndTime();
        if (start != null && end != null && end.getTime() > start.getTime())
            queryWrapper.apply("req_time > date_format({0},'%Y-%m-%d %H:%i:%s') and req_time < date_format({1},'%Y-%m-%d %H:%i:%s')", start, end);
        queryWrapper.orderByDesc(SysOperLog::getReqTime);
        IPage<SysOperLog> page1 = page(page, queryWrapper);
        if (RequestUtils.getRoleCode() != AuthConstant.ADMIN_ROLE_CODE) {
            for (SysOperLog record : page1.getRecords()) {
                record.setReqArgs("没有权限");
                record.setReqResult("没有权限");
            }
        }
        return page1;
    }
}
