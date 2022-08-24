package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.base.RoleType;
import com.novedu.nov.common.constants.RedisKeyConstants;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.edu.client.OpenOrderService;
import com.novedu.nov.edu.client.OpenUcenterService;
import com.novedu.nov.edu.entity.*;
import com.novedu.nov.edu.entity.dto.EduCourseInfoDTO;
import com.novedu.nov.edu.entity.vo.DashBoardInfoVO;
import com.novedu.nov.edu.entity.vo.EduCourseInfoVO;
import com.novedu.nov.edu.mapper.EduCourseMapper;
import com.novedu.nov.edu.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    EduTeacherService teacherService;

    @Autowired
    OpenUcenterService openUcenterService;

    @Autowired
    OpenOrderService openOrderService;


    private boolean needToken = true;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult saveCourse(EduCourseInfoDTO courseInfoDTO) {
        if (courseInfoDTO.getSubjectId() == null || courseInfoDTO.getSubjectId().length < 2) {
            return BaseResult.error("课程分类不能为空");
        }
        LambdaQueryWrapper<EduCourse> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(EduCourse::getTeacherId, courseInfoDTO.getTeacherId());
        queryWrapper.eq(EduCourse::getTitle, courseInfoDTO.getTitle());
        if (!ObjectUtils.isEmpty(courseInfoDTO.getId()))
            queryWrapper.ne(EduCourse::getId, courseInfoDTO.getId());
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
        String key = RedisKeyConstants.COURSE_DETAIL + id;
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
            BaseResult baseResult = openUcenterService.queryUserRole(Long.valueOf(uid));
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

    @Override
    public BaseResult getRecentAddCourses() {
        Long uid = RequestUtils.getUid();
        Integer rolecode = RequestUtils.getRoleCode();
        DashBoardInfoVO dashBoardInfoVO = new DashBoardInfoVO();
        Long teacherId = 0l;
        if (rolecode.equals(RoleType.TEACHER.getCode())) {
            EduTeacher teacher = teacherService.query().eq("uid", uid).one();
            if (teacher == null)
                return BaseResult.error("当前账号尚未绑定讲师");
            teacherId = teacher.getId();
            dashBoardInfoVO.setTeacherName(teacher.getName());
        }
        BaseResult baseResult = openOrderService.queryOrderCount(teacherId);
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            Map orderInfo = (Map) baseResult.getData();
            dashBoardInfoVO.setOrderCount((Integer) orderInfo.get("count"));
            BigDecimal amount = new BigDecimal(orderInfo.get("amount").toString());
            dashBoardInfoVO.setOrderAmount(amount);
        }
        LambdaQueryWrapper<EduCourse> queryWrapper = new LambdaQueryWrapper();
        if (teacherId != 0) {
            queryWrapper.eq(EduCourse::getTeacherId, teacherId);
        }
        queryWrapper.eq(EduCourse::getStatus, 1);
        queryWrapper.orderByDesc(EduCourse::getCreateTime);
        List<EduCourse> courses = list(queryWrapper);
        dashBoardInfoVO.setCourseCount(courses.size());
        if (courses.size() > 4)
            courses = courses.subList(0, 4);
        dashBoardInfoVO.setRecentAddCourses(courses);
        return BaseResult.success(dashBoardInfoVO);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult queryCourseList(EduCourseInfoVO courseInfoVO) {
        Long uid = RequestUtils.getUid();
        BaseResult baseResult = openUcenterService.queryUserRole(uid);
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
        List<EduCourse> eduCourses = lambdaQuery().eq(EduCourse::getStatus, 1).eq(EduCourse::getTeacherId, eduTeacher).list();
        return BaseResult.success(eduCourses);
    }


    @Override
    public IPage<EduCourseInfoVO> queryCoursePage(Page page, EduCourseInfoDTO courseInfoDTO) {
        if (!ObjectUtils.isEmpty(courseInfoDTO.getSubjectId())) {
            Integer subjectId = courseInfoDTO.getSubjectId()[courseInfoDTO.getSubjectId().length - 1];
            courseInfoDTO.setClientSubjectId(subjectId);
        }
        return courseMapper.queryPage(page, courseInfoDTO);
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
        List<EduCourse> courses1;
        List<EduCourse> courses2;
        if (redisTemplate.hasKey(RedisKeyConstants.CLIENT_COURSE_LIST1)) {
            courses1 = (List<EduCourse>) redisTemplate.opsForValue().get(RedisKeyConstants.CLIENT_COURSE_LIST1);
        } else {
            courses1 = lambdaQuery().eq(EduCourse::getStatus, 1).orderByDesc(EduCourse::getViewCount).last("limit 8").list();
            redisTemplate.opsForValue().set(RedisKeyConstants.CLIENT_COURSE_LIST1, courses1, 30, TimeUnit.MINUTES);
        }
        if (redisTemplate.hasKey(RedisKeyConstants.CLIENT_COURSE_LIST2)) {
            courses2 = (List<EduCourse>) redisTemplate.opsForValue().get(RedisKeyConstants.CLIENT_COURSE_LIST2);
        } else {
            courses2 = lambdaQuery().eq(EduCourse::getStatus, 1).orderByDesc(EduCourse::getBuyCount).last("limit 8").list();
            redisTemplate.opsForValue().set(RedisKeyConstants.CLIENT_COURSE_LIST2, courses2, 30, TimeUnit.MINUTES);
        }
        return BaseResult.success()
                .map("c1", courses1)
                .map("c2", courses2);
    }


    @Override
    public BaseResult queryClientCoursePage(Page page, EduCourseInfoDTO courseInfoDTO) {
        LambdaQueryWrapper<EduCourse> queryWrapper = new LambdaQueryWrapper();
        Integer subjectId = courseInfoDTO.getClientSubjectId();
        if (subjectId != null && subjectId > 0)
            queryWrapper.eq(EduCourse::getSubjectId, subjectId);
        String title = courseInfoDTO.getTitle();
        if (StringUtils.hasText(title))
            queryWrapper.like(EduCourse::getTitle, title);
        Integer orderFieldValue = courseInfoDTO.getOrderFieldValue();
        if (orderFieldValue != null && !orderFieldValue.equals(EduCourseInfoDTO.ORDER_BY.NONE.ordinal())) {
            if (orderFieldValue.equals(EduCourseInfoDTO.ORDER_BY.NEWEST_ASC.ordinal()))
                queryWrapper.orderByAsc(EduCourse::getCreateTime);
            else if (orderFieldValue.equals(EduCourseInfoDTO.ORDER_BY.NEWEST_DESC.ordinal()))
                queryWrapper.orderByDesc(EduCourse::getCreateTime);
            else if (orderFieldValue.equals(EduCourseInfoDTO.ORDER_BY.PRICE_ASC.ordinal()))
                queryWrapper.orderByAsc(EduCourse::getPrice);
            else if (orderFieldValue.equals(EduCourseInfoDTO.ORDER_BY.PRICE_DESC.ordinal()))
                queryWrapper.orderByDesc(EduCourse::getPrice);
        }
        queryWrapper.eq(EduCourse::getStatus, 1);
        return BaseResult.success(page(page, queryWrapper));
    }

    /*统计各个课程的播放量*/
    @Override
    public BaseResult statisticsCoursePlayCount() {
        needToken = false;
        Page page = (Page) queryCourseTree(new Page(1, count()), new EduCourseInfoDTO()).getData();
        List<EduCourse> courses = page.getRecords();
        boolean hasKey = redisTemplate.hasKey(RedisKeyConstants.VIDEO_PLAY_COUNT);
        Map videoPlayCounts = null;
        if (hasKey)
            videoPlayCounts = (Map) redisTemplate.opsForValue().get(RedisKeyConstants.VIDEO_PLAY_COUNT);
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
                int count = commentService.lambdaQuery().eq(EduComment::getCourseId, course.getId()).count();
                LambdaUpdateWrapper<EduCourse> updateWrapper = new LambdaUpdateWrapper();
                updateWrapper.eq(EduCourse::getId, course.getId());
                updateWrapper.set(EduCourse::getViewCount, courseViewCount);
                updateWrapper.set(EduCourse::getCommentCount, count);
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
    public void export(HttpServletResponse response, EduCourseInfoDTO courseInfoDTO) {
        ExcelUtils.exportExcel(queryCoursePage(new Page(1, -1), courseInfoDTO).getRecords(), "课程信息", "课程信息", EduCourseInfoVO.class, "课程信息", response);
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
