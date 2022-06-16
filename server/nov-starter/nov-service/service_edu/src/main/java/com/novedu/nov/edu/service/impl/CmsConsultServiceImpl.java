package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.edu.entity.CmsConsult;
import com.novedu.nov.edu.entity.vo.CmsConsultVO;
import com.novedu.nov.edu.mapper.CmsConsultMapper;
import com.novedu.nov.edu.service.CmsConsultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-05-27
 */
@Service
public class CmsConsultServiceImpl extends ServiceImpl<CmsConsultMapper, CmsConsult> implements CmsConsultService {

    @Autowired
    CmsConsultMapper cmsConsultMapper;

    @Override
    public BaseResult queryPage(Page page, CmsConsult cmsConsult) {
        QueryWrapper queryWrapper = new QueryWrapper();
        Page<CmsConsultVO> cmsConsultVoPage = (Page<CmsConsultVO>) cmsConsultMapper.queryPage(page, queryWrapper);
        return BaseResult.success(cmsConsultVoPage);
    }

    @Override
    public BaseResult update(CmsConsult cmsConsult) {
        cmsConsult.setAdminId(RequestUtils.getUid());
        return BaseResult.successOrError(updateById(cmsConsult));
    }

    @Override
    public BaseResult queryClientPage(Page page) {
        Page<CmsConsultVO> cmsConsultVoPage = (Page<CmsConsultVO>) cmsConsultMapper.queryPage(page,null);
        return BaseResult.success(cmsConsultVoPage);
    }
}
