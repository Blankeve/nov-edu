package com.novedu.nov.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.CmsConsult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author juam
 * @since 2022-05-27
 */
public interface CmsConsultService extends IService<CmsConsult> {

    BaseResult queryPage(Page page, CmsConsult cmsConsult);

    BaseResult update(CmsConsult cmsConsult);

    BaseResult queryClientPage(Page page);
}
