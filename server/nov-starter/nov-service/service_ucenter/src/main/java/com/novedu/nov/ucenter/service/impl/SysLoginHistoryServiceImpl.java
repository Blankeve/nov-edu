package com.novedu.nov.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.ucenter.entity.SysLoginHistory;
import com.novedu.nov.ucenter.entity.vo.AclUserRoleVO;
import com.novedu.nov.ucenter.mapper.SysLoginHistoryMapper;
import com.novedu.nov.ucenter.service.SysLoginHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

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
    public BaseResult<List<AclUserRoleVO>> queryLoginHistoryPage(Page page, SysLoginHistory loginHistory) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.hasText(loginHistory.getUsername()))
            queryWrapper.like("username", loginHistory.getUsername());
        Date start = loginHistory.getStartTime();
        Date end = loginHistory.getEndTime();
        if (start != null && end != null && end.getTime() > start.getTime())
            queryWrapper.apply("create_time > date_format({0},'%Y-%m-%d %H:%i:%s') and create_time < date_format({1},'%Y-%m-%d %H:%i:%s')", start, end);
        queryWrapper.orderByDesc("create_time");
        return BaseResult.success(page(page, queryWrapper));
    }
}
