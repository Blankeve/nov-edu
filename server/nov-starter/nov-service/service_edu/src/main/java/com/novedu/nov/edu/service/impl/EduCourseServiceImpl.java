package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.*;
import com.novedu.nov.edu.entity.dto.EduCourseInfoDTO;
import com.novedu.nov.edu.mapper.EduCourseApplyMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    EduCommentService commentService;

    private String courseViewCountRedisKey = "course_play_count";

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
    public BaseResult queryCourseDetail(Long id) {
        String key = "course_detail_" + id;
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
            redisTemplate.opsForValue().set(key, courseInfoVO, 5, TimeUnit.MINUTES);
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
        Page page1 = (Page) courseMapper.queryPage(page, queryWrapper);
        List<EduCourseInfoVO> courses = page1.getRecords();
        setCourseCommentCountAndViewCount2(courses);
        return BaseResult.success(page1);
    }

    @Override
    public BaseResult queryCourseById(EduCourse id) {
        return BaseResult.success(getById(id));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult removeCourse(Long id) {
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
        List<EduCourse> courses = query().orderByDesc("view_count").last("limit 8").list();
        setCourseCommentCountAndViewCount(courses);
        return BaseResult.success(courses);
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
        Page page1 = page(page, queryWrapper);
        List<EduCourse> courses = page1.getRecords();
        setCourseCommentCountAndViewCount(courses);
        return BaseResult.success(page1);
    }

    private void setCourseCommentCountAndViewCount(List<EduCourse> courses) {
        boolean hasKey = redisTemplate.hasKey(courseViewCountRedisKey);
        if (!hasKey) {
            return;
        }
        Map coursePlayCount = (Map) redisTemplate.opsForValue().get(courseViewCountRedisKey);
        Map finalCoursePlayCount = coursePlayCount;
        courses.forEach(o -> {
                    QueryWrapper queryWrapper1 = new QueryWrapper();
                    queryWrapper1.eq("course_id", o.getId());
                    o.setCommentCount((long) commentService.count(queryWrapper1));
                    if (finalCoursePlayCount != null) {
                        o.setViewCount((Long) finalCoursePlayCount.get(o.getId()));
                    }
                }
        );
    }

    private void setCourseCommentCountAndViewCount2(List<EduCourseInfoVO> courses) {
        boolean hasKey = redisTemplate.hasKey(courseViewCountRedisKey);
        if (!hasKey) {
            return;
        }
        Map coursePlayCount = (Map) redisTemplate.opsForValue().get(courseViewCountRedisKey);
        Map finalCoursePlayCount = coursePlayCount;
        courses.forEach(o -> {
                    QueryWrapper queryWrapper1 = new QueryWrapper();
                    queryWrapper1.eq("course_id", o.getCourseId());
                    o.setCourseCommentCount((long) commentService.count(queryWrapper1));
                    if (finalCoursePlayCount != null) {
                        o.setCourseViewCount((Long) finalCoursePlayCount.get(o.getCourseId()));
                    }
                }
        );
    }

    /*统计各个课程的播放量*/
    @Override
    public BaseResult statisticsCoursePlayCount() {
        Page page = (Page) queryCourseTree(new Page(1, count()), new EduCourseInfoDTO()).getData();
        List<EduCourse> courses = page.getRecords();
        String key = "video_play_count";
        boolean hasKey = redisTemplate.hasKey(key);
        Map videoPlayCounts = null;
        Map coursePlayCounts = new HashMap();
        if (hasKey)
            videoPlayCounts = (Map) redisTemplate.opsForValue().get(key);
        if (videoPlayCounts != null) {
            Map finalVideoPlayCounts = videoPlayCounts;
            for (EduCourse course : courses) {
                Long courseViewCount = 0l;
                List<EduChapter> chapters = course.getChildren();
                if (!CollectionUtils.isEmpty(chapters))
                    for (EduChapter chapter : chapters) {
                        List<EduVideo> videos = chapter.getChildren();
                        if (!CollectionUtils.isEmpty(videos))
                            for (EduVideo video : videos) {
                                Long playCount = (Long) finalVideoPlayCounts.get(video.getId());
                                if (playCount != null)
                                    courseViewCount += playCount;
                            }
                    }
                coursePlayCounts.put(course.getId(), courseViewCount);
            }
        }
        redisTemplate.opsForValue().set(courseViewCountRedisKey, coursePlayCounts);
        return BaseResult.success();
    }

    @Override
    public BaseResult<List<EduCourse>> getClientApplyCourseList() {
        List<EduCourse> courses = query().orderByDesc("apply_count").last("limit 8").list();
        setCourseCommentCountAndViewCount(courses);
        return BaseResult.success(courses);
    }

    @Override
    public BaseResult<List<EduCourse>> getClientBoughtCourseList() {
        List<EduCourse> courses = query().gt("price",0).orderByDesc("buy_count").last("limit 8").list();
        setCourseCommentCountAndViewCount(courses);
        return BaseResult.success(courses);
    }

    @Override
    public BaseResult statisticsCourseApplyCount() {
        courseMapper.statisticsCourseApplyCount();
        return BaseResult.success();
    }

    @Override
    public BaseResult statisticsCourseBuyCount() {
        courseMapper.statisticsCourseBuyCount();
        return BaseResult.success();
    }

}
