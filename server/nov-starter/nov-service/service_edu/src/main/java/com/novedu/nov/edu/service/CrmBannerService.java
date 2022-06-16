package com.novedu.nov.edu.service;

import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author juam
 * @since 2022-01-13
 */
public interface CrmBannerService extends IService<CrmBanner> {

    BaseResult<List<CrmBanner>> getBannerList();

    BaseResult saveBanner(CrmBanner banner);

    BaseResult removeBanner(Long id);

    BaseResult<List<CrmBanner>> getClientBannerList();
}
