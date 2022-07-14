package com.novedu.nov.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.system.entity.SysLoginHistory;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author juam
 * @since 2022-05-18
 */
public interface SysLoginHistoryService extends IService<SysLoginHistory> {

    BaseResult queryLoginHistoryPage(Page page, SysLoginHistory loginHistory);
}
