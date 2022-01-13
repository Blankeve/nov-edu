package com.novedu.nov.cms.service;

import com.novedu.nov.cms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.common.api.BaseResult;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author juam
 * @since 2022-01-13
 */
public interface CrmBannerService extends IService<CrmBanner> {

    BaseResult queryBannerList();
}
