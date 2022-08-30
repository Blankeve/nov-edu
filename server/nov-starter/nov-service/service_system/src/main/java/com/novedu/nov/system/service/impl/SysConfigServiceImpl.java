package com.novedu.nov.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.util.TreeUtils;
import com.novedu.nov.system.entity.SysConfig;
import com.novedu.nov.system.entity.dto.SysConfigDTO;
import com.novedu.nov.system.mapper.SysConfigMapper;
import com.novedu.nov.system.service.SysConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public BaseResult<SysConfig> getSysConfigByKey(String key) {
        SysConfig config = null;
        List<SysConfig> sysConfigs = lambdaQuery().eq(SysConfig::getStatus, 1).eq(SysConfig::getConfigKey, key).list();
        if (!CollectionUtils.isEmpty(sysConfigs))
            config = sysConfigs.get(0);
        return BaseResult.success(config);
    }

    @Override
    public BaseResult<List<SysConfig>> getConfigList(SysConfigDTO config) {
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper();
        if (!StringUtils.isEmpty(config.getConfigName()))
            queryWrapper.like(SysConfig::getConfigName, config.getConfigName());
        if (!StringUtils.isEmpty(config.getConfigKey()))
            queryWrapper.like(SysConfig::getConfigKey, config.getConfigKey());
        if (!ObjectUtils.isEmpty(config.getStatus()))
            queryWrapper.eq(SysConfig::getStatus, config.getStatus());
        Date start = config.getStartTime();
        Date end = config.getEndTime();
        if (start != null && end != null && end.getTime() > start.getTime())
            queryWrapper.apply("create_time > date_format({0},'%Y-%m-%d %H:%i:%s') and create_time < date_format({1},'%Y-%m-%d %H:%i:%s')", start, end);
        List<SysConfig> sysConfigs = list(queryWrapper);
        sysConfigs = (List<SysConfig>) TreeUtils.toTree(sysConfigs, SysConfig.class);
        return BaseResult.success(sysConfigs);
    }

    @Override
    public BaseResult saveConfig(SysConfig config) {
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysConfig::getConfigName, config.getConfigName());
        if (config.getId() != null) {
            queryWrapper.ne(SysConfig::getId, config.getId());
        }
        Integer count = count(queryWrapper);
        if (count > 0) {
            return BaseResult.error("操作失败，存在相同名称：" + config.getConfigName());
        }
        return BaseResult.successOrError(saveOrUpdate(config));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult removeConfig(Integer id) {
        List<SysConfig> sysConfigs = list();
        List<SysConfig> sysConfigList = (List<SysConfig>) TreeUtils.getChildren(id, sysConfigs, SysConfig.class);
        List<Integer> ids = sysConfigList.stream().map(SysConfig::getId).collect(Collectors.toList());
        ids.add(sysConfigs.stream().filter(s -> s.getId().equals(id)).findAny().get().getId());
        return BaseResult.successOrError(removeByIds(ids));
    }

    @Override
    public BaseResult<List<SysConfig>> getConfigListByKey(String key, Integer grade) {
        return BaseResult.success(lambdaQuery().eq(SysConfig::getStatus, 1).eq(SysConfig::getConfigKey, key).eq(SysConfig::getGrade, grade).list());
    }

    @Override
    public BaseResult changeStatus(SysConfig config) {
        List<SysConfig> sysConfigs = list();
        List<SysConfig> sysConfigList = (List<SysConfig>) TreeUtils.getChildren(config.getId(), sysConfigs, SysConfig.class);
        sysConfigList.add(sysConfigs.stream().filter(s -> s.getId().equals(config.getId())).findAny().get());
        sysConfigList.forEach(s -> s.setStatus(config.getStatus()));
        return BaseResult.successOrError(updateBatchById(sysConfigList));
    }


}
