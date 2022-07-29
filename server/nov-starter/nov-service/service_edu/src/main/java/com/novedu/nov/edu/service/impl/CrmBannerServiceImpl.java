package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.CrmBanner;
import com.novedu.nov.edu.mapper.CrmBannerMapper;
import com.novedu.nov.edu.service.CrmBannerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-01-13
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Override
    public BaseResult<List<CrmBanner>> getBannerList() {
        return BaseResult.success(list());
    }

    @Override
    public BaseResult saveBanner(CrmBanner banner) {
        if(banner.getId() != null){
            if(StringUtils.isEmpty(banner.getImageUrl()))
                return BaseResult.error("请先上传图片");
        }
        return BaseResult.successOrError(saveOrUpdate(banner));
    }

    @Override
    public BaseResult removeBanner(Long id) {
        return BaseResult.successOrError(removeById(id));
    }


    @Override
    public BaseResult<List<CrmBanner>> getClientBannerList() {
        List<CrmBanner> list = lambdaQuery().orderByDesc(CrmBanner::getSort).orderByDesc(CrmBanner::getCreateTime).last("limit 5").list();
        return BaseResult.success(list);
    }
}
