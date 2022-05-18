package com.novedu.nov.ucenter.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.ucenter.entity.SysLoginHistory;
import com.novedu.nov.ucenter.entity.dto.AclUserRoleDTO;
import com.novedu.nov.ucenter.entity.vo.AclUserRoleVO;
import com.novedu.nov.ucenter.service.SysLoginHistoryService;
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
 * @since 2022-05-18
 */
@RestController
@RequestMapping("/ucenter/sys-login-history")
public class SysLoginHistoryController {

    @Autowired
    SysLoginHistoryService sysLoginHistoryService;

    @GetMapping("/page")
    public BaseResult<List<AclUserRoleVO>> queryLoginHistoryPage(Page page, SysLoginHistory loginHistory) {
        return sysLoginHistoryService.queryLoginHistoryPage(page, loginHistory);
    }

}

