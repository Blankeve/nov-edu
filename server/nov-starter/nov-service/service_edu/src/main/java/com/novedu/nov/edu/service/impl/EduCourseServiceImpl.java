package com.novedu.nov.edu.service.impl;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduCourse;
import com.novedu.nov.edu.entity.EduCourseIntro;
import com.novedu.nov.edu.mapper.EduCourseMapper;
import com.novedu.nov.edu.model.vo.EduCourseInfoVO;
import com.novedu.nov.edu.service.EduCourseIntroService;
import com.novedu.nov.edu.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author juam
 * @since 2021-12-21
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseIntroService courseIntroService;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult addCourse(EduCourseInfoVO courseInfoVO) {
        EduCourse eduCourse = new EduCourse();
        EduCourseIntro courseIntro = new EduCourseIntro();
        BeanUtils.copyProperties(courseInfoVO, eduCourse);
        BeanUtils.copyProperties(courseInfoVO, courseIntro);
        save(eduCourse);
        courseIntro.setId(eduCourse.getId());
        courseIntroService.save(courseIntro);
        return BaseResult.success();
    }
}
