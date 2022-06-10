package com.novedu.nov.common.module.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.module.entity.SysConfig;
import com.novedu.nov.common.module.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author juam
 * @since 2022-01-07
 */
@Api("系统配置的接口文档")
@RestController
@RequestMapping("/edu/config")
public class SysConfigController {

    @Autowired
    SysConfigService sysConfigService;

    @GetMapping("/list")
    public BaseResult<List<SysConfig>> getConfigList(SysConfig config) {
        return sysConfigService.getConfigList(config);
    }

    @PostMapping("/change/status")
    public BaseResult changeStatus(SysConfig config) {
        return sysConfigService.changeStatus(config);
    }

    @PostMapping("/save")
    public BaseResult saveConfig(@Validated SysConfig config) {
        return sysConfigService.saveConfig(config);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{ids}")
    public BaseResult removeConfig(@PathVariable Integer[] ids) {
        return BaseResult.successOrError(sysConfigService.removeByIds(Arrays.asList(ids)));
    }

    @PostMapping("/list")
    public BaseResult<List<SysConfig>> getConfigListByKey(String key, Integer grade) {
        return sysConfigService.getConfigListByKey(key, grade);
    }
}

