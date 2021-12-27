package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduChapter;
import com.novedu.nov.edu.entity.EduCourse;
import com.novedu.nov.edu.entity.EduCourseIntro;
import com.novedu.nov.edu.entity.dto.EduCourseInfoDTO;
import com.novedu.nov.edu.mapper.EduCourseMapper;
import com.novedu.nov.edu.entity.vo.EduCourseInfoVO;
import com.novedu.nov.edu.service.EduChapterService;
import com.novedu.nov.edu.service.EduCourseIntroService;
import com.novedu.nov.edu.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author juam
 * @since 2021-12-23
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    EduCourseIntroService courseIntroService;


    @Autowired
    EduCourseMapper courseMapper;

    @Autowired
    EduChapterService eduChapterService;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult saveCourse(EduCourseInfoDTO courseInfoDTO) {
        EduCourse course = new EduCourse();
        EduCourseIntro courseIntro = new EduCourseIntro();
        BeanUtils.copyProperties(courseInfoDTO,course);
        course.setSubjectId(courseInfoDTO.getSubjectId()[courseInfoDTO.getSubjectId().length-1]);
        save(course);
        BeanUtils.copyProperties(courseInfoDTO,courseIntro);
        courseIntro.setId(course.getId());
        courseIntroService.save(courseIntro);
        return BaseResult.success();
    }

    @Override
    public BaseResult queryCourseDetail(EduCourseInfoVO courseInfoVO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("title",courseInfoVO.getCourseTitle());
        List<EduCourse> eduCourses = list();
        List<EduChapter> chapters = eduChapterService.list();
        for (EduCourse eduCourse : eduCourses) {
            eduCourse.setChildren(new ArrayList<>());
            for (EduChapter chapter : chapters) {
                if(chapter.getCourseId() == eduCourse.getId())
                    eduCourse.getChildren().add(chapter);
            }
        }
        return BaseResult.success(eduCourses);
    }

    @Override
    public BaseResult queryCourseTree(EduCourseInfoVO courseInfoVO) {
        return BaseResult.success(courseMapper.queryCourseTree());
    }

    @Override
    public BaseResult queryCourseList(EduCourseInfoVO courseInfoVO) {
        return BaseResult.success(list());
    }

    @Override
    public BaseResult queryCoursesByTeacherId(Long eduTeacher) {
        List<EduCourse> eduCourses = query().eq("teacher_id",eduTeacher).list();
        return BaseResult.success(eduCourses);
    }



    @Override
    public BaseResult queryCoursePage(Page page, EduCourseInfoVO courseInfoVO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        return BaseResult.success(courseMapper.queryPage(page,null));
    }

}
