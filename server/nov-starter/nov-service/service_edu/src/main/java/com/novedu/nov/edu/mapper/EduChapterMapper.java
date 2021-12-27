package com.novedu.nov.edu.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.edu.entity.EduChapter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novedu.nov.edu.entity.vo.EduCourseInfoVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author juam
 * @since 2021-12-23
 */
@Repository
public interface EduChapterMapper extends BaseMapper<EduChapter> {

    IPage<EduCourseInfoVO> queryPage(Page page, @Param("ew") Wrapper<EduCourseInfoVO> queryWrapper);

}
