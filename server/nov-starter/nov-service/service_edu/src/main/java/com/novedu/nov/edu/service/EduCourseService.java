package com.novedu.nov.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.edu.entity.dto.EduCourseInfoDTO;
import com.novedu.nov.edu.entity.vo.EduCourseInfoVO;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author juam
 * @since 2021-12-23
 */
public interface EduCourseService extends IService<EduCourse> {

    BaseResult saveCourse(EduCourseInfoDTO eduCourseInfoDTO);

    BaseResult queryCourseDetail(Integer id);

    BaseResult queryCourseTree(EduCourseInfoVO courseInfoVO);

    BaseResult queryCourseList(EduCourseInfoVO courseInfoVO);

    BaseResult queryCoursesByTeacherId(Long eduTeacher);

    BaseResult queryCoursePage(Page page, EduCourseInfoVO courseInfoVO);

    BaseResult queryCourseById(EduCourse id);

    BaseResult removeCourse(Integer id);
}
