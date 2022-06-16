package com.novedu.nov.ucenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.ucenter.entity.SysLoginHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.ucenter.entity.vo.AclUserRoleVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author juam
 * @since 2022-05-18
 */
public interface SysLoginHistoryService extends IService<SysLoginHistory> {

    BaseResult<List<AclUserRoleVO>> queryLoginHistoryPage(Page page, SysLoginHistory loginHistory);
}
