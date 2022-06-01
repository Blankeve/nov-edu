package com.novedu.nov.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.CmsInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author juam
 * @since 2022-05-31
 */
public interface CmsInfoService extends IService<CmsInfo> {

    BaseResult queryPage(Page page, CmsInfo cmsInfo);
}
