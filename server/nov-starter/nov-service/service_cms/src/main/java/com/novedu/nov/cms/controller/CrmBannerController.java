package com.novedu.nov.cms.controller;


import com.novedu.nov.cms.service.CrmBannerService;
import com.novedu.nov.common.api.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author juam
 * @since 2022-01-13
 */
@RestController
@RequestMapping("/cms/banner")
public class CrmBannerController {

    @Autowired
    CrmBannerService crmBannerService;

    @GetMapping("/list")
    public BaseResult queryBannerList() {
        return crmBannerService.queryBannerList();
    }
}

