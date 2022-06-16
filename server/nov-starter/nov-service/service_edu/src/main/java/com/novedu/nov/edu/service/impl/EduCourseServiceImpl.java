package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.base.RoleType;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.common.util.JwtUtils;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.edu.client.UserRoleClient;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
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

    @Autowired
    EduTeacherService teacherService;

    @Autowired
    UserRoleClient userRoleClient;

    private String courseViewCountRedisKey = "course_play_count";

    private boolean needToken = true;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult saveCourse(EduCourseInfoDTO courseInfoDTO) {
        if (courseInfoDTO.getSubjectId() == null || courseInfoDTO.getSubjectId().length < 2) {
            return BaseResult.error("课程分类不能为空");
        }
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
            redisTemplate.opsForValue().set(key, courseInfoVO, 1, TimeUnit.MINUTES);
        }
        return BaseResult.success(courseInfoVO);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult queryCourseTree(Page page, EduCourseInfoDTO courseInfoDTO) {

        if (needToken) {
            Long uid = RequestUtils.getUid();
            BaseResult baseResult = userRoleClient.queryUserRole(Long.valueOf(uid));
            if (baseResult == null) {
                return BaseResult.success();
            }
            Map role = (Map) baseResult.getData();
            Integer code = (Integer) role.get("code");
            if (code == RoleType.TEACHER.getCode()) {
                Long teacherId = teacherService.query().eq("uid", uid).one().getId();
                courseInfoDTO.setTeacherId(teacherId);
            }
        }
        needToken = true;
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
        Date start = courseInfoDTO.getStartTime();
        Date end = courseInfoDTO.getEndTime();
        if (start != null && end != null && end.getTime() > start.getTime())
            queryWrapper.apply("c1.create_time > date_format({0},'%Y-%m-%d %H:%i:%s') and c1.create_time < date_format({1},'%Y-%m-%d %H:%i:%s')", start, end);
        if (StringUtils.hasText(courseInfoDTO.getTitle()))
            queryWrapper.orderByAsc("title");
        IPage<EduCourse> eduCourseIPage = courseMapper.queryCourseTree(page, queryWrapper);
        List<EduCourse> eduCourseList = eduCourseIPage.getRecords();
        eduCourseList.forEach(c -> c.getChildren().forEach(ch -> {
            boolean courseIsFree = false;
            if (c.getPrice().compareTo(BigDecimal.valueOf(0)) == 0)
                courseIsFree = true;
            ch.setCourseIsFree(courseIsFree);
            boolean finalCourseIsFree = courseIsFree;
            ch.getChildren().forEach(v -> v.setCourseIsFree(finalCourseIsFree));
        }));
        return BaseResult.success(eduCourseIPage);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult queryClientCourseTree(EduCourseInfoDTO courseInfoDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (courseInfoDTO.getId() != null) {
            queryWrapper.eq("c1.id", courseInfoDTO.getId());
        }
        return BaseResult.success(courseMapper.queryCourseTree(new Page(1, 1), queryWrapper));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult queryCourseList(EduCourseInfoVO courseInfoVO) {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String token = request.getHeader("X-Token");
        String uid = JwtUtils.getAudience(token).get("uid");
        BaseResult baseResult = userRoleClient.queryUserRole(Long.valueOf(uid));
        if (baseResult == null) {
            return BaseResult.success();
        }
        Map role = (Map) baseResult.getData();
        Integer code = (Integer) role.get("code");
        if (code == RoleType.TEACHER.getCode()) {
            Long teacherId = teacherService.query().eq("uid", uid).one().getId();
            return BaseResult.success(query().eq("teacher_id", teacherId).list());
        }
        return BaseResult.success(list());
    }

    @Override
    public BaseResult queryCoursesByTeacherId(Long eduTeacher) {
        List<EduCourse> eduCourses = query().eq("status", 1).eq("teacher_id", eduTeacher).list();
        setCourseCommentCount(eduCourses);
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
        Date start = courseInfoDTO.getStartTime();
        Date end = courseInfoDTO.getEndTime();
        if (start != null && end != null && end.getTime() > start.getTime())
            queryWrapper.apply("course.create_time > date_format({0},'%Y-%m-%d %H:%i:%s') and course.create_time < date_format({1},'%Y-%m-%d %H:%i:%s')", start, end);
        Page page1 = (Page) courseMapper.queryPage(page, queryWrapper);
        List<EduCourseInfoVO> courses = page1.getRecords();
        setCourseCommentCount2(courses);
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
        List<EduCourse> courses1 = query().eq("status", 1).orderByDesc("view_count").last("limit 8").list();
        setCourseCommentCount(courses1);
        List<EduCourse> courses2 = query().eq("status", 1).orderByDesc("buy_count").last("limit 8").list();
        setCourseCommentCount(courses2);
        // List<EduCourse> courses3 = query().eq("status", 1).gt("price", 0).orderByDesc("buy_count").last("limit 8").list();
        //  setCourseCommentCount(courses3);
        return BaseResult.success()
                .mapSet("c1", courses1)
                .mapSet("c2", courses2);
        //  .mapSet("c3", courses3);
    }

    @Override
    public BaseResult<List<EduCourse>> getClientApplyCourseList() {
        List<EduCourse> courses = query().eq("status", 1).orderByDesc("apply_count").last("limit 8").list();
        setCourseCommentCount(courses);
        return BaseResult.success(courses);
    }

    @Override
    public BaseResult<List<EduCourse>> getClientBoughtCourseList() {
        List<EduCourse> courses = query().eq("status", 1).gt("price", 0).orderByDesc("buy_count").last("limit 8").list();
        setCourseCommentCount(courses);
        return BaseResult.success(courses);
    }


    @Override
    public BaseResult queryClientCoursePage(Page page, EduCourseInfoDTO courseInfoDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        Integer subjectId = courseInfoDTO.getClientSubjectId();
        if (subjectId != null && subjectId > 0)
            queryWrapper.eq("subject_id", subjectId);
        String title = courseInfoDTO.getTitle();
        if (StringUtils.hasText(title))
            queryWrapper.like("title", title);
        Integer orderFieldValue = courseInfoDTO.getOrderFieldValue();
        if (orderFieldValue != null && !orderFieldValue.equals(EduCourseInfoDTO.ORDER_BY.NONE.ordinal())) {
            if (orderFieldValue.equals(EduCourseInfoDTO.ORDER_BY.NEWEST_ASC.ordinal()))
                queryWrapper.orderByAsc("create_time");
            else if (orderFieldValue.equals(EduCourseInfoDTO.ORDER_BY.NEWEST_DESC.ordinal()))
                queryWrapper.orderByDesc("create_time");
            else if (orderFieldValue.equals(EduCourseInfoDTO.ORDER_BY.PRICE_ASC.ordinal()))
                queryWrapper.orderByAsc("price");
            else if (orderFieldValue.equals(EduCourseInfoDTO.ORDER_BY.PRICE_DESC.ordinal()))
                queryWrapper.orderByDesc("price");
        }
        queryWrapper.eq("status", 1);
        Page page1 = page(page, queryWrapper);
        List<EduCourse> courses = page1.getRecords();
        setCourseCommentCount(courses);
        return BaseResult.success(page1);
    }

    private void setCourseCommentCount(List<EduCourse> courses) {
        courses.forEach(o -> {
                    QueryWrapper queryWrapper1 = new QueryWrapper();
                    queryWrapper1.eq("course_id", o.getId());
                    o.setCommentCount((long) commentService.count(queryWrapper1));
                }
        );
    }

    private void setCourseCommentCount2(List<EduCourseInfoVO> courses) {
        courses.forEach(o -> {
                    QueryWrapper queryWrapper1 = new QueryWrapper();
                    queryWrapper1.eq("course_id", o.getCourseId());
                    o.setCourseCommentCount((long) commentService.count(queryWrapper1));
                }
        );
    }

    /*统计各个课程的播放量*/
    @Override
    public BaseResult statisticsCoursePlayCount() {
        needToken = false;
        Page page = (Page) queryCourseTree(new Page(1, count()), new EduCourseInfoDTO()).getData();
        List<EduCourse> courses = page.getRecords();
        String key = "video_play_count";
        boolean hasKey = redisTemplate.hasKey(key);
        Map videoPlayCounts = null;
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
                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.eq("id", course.getId());
                updateWrapper.set("view_count", courseViewCount);
                update(updateWrapper);
            }
        }
        return BaseResult.success();
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

    @Override
    public void exportCoursePage(HttpServletResponse response, Page page, EduCourseInfoDTO courseInfoDTO) {
        BaseResult baseResult = queryCoursePage(page, courseInfoDTO);
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            Page page1 = (Page) baseResult.getData();
            ExcelUtils.exportExcel(page1.getRecords(), "课程信息", "课程信息", EduCourseInfoVO.class, "课程信息", response);
        }
    }

    @Override
    public void exportAll(HttpServletResponse response) {
        BaseResult baseResult = queryCoursePage(new Page(1, count()), new EduCourseInfoDTO());
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            Page page1 = (Page) baseResult.getData();
            ExcelUtils.exportExcel(page1.getRecords(), "课程信息", "课程信息", EduCourseInfoVO.class, "课程信息", response);
        }
    }

    @Override
    public BaseResult releaseCourse(EduCourseInfoDTO courseInfoDTO) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", courseInfoDTO.getId());
        updateWrapper.set("status", courseInfoDTO.getStatus());
        update(updateWrapper);
        return BaseResult.success();
    }

}
