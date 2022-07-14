package com.novedu.nov.edu.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.edu.entity.CmsConsult;
import com.novedu.nov.edu.entity.vo.CmsConsultVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author juam
 * @since 2022-05-27
 */
public interface CmsConsultMapper extends BaseMapper<CmsConsult> {
    IPage<CmsConsultVO> queryPage(Page page, @Param("ew") Wrapper<CmsConsult> queryWrapper);
}
