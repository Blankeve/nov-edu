package com.novedu.nov.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.CmsInfo;
import com.novedu.nov.edu.entity.vo.CmsInfoVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author juam
 * @since 2022-05-31
 */
public interface CmsInfoService extends IService<CmsInfo> {

    IPage<CmsInfoVO> queryPage(Page page, CmsInfo cmsInfo);

    BaseResult queryClientDetail(String id);

    BaseResult saveOrUpdateInfo(CmsInfoVO cmsInfoVO);

    BaseResult queryDetail(String id);
}
