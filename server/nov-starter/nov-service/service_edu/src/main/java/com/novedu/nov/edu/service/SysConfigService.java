package com.novedu.nov.edu.service;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.SysConfig;
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

    BaseResult<SysConfig> getConfigByKey(String key);

    BaseResult<List<SysConfig>> getConfigList();

    BaseResult saveConfig(SysConfig config);

    BaseResult removeConfig(Integer id);
}
