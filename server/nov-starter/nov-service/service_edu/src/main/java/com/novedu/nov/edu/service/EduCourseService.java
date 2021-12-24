package com.novedu.nov.edu.service;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.edu.entity.EduTeacher;
import com.novedu.nov.edu.model.vo.EduCourseInfoVO;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author juam
 * @since 2021-12-23
 */
public interface EduCourseService extends IService<EduCourse> {

    BaseResult saveCourse(EduCourseInfoVO courseInfoVO);

    BaseResult findCourseDetail(EduCourseInfoVO courseInfoVO);

    BaseResult queryCoursesForTreeData(EduCourseInfoVO courseInfoVO);

    BaseResult queryCourseList(EduCourseInfoVO courseInfoVO);

    BaseResult queryCoursesByTeacherId(Long eduTeacher);
}
