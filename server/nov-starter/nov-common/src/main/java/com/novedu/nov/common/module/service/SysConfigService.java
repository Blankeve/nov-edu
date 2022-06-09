package com.novedu.nov.common.module.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.module.entity.SysConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author juam
 * @since 2022-01-07
 */
public interface SysConfigService extends IService<SysConfig> {

    BaseResult<SysConfig> getRootConfigByKey(String key);

    BaseResult<List<SysConfig>> getConfigList(SysConfig config);

    BaseResult saveConfig(SysConfig config);

    BaseResult removeConfig(Integer id);

    BaseResult<List<SysConfig>> getConfigListByKey(String key,Integer grade);
}
