package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.*;
import com.novedu.nov.edu.entity.dto.EduCourseInfoDTO;
import com.novedu.nov.edu.mapper.EduCourseMapper;
import com.novedu.nov.edu.entity.vo.EduCourseInfoVO;
import com.novedu.nov.edu.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @Autowired
    EduVideoService videoService;

    @Autowired
    EduCourseIntroService introService;

    @Autowired
    EduSubjectService subjectService;

    @Autowired
    RedisTemplate redisTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult saveCourse(EduCourseInfoDTO courseInfoDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("teacher_id", courseInfoDTO.getTeacherId());
        queryWrapper.eq("title", courseInfoDTO.getTitle());
        if (!ObjectUtils.isEmpty(courseInfoDTO.getId()))
            queryWrapper.ne("id", courseInfoDTO.getId());
        if (!CollectionUtils.isEmpty(list(queryWrapper)))
            return BaseResult.error("当前课程已存在!");
        EduCourse course = new EduCourse();
        EduCourseIntro courseIntro = new EduCourseIntro();
        BeanUtils.copyProperties(courseInfoDTO, course);
        course.setSubjectId(courseInfoDTO.getSubjectId()[courseInfoDTO.getSubjectId().length - 1]);
        course.setTitle(course.getTitle().trim());
        saveOrUpdate(course);
        BeanUtils.copyProperties(courseInfoDTO, courseIntro);
        courseIntro.setId(course.getId());
        courseIntroService.saveOrUpdate(courseIntro);
        return BaseResult.success();
    }

    @Override
    public BaseResult queryCourseDetail(Integer id) {
        String key = "course_detail_"+id;
        boolean hasKey = redisTemplate.hasKey(key);
        EduCourseInfoVO courseInfoVO;
        if (hasKey) {
            courseInfoVO = (EduCourseInfoVO) redisTemplate.opsForValue().get(key);
        } else {
            courseInfoVO = courseMapper.queryCourseDetail(id);
            List<Integer> arr = subjectService.getParentSubjects(courseInfoVO.getSubjectId()).getData();
            Integer[] arr2 = new Integer[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                arr2[i] = arr.get(i);
            }
            courseInfoVO.setSubjectIds(arr2);
            redisTemplate.opsForValue().set(key, courseInfoVO, 1, TimeUnit.HOURS);
        }
        return BaseResult.success(courseInfoVO);
    }

    @Override
    public BaseResult queryCourseTree(Page page, EduCourseInfoDTO courseInfoDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.hasText(courseInfoDTO.getTitle())) {
            queryWrapper.like("title", courseInfoDTO.getTitle());
        }
        if (courseInfoDTO.getId() != null) {
            queryWrapper.eq("c1.id", courseInfoDTO.getId());
        }
        if (!ObjectUtils.isEmpty(courseInfoDTO.getSubjectId())) {
            Integer subjectId = courseInfoDTO.getSubjectId()[courseInfoDTO.getSubjectId().length - 1];
            queryWrapper.eq("subject_id", subjectId);
        }
        if (!ObjectUtils.isEmpty(courseInfoDTO.getTeacherId()))
            queryWrapper.eq("teacher_id", courseInfoDTO.getTeacherId());
        if (!ObjectUtils.isEmpty(courseInfoDTO.getStatus()))
            queryWrapper.eq("status", courseInfoDTO.getStatus());
        if (!ObjectUtils.isEmpty(courseInfoDTO.getCreateTime()))
            queryWrapper.apply("c1.create_time > date_format({0},'%Y-%m-%d')", courseInfoDTO.getCreateTime());
        if (StringUtils.hasText(courseInfoDTO.getTitle()))
            queryWrapper.orderByAsc("title");

        return BaseResult.success(courseMapper.queryCourseTree(page, queryWrapper));
    }

    @Override
    public BaseResult queryCourseList(EduCourseInfoVO courseInfoVO) {
        return BaseResult.success(list());
    }

    @Override
    public BaseResult queryCoursesByTeacherId(Long eduTeacher) {
        List<EduCourse> eduCourses = query().eq("teacher_id", eduTeacher).list();
        return BaseResult.success(eduCourses);
    }


    @Override
    public BaseResult queryCoursePage(Page page, EduCourseInfoDTO courseInfoDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.hasText(courseInfoDTO.getTitle()))
            queryWrapper.like("course.title", courseInfoDTO.getTitle());
        if (!ObjectUtils.isEmpty(courseInfoDTO.getSubjectId())) {
            Integer subjectId = courseInfoDTO.getSubjectId()[courseInfoDTO.getSubjectId().length - 1];
            queryWrapper.eq("subject_id", subjectId);
        }
        if (!ObjectUtils.isEmpty(courseInfoDTO.getTeacherId()))
            queryWrapper.eq("teacher_id", courseInfoDTO.getTeacherId());
        if (!ObjectUtils.isEmpty(courseInfoDTO.getStatus()))
            queryWrapper.eq("status", courseInfoDTO.getStatus());
        if (!ObjectUtils.isEmpty(courseInfoDTO.getCreateTime()))
            queryWrapper.apply("course.create_time > date_format({0},'%Y-%m-%d')", courseInfoDTO.getCreateTime());
        return BaseResult.success(courseMapper.queryPage(page, queryWrapper));
    }

    @Override
    public BaseResult queryCourseById(EduCourse id) {
        return BaseResult.success(getById(id));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult removeCourse(Integer id) {
        List<EduChapter> chapters = eduChapterService.list();
        List<EduVideo> videos = videoService.list();
        List<Long> chapters1 = chapters.stream().filter(o -> o.getCourseId().equals(id)).map(EduChapter::getId).collect(Collectors.toList());
        List<Long> videos1 = new ArrayList<>();
        for (Long eduChapter : chapters1) {
            for (EduVideo video : videos) {
                if (eduChapter.equals(video.getChapterId()))
                    videos1.add(video.getId());
            }
        }
        removeById(id);
        eduChapterService.removeByIds(chapters1);
        videoService.removeByIds(videos1);
        return BaseResult.success();
    }

    @Override
    public BaseResult<List<EduCourse>> getClientCourseList() {
        return BaseResult.success(query().orderByDesc("view_count").last("limit 8").list());
    }

    @Override
    public BaseResult queryClientCoursePage(Page page, EduCourseInfoDTO courseInfoDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        Integer subjectId = courseInfoDTO.getClientSubjectId();
        if (subjectId != null && subjectId > 0)
            queryWrapper.eq("subject_id", subjectId);
        Integer orderFieldPriceAsc = courseInfoDTO.getOrderFieldPriceAsc();
        if (orderFieldPriceAsc != null) {
            if (orderFieldPriceAsc.equals(1))
                queryWrapper.orderByAsc("price");
            else
                queryWrapper.orderByDesc("price");
        }
        Integer orderFieldNewestAsc = courseInfoDTO.getOrderFieldNewestAsc();
        if (orderFieldNewestAsc != null) {
            if (orderFieldNewestAsc.equals(1))
                queryWrapper.orderByAsc("create_time");
            else
                queryWrapper.orderByDesc("create_time");
        }
        return BaseResult.success(page(page, queryWrapper));
    }

}
