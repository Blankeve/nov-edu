package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.constants.RedisKeyConstants;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.edu.entity.CmsInfo;
import com.novedu.nov.edu.entity.CmsInfoDetail;
import com.novedu.nov.edu.entity.vo.CmsInfoVO;
import com.novedu.nov.edu.mapper.CmsInfoMapper;
import com.novedu.nov.edu.service.CmsInfoDetailService;
import com.novedu.nov.edu.service.CmsInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private RedisTemplate redisTemplate;
    @Autowired
    private CmsInfoDetailService infoDetailService;
    @Autowired
    private CmsInfoMapper cmsInfoMapper;

    @Override
    public BaseResult queryPage(Page page, CmsInfo cmsInfo) {
        IPage<CmsInfoVO> iPage = cmsInfoMapper.queryPage(page, cmsInfo);
        List<CmsInfoVO> cmsInfoVOS = iPage.getRecords();
        for (CmsInfoVO o : cmsInfoVOS) {
            Long clickCount = (Long) redisTemplate.opsForValue().get(RedisKeyConstants.INFO_CLICK_COUNT + o.getId());
            o.setClickCount(clickCount == null ? 0 : clickCount);
        }
        return BaseResult.success(iPage);
    }

    @Override
    public BaseResult getClientDetail(String id) {
        CmsInfoVO cmsInfoVO = cmsInfoMapper.queryDetail(id);
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
        BeanUtils.copyProperties(cmsInfoVO, cmsInfo);
        CmsInfoDetail cmsInfoDetail = new CmsInfoDetail();
        if (cmsInfoVO.getId() == null) {
            cmsInfo.setCreater(RequestUtils.getUid());
            cmsInfoDetail.setContent(cmsInfoVO.getContent());
            save(cmsInfo);
            cmsInfoDetail.setId(cmsInfo.getId());
            infoDetailService.save(cmsInfoDetail);
        } else {
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
        return BaseResult.success(cmsInfoMapper.queryDetail(id));
    }
}
