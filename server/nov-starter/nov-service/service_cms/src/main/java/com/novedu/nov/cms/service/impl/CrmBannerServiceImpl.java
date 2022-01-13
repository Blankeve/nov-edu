package com.novedu.nov.cms.service.impl;

import com.novedu.nov.cms.entity.CrmBanner;
import com.novedu.nov.cms.mapper.CrmBannerMapper;
import com.novedu.nov.cms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.common.api.BaseResult;
import org.springframework.stereotype.Service;

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
    public BaseResult queryBannerList() {
        return BaseResult.success(query().orderByDesc("create_time").last("limit 2").list());
    }

}
