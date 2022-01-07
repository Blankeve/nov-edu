package com.novedu.nov.edu.service.impl;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.SysConfig;
import com.novedu.nov.edu.mapper.SysConfigMapper;
import com.novedu.nov.edu.service.SysConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-01-07
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {


    @Override
    public BaseResult<SysConfig> getConfigByKey(String key) {
        SysConfig config = query().eq("config_key", key).one();
        return config == null ? BaseResult.error() : BaseResult.success(config);
    }

    @Override
    public BaseResult<List<SysConfig>> getConfigList() {
        return BaseResult.success(list());
    }

    @Override
    public BaseResult saveConfig(SysConfig config) {
        return BaseResult.successOrError(saveOrUpdate(config));
    }

    @Override
    public BaseResult removeConfig(Integer id) {
        return BaseResult.successOrError(removeById(id));
    }


}
