package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.edu.client.OrderClient;
import com.novedu.nov.edu.entity.*;
import com.novedu.nov.edu.entity.dto.EduStudyRecordDTO;
import com.novedu.nov.edu.entity.dto.EduVideoInfoDTO;
import com.novedu.nov.edu.entity.vo.EduStudyRecordVO;
import com.novedu.nov.edu.entity.vo.EduVideoInfoVO;
import com.novedu.nov.edu.mapper.EduStudyRecordMapper;
import com.novedu.nov.edu.mapper.EduVideoMapper;
import com.novedu.nov.edu.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author juam
 * @since 2021-12-23
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    EduVideoMapper videoMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    EduChapterService chapterService;

    @Autowired
    EduCourseService courseService;

    @Autowired
    OrderClient orderClient;

    @Autowired
    EduCourseApplyService courseApplyService;

    @Autowired
    EduStudyRecordService studyRecordService;

    @Autowired
    EduStudyRecordMapper studyRecordMapper;

    @Override
    public BaseResult saveVideo(EduVideo video) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("chapter_id", video.getChapterId());
        List<EduVideo> eduVideos = list(queryWrapper);
        if (!ObjectUtils.isEmpty(video.getId()))
            video.setSort(null);
        else {
            if (eduVideos.stream().filter(o -> o.getSort() < (video.getSort())).count() != video.getSort() - 1) {
                return BaseResult.error("请先添加之前小节!");
            }
            if (eduVideos.stream().filter(o -> o.getSort().equals(video.getSort())).count() > 0)
                return BaseResult.error("当前小节已存在!");
        }
        if (StringUtils.hasText(video.getTitle()))
            video.setTitle(video.getTitle().trim());
        saveOrUpdate(video);
        return BaseResult.success(video.getId());
    }


    @Override
    public BaseResult queryVideoPage(Page page, EduVideoInfoDTO videoInfoDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.hasText(videoInfoDTO.getTitle()))
            queryWrapper.like("video.title", videoInfoDTO.getTitle());
        if (videoInfoDTO.getChapterId() != null)
            queryWrapper.eq("video.chapter_id", videoInfoDTO.getChapterId());
        if (videoInfoDTO.getSort() != null && videoInfoDTO.getSort() > 0)
            queryWrapper.eq("video.sort", videoInfoDTO.getSort());
        Date start = videoInfoDTO.getStartTime();
        Date end = videoInfoDTO.getEndTime();
        if (start != null && end != null && end.getTime() > start.getTime())
            queryWrapper.apply("video.create_time > date_format({0},'%Y-%m-%d %H:%i:%s') and video.create_time < date_format({1},'%Y-%m-%d %H:%i:%s')", start, end);
        Page page1 = (Page) videoMapper.queryPage(page, queryWrapper);
        List<EduVideoInfoVO> courses = page1.getRecords();
        String key = "video_play_count";
        boolean hasKey = redisTemplate.hasKey(key);
        Map videoPlayCounts = null;
        if (hasKey) {
            videoPlayCounts = (Map) redisTemplate.opsForValue().get(key);
        }
        Map finalVideoPlayCounts = videoPlayCounts;
        courses.forEach(o -> {
                    if (finalVideoPlayCounts != null) {
                        Long count = (Long) finalVideoPlayCounts.get(o.getVideoId());
                        if (count != null)
                            o.setVideoPlayCount(count);
                    }
                }
        );
        return BaseResult.success(page1);
    }

    @Override
    public BaseResult queryVideoDetail(Long id) {
        return BaseResult.success(getById(id));
    }

    @Override
    public BaseResult removeVideo(Long id) {
        return BaseResult.successOrError(removeById(id));
    }

    public boolean queryOrderByUidAndCourseId(Long id, Long uid) {
        EduVideo video = getById(id);
        if (video.getIsFree().equals(0)) {
            EduChapter chapter = chapterService.getById(video.getChapterId());
            EduCourse course = courseService.getById(chapter.getCourseId());
            EduCourseApply courseApply = new EduCourseApply();
            courseApply.setCourseId(course.getId());
            courseApply.setUid(uid);
            BaseResult baseResult = courseApplyService.queryCourseApplyByCourseIdAndUid(courseApply);
            if (baseResult == null || baseResult.getCode().equals(BaseResult.error().getCode()))
                return false;
        }
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult queryClientVideo(Long id, HttpServletRequest request) {
        Long uid = RequestUtils.getUid();
        if (!queryOrderByUidAndCourseId(id, uid))
            return BaseResult.error("请先购买该课程");
        String key = "video_play_count";
        boolean hasKey = redisTemplate.hasKey(key);
        Long playCount = 1l;
        Map videoPlayCounts;
        if (hasKey) {
            videoPlayCounts = (Map) redisTemplate.opsForValue().get(key);
            playCount = (Long) videoPlayCounts.get(id);
            if (playCount != null) {
                playCount++;
            } else
                playCount = 1l;
        } else {
            videoPlayCounts = new HashMap();
        }
        videoPlayCounts.put(id, playCount);
        redisTemplate.opsForValue().set(key, videoPlayCounts);
        String historyKey = "history_watch_" + uid;
        List<EduStudyRecordVO> historyWatchVOS = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        if (redisTemplate.hasKey(historyKey)) {
            String str = (String) redisTemplate.opsForValue().get(historyKey);
            try {
                historyWatchVOS = objectMapper.readValue(str, new TypeReference<List<EduStudyRecordVO>>() {
                });
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
        EduVideo video = query().eq("id", id).one();
        QueryWrapper chapterWrapper = new QueryWrapper();
        chapterWrapper.eq("id", video.getChapterId());
        EduChapter chapter = chapterService.getOne(chapterWrapper);
        QueryWrapper courseWrapper = new QueryWrapper();
        courseWrapper.eq("id", chapter.getCourseId());
        EduCourse course = courseService.getOne(courseWrapper);
        EduStudyRecordVO historyWatchVO = new EduStudyRecordVO();
        historyWatchVO.setCourseId(course.getId());
        historyWatchVO.setCourseTitle(course.getTitle());
        historyWatchVO.setCourseCover(course.getCover());
        historyWatchVO.setChapterId(chapter.getId());
        historyWatchVO.setChapterTitle(chapter.getTitle());
        historyWatchVO.setChapterSort(chapter.getSort());
        historyWatchVO.setVideoId(id);
        historyWatchVO.setVideoTitle(video.getTitle());
        historyWatchVO.setVideoSort(video.getSort());
        historyWatchVO.setCreateTime(Calendar.getInstance().getTime());
        Iterator<EduStudyRecordVO> iterator = historyWatchVOS.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getCourseId().equals(historyWatchVO.getCourseId())) {
                iterator.remove();
                break;
            }
        }
        historyWatchVOS.add(historyWatchVO);
        try {
            redisTemplate.opsForValue().set(historyKey, new ObjectMapper().writeValueAsString(historyWatchVOS));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        EduStudyRecord studyRecord = new EduStudyRecord();
        BeanUtils.copyProperties(historyWatchVO, studyRecord);
        studyRecord.setUid(uid);
        studyRecordService.save(studyRecord);
        return BaseResult.success(getById(id));
    }

    @Override
    public void exportVideoPage(HttpServletResponse response, Page page, EduVideoInfoDTO videoInfoDTO) {
        BaseResult baseResult = queryVideoPage(page, videoInfoDTO);
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            Page page1 = (Page) baseResult.getData();
            ExcelUtils.exportExcel(page1.getRecords(), "视频信息", "视频信息", EduVideoInfoVO.class, "视频信息", response);
        }
    }

    @Override
    public void exportAll(HttpServletResponse response) {
        BaseResult baseResult = queryVideoPage(new Page(1, count()), new EduVideoInfoDTO());
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            Page page1 = (Page) baseResult.getData();
            ExcelUtils.exportExcel(page1.getRecords(), "视频信息", "视频信息", EduVideoInfoVO.class, "视频信息", response);
        }
    }

    @Override
    public BaseResult queryHistoryWatchPage(Page page) {
        Long uid = RequestUtils.getUid();
        String historyKey = "history_watch_" + uid;
        List<EduStudyRecordVO> historyWatchVOS = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        if (redisTemplate.hasKey(historyKey)) {
            String str = (String) redisTemplate.opsForValue().get(historyKey);
            try {
                historyWatchVOS = objectMapper.readValue(str, new TypeReference<List<EduStudyRecordVO>>() {
                });
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
        Collections.reverse(historyWatchVOS);
        int start = (int) ((page.getCurrent() - 1) * page.getSize());
        // 当前页最后一条数据在List中的位置
        int end = (int) ((start + page.getSize()) > historyWatchVOS.size() ? historyWatchVOS.size() : (page.getSize() * page.getCurrent()));
        page.setRecords(new ArrayList<>());
        if (page.getSize() * (page.getCurrent() - 1) <= page.getTotal()) {
            // 分隔列表 当前页存在数据时 设置
            page.setRecords(historyWatchVOS.subList(start, end));
        }
        page.setTotal(historyWatchVOS.size());
        return BaseResult.success(page);
    }

    @Override
    public BaseResult queryStudyRecordPage(Page page, EduStudyRecordDTO studyRecordDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!StringUtils.isEmpty(studyRecordDTO.getNickname()))
            queryWrapper.like("u.nickname", studyRecordDTO.getNickname());
        if (!StringUtils.isEmpty(studyRecordDTO.getCourseTitle()))
            queryWrapper.like("c.title", studyRecordDTO.getCourseTitle());
        if (!StringUtils.isEmpty(studyRecordDTO.getVideoTitle()))
            queryWrapper.like("v.title", studyRecordDTO.getVideoTitle());
        Date start = studyRecordDTO.getStartTime();
        Date end = studyRecordDTO.getEndTime();
        if (start != null && end != null && end.getTime() > start.getTime())
            queryWrapper.apply("r.create_time > date_format({0},'%Y-%m-%d %H:%i:%s') and r.create_time < date_format({1},'%Y-%m-%d %H:%i:%s')", start, end);
        return BaseResult.success(studyRecordMapper.queryPage(page, queryWrapper));
    }
}
