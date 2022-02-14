package com.novedu.nov.edu.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.edu.entity.EduVideo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novedu.nov.edu.entity.dto.EduVideoInfoDTO;
import com.novedu.nov.edu.entity.vo.EduCourseInfoVO;
import com.novedu.nov.edu.entity.vo.EduVideoInfoVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 课程视频 Mapper 接口
 * </p>
 *
 * @author juam
 * @since 2021-12-23
 */
@Repository
public interface EduVideoMapper extends BaseMapper<EduVideo> {

    IPage<EduVideoInfoVO> queryPage(Page page, @Param("ew") Wrapper<EduVideoInfoDTO> queryWrapper);

}
