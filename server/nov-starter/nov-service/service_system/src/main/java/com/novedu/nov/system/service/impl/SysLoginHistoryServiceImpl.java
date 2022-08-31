package com.novedu.nov.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.system.entity.SysLoginHistory;
import com.novedu.nov.system.mapper.SysLoginHistoryMapper;
import com.novedu.nov.system.service.SysLoginHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-05-18
 */
@Service
public class SysLoginHistoryServiceImpl extends ServiceImpl<SysLoginHistoryMapper, SysLoginHistory> implements SysLoginHistoryService {


    @Override
    public IPage<SysLoginHistory> queryLoginHistoryPage(Page page, SysLoginHistory loginHistory) {
        LambdaQueryWrapper<SysLoginHistory> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.hasText(loginHistory.getUsername()), SysLoginHistory::getUsername, loginHistory.getUsername());
        queryWrapper.like(StringUtils.hasText(loginHistory.getLoginIp()), SysLoginHistory::getLoginIp, loginHistory.getLoginIp());
        queryWrapper.like(StringUtils.hasText(loginHistory.getLoginAddress()), SysLoginHistory::getLoginAddress, loginHistory.getLoginAddress());
        queryWrapper.like(StringUtils.hasText(loginHistory.getLoginDevice()), SysLoginHistory::getLoginDevice, loginHistory.getLoginDevice());
        Date start = loginHistory.getStartTime();
        Date end = loginHistory.getEndTime();
        if (start != null && end != null && end.getTime() > start.getTime())
            queryWrapper.apply("create_time > date_format({0},'%Y-%m-%d %H:%i:%s') and create_time < date_format({1},'%Y-%m-%d %H:%i:%s')", start, end);
        queryWrapper.orderByDesc(SysLoginHistory::getCreateTime);
        return page(page, queryWrapper);
    }
}
