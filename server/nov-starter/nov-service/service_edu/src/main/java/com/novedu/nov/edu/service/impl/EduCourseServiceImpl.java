package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduChapter;
import com.novedu.nov.edu.entity.EduCourse;
import com.novedu.nov.edu.entity.EduCourseIntro;
import com.novedu.nov.edu.mapper.EduCourseMapper;
import com.novedu.nov.edu.model.vo.EduCourseInfoVO;
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
    EduChapterService eduChapterService;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult saveCourse(EduCourseInfoVO courseInfoVO) {
        EduCourse course = new EduCourse();
        EduCourseIntro courseIntro = new EduCourseIntro();
        BeanUtils.copyProperties(courseInfoVO,course);
        course.setSubjectId(courseInfoVO.getSubjectId()[courseInfoVO.getSubjectId().length-1]);
        save(course);
        BeanUtils.copyProperties(courseInfoVO,courseIntro);
        courseIntro.setId(course.getId());
        courseIntroService.save(courseIntro);
        return BaseResult.success();
    }

    @Override
    public BaseResult findCourseDetail(EduCourseInfoVO courseInfoVO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("title",courseInfoVO.getTitle());
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

}
