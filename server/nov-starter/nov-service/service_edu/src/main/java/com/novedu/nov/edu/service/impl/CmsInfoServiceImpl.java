package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.module.entity.SysConfig;
import com.novedu.nov.common.module.service.SysConfigService;
import com.novedu.nov.common.util.BeanListUtils;
import com.novedu.nov.edu.client.UserRoleClient;
import com.novedu.nov.edu.entity.AclUser;
import com.novedu.nov.edu.entity.CmsInfo;
import com.novedu.nov.edu.entity.vo.CmsInfoVO;
import com.novedu.nov.edu.mapper.CmsInfoMapper;
import com.novedu.nov.edu.service.CmsInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-05-31
 */
@Service
public class CmsInfoServiceImpl extends ServiceImpl<CmsInfoMapper, CmsInfo> implements CmsInfoService {

    @Autowired
    CmsInfoMapper cmsInfoMapper;
    @Autowired
    UserRoleClient userRoleClient;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    SysConfigService configService;

    @Override
    public BaseResult queryPage(Page page, CmsInfo cmsInfo) {
        BaseResult baseResult = userRoleClient.syncUsersCache();
        QueryWrapper queryWrapper = new QueryWrapper();
        IPage<CmsInfoVO> iPage = BeanListUtils.copyPage(page(page, queryWrapper), new Page<>(), CmsInfoVO::new);
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            try {
                String key = "usersCache";
                ObjectMapper objectMapper = new ObjectMapper();
                String str = (String) redisTemplate.opsForValue().get(key);
                List<AclUser> users = objectMapper.readValue(str, new TypeReference<List<AclUser>>() {
                });
                List<SysConfig> sysConfigs = configService.getConfigListByKey("info_cate").getData();
                List<CmsInfoVO> cmsInfoVOS = iPage.getRecords();
                for (CmsInfoVO o : cmsInfoVOS) {
                    o.setContent("");
                    if (o.getCreater() != null) {
                        String nickname = users.stream().filter(u -> u.getId().equals(o.getCreater())).findAny().get().getNickname();
                        o.setCreaterNickname(nickname);
                    }
                    if (o.getUpdater() != null) {
                        String nickname = users.stream().filter(u -> u.getId().equals(o.getUpdater())).findAny().get().getNickname();
                        o.setUpdaterNickname(nickname);
                    }
                    String catename = sysConfigs.stream().filter(s -> s.getConfigValue().equals(o.getCate().toString())).findAny().get().getConfigName();
                    o.setCatename(catename);
                }
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                return BaseResult.error();
            }
        }
        return BaseResult.success(iPage);
    }
}
