package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.constants.RedisKeyConstants;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.edu.client.OpenUcenterService;
import com.novedu.nov.edu.entity.AclUser;
import com.novedu.nov.edu.entity.CmsInfo;
import com.novedu.nov.edu.entity.CmsInfoDetail;
import com.novedu.nov.edu.entity.vo.CmsInfoVO;
import com.novedu.nov.edu.mapper.CmsInfoMapper;
import com.novedu.nov.edu.service.CmsInfoDetailService;
import com.novedu.nov.edu.service.CmsInfoService;
import com.novedu.nov.system.entity.SysConfig;
import com.novedu.nov.system.service.SysConfigService;
import com.novedu.nov.system.utils.BeanListUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

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
    private CmsInfoMapper cmsInfoMapper;
    @Autowired
    private OpenUcenterService openUcenterService;
    @Autowired
    private SysConfigService configService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CmsInfoDetailService infoDetailService;

    @Override
    public BaseResult queryPage(Page page, CmsInfo cmsInfo) {
        BaseResult baseResult = openUcenterService.syncUsersCache();
        IPage<CmsInfoVO> iPage = new Page<>();
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String str = (String) redisTemplate.opsForValue().get(RedisKeyConstants.USERS_CACHE);
                List<AclUser> users = objectMapper.readValue(str, new TypeReference<List<AclUser>>() {
                });
                List<SysConfig> sysConfigs = configService.getConfigListByKey("artcle_cate", 2).getData();
                QueryWrapper queryWrapper = new QueryWrapper();
                if (!StringUtils.isEmpty(cmsInfo.getTitle())) {
                    queryWrapper.like("title", cmsInfo.getTitle());
                }
                if (cmsInfo.getCate() != null) {
                    queryWrapper.eq("cate", cmsInfo.getCate());
                }
                if (!StringUtils.isEmpty(cmsInfo.getCreaterNickname())) {
                    Long id = users.stream().filter(o -> o.getNickname().equals(cmsInfo.getCreaterNickname())).findAny().get().getId();
                    queryWrapper.eq("creater", id);
                }
                Date start = cmsInfo.getStartTime();
                Date end = cmsInfo.getEndTime();
                if (start != null && end != null && end.getTime() > start.getTime())
                    queryWrapper.apply("create_time > date_format({0},'%Y-%m-%d %H:%i:%s') and create_time < date_format({1},'%Y-%m-%d %H:%i:%s')", start, end);
                queryWrapper.orderByDesc("create_time");
                iPage = BeanListUtils.copyPage(page(page, queryWrapper), new Page<>(), CmsInfoVO::new);
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
                    try {
                        String catename = sysConfigs.stream().filter(s -> s.getConfigValue().equals(o.getCate().toString())).findAny().get().getConfigName();
                        o.setCatename(catename);
                    } catch (NoSuchElementException ex) {
                        log.error(ex.getMessage());
                    }
                    Long clickCount = (Long) redisTemplate.opsForValue().get(RedisKeyConstants.INFO_CLICK_COUNT + o.getId());
                    o.setClickCount(clickCount);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return BaseResult.success(iPage);
    }

    @Override
    public BaseResult getClientDetail(String id) {
        CmsInfoDetail cmsInfoDetail = infoDetailService.getById(id);
        CmsInfo cmsInfo = getById(id);
        CmsInfoVO cmsInfoVO = new CmsInfoVO();
        BeanUtils.copyProperties(cmsInfo, cmsInfoVO);
        cmsInfoVO.setContent(cmsInfoDetail.getContent());
        BaseResult baseResult = openUcenterService.syncUsersCache();
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            ObjectMapper objectMapper = new ObjectMapper();
            String str = (String) redisTemplate.opsForValue().get(RedisKeyConstants.USERS_CACHE);
            try {
                List<AclUser> users = objectMapper.readValue(str, new TypeReference<List<AclUser>>() {
                });
                if (cmsInfo.getCreater() != null) {
                    String nickname = users.stream().filter(u -> u.getId().equals(cmsInfo.getCreater())).findAny().get().getNickname();
                    cmsInfoVO.setCreaterNickname(nickname);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Long clickCount = (Long) redisTemplate.opsForValue().get(RedisKeyConstants.INFO_CLICK_COUNT + id);
        if (clickCount != null) {
            clickCount++;
        } else
            clickCount = 1l;
        redisTemplate.opsForValue().set(RedisKeyConstants.INFO_CLICK_COUNT + id, clickCount);
        cmsInfoVO.setClickCount(clickCount);
        return BaseResult.success(cmsInfoVO);
    }

    @Transactional
    @Override
    public BaseResult saveOrUpdateInfo(CmsInfoVO cmsInfoVO) {
        CmsInfo cmsInfo = new CmsInfo();
        BeanUtils.copyProperties(cmsInfoVO,cmsInfo);
        CmsInfoDetail cmsInfoDetail = new CmsInfoDetail();
        if(cmsInfoVO.getId() == null){
            cmsInfo.setCreater(RequestUtils.getUid());
            cmsInfoDetail.setContent(cmsInfoVO.getContent());
            save(cmsInfo);
            cmsInfoDetail.setId(cmsInfo.getId());
            infoDetailService.save(cmsInfoDetail);
        }
        else{
            cmsInfo.setUpdater(RequestUtils.getUid());
            updateById(cmsInfo);
            cmsInfoDetail.setId(cmsInfo.getId());
            cmsInfoDetail.setContent(cmsInfoVO.getContent());
            infoDetailService.updateById(cmsInfoDetail);
        }
        return BaseResult.success();
    }

    @Transactional
    @Override
    public BaseResult getDetail(String id) {
        CmsInfoDetail cmsInfoDetail = infoDetailService.getById(id);
        CmsInfo cmsInfo = getById(id);
        CmsInfoVO cmsInfoVO = new CmsInfoVO();
        BeanUtils.copyProperties(cmsInfo, cmsInfoVO);
        cmsInfoVO.setContent(cmsInfoDetail.getContent());
        return BaseResult.success(cmsInfoVO);
    }
}
