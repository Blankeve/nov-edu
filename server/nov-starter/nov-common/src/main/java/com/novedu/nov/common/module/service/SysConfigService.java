package com.novedu.nov.common.module.service;

import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.base.SysConfig;
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

    BaseResult<SysConfig> getSysConfigByKey(String key);

    BaseResult<List<SysConfig>> getConfigList(SysConfig config);

    BaseResult saveConfig(SysConfig config);

    BaseResult removeConfig(Integer id);

    BaseResult<List<SysConfig>> getConfigListByKey(String key,Integer grade);

    BaseResult changeStatus(SysConfig config);
}
