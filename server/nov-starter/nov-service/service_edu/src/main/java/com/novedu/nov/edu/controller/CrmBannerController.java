package com.novedu.nov.edu.controller;


import com.novedu.nov.common.annotation.UserMultiSubmitLimit;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.CrmBanner;
import com.novedu.nov.edu.service.CrmBannerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author juam
 * @since 2022-01-13
 */
@RestController
@RequestMapping("/edu/banner")
public class CrmBannerController {

    @Autowired
    CrmBannerService crmBannerService;

    @GetMapping("/list")
    public BaseResult<List<CrmBanner>> getBannerList(){
        return crmBannerService.getBannerList();
    }

    @UserMultiSubmitLimit
    @PostMapping("/save")
    public BaseResult saveBanner(CrmBanner banner){
        return crmBannerService.saveBanner(banner);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{id}")
    public BaseResult removeBanner(@PathVariable Long id) {
        return crmBannerService.removeBanner(id);
    }

    @GetMapping("/client-list")
    public BaseResult<List<CrmBanner>> getClientBannerList(){
        return crmBannerService.getClientBannerList();
    }
}

