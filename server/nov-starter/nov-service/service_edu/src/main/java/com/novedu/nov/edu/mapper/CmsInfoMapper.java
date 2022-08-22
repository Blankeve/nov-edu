package com.novedu.nov.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.edu.entity.CmsInfo;
import com.novedu.nov.edu.entity.vo.CmsInfoVO;



/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author juam
 * @since 2022-05-31
 */
public interface CmsInfoMapper extends BaseMapper<CmsInfo> {
    IPage<CmsInfoVO> queryPage(Page page, CmsInfo cmsInfo);

    CmsInfoVO queryDetail(String id);
}
