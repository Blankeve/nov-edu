package com.novedu.nov.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.CmsNotice;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 广告推荐 服务类
 * </p>
 *
 * @author juam
 * @since 2022-02-28
 */
public interface CmsNoticeService extends IService<CmsNotice> {

    BaseResult saveOrUpdateNotice(CmsNotice cmsNotice);

    BaseResult removeNotice(String id);

    BaseResult queryNoticePage(Page page, CmsNotice cmsNotice);

    BaseResult receiveNotice();
}
