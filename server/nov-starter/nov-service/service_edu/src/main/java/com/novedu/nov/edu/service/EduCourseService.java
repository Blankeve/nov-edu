package com.novedu.nov.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
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

    BaseResult queryCoursePage(Page page, EduCourseInfoDTO courseInfoDTO);

    BaseResult queryCourseById(EduCourse id);

    BaseResult removeCourse(Long id);

    BaseResult<List<EduCourse>> getClientCourseList();

    BaseResult queryClientCoursePage(Page page, EduCourseInfoDTO courseInfoDTO);

    BaseResult statisticsCoursePlayCount();

    BaseResult<List<EduCourse>> getClientApplyCourseList();

    BaseResult<List<EduCourse>> getClientBoughtCourseList();

    BaseResult statisticsCourseApplyCount();

    BaseResult statisticsCourseBuyCount();

    void exportCoursePage(HttpServletResponse response, Page page, EduCourseInfoDTO courseInfoDTO);

    void exportAll(HttpServletResponse response);
}
