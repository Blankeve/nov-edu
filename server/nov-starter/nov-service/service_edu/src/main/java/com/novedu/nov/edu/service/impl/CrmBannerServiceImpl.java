package com.novedu.nov.edu.service.impl;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.CrmBanner;
import com.novedu.nov.edu.mapper.CrmBannerMapper;
import com.novedu.nov.edu.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
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
        return BaseResult.successOrError(saveOrUpdate(banner));
    }

    @Override
    public BaseResult removeBanner(Long id) {
        return BaseResult.successOrError(removeById(id));
    }

    @Cacheable("client-banners")
    public List<CrmBanner> getClientBannerListCache(){
        return query().orderByDesc("create_time").last("limit 2").list();
    }

    @Override
    public BaseResult<List<CrmBanner>> getClientBannerList() {
        return BaseResult.success(getClientBannerListCache());
    }
}
