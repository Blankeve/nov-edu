package com.novedu.nov.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.EduCourse;
import com.novedu.nov.edu.entity.dto.EduCourseInfoDTO;
import com.novedu.nov.edu.entity.vo.EduCourseInfoVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    BaseResult queryCourseDetail(Long id);

    BaseResult queryCourseTree(Page page, EduCourseInfoDTO courseInfoDTO);

    BaseResult queryCourseList(EduCourseInfoVO courseInfoVO);

    BaseResult queryCoursesByTeacherId(Long eduTeacher);

    IPage<EduCourseInfoVO> queryCoursePage(Page page, EduCourseInfoDTO courseInfoDTO);

    BaseResult queryCourseById(EduCourse id);

    BaseResult removeCourse(Long id);

    BaseResult<List<EduCourse>> getClientCourseList();

    BaseResult queryClientCoursePage(Page page, EduCourseInfoDTO courseInfoDTO);

    BaseResult statisticsCoursePlayCount();

    BaseResult statisticsCourseApplyCount();

    BaseResult statisticsCourseBuyCount();

    void export(HttpServletResponse response, EduCourseInfoDTO courseInfoDTO);

    BaseResult releaseCourse(EduCourseInfoDTO courseInfoDTO);

    BaseResult queryClientCourseTree(EduCourseInfoDTO courseInfoDTO);

    BaseResult getRecentAddCourses();
}
