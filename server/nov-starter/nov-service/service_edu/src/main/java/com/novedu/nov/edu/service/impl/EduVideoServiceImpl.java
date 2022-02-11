package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduCourse;
import com.novedu.nov.edu.entity.EduVideo;
import com.novedu.nov.edu.entity.dto.EduVideoInfoDTO;
import com.novedu.nov.edu.entity.vo.EduCourseInfoVO;
import com.novedu.nov.edu.mapper.EduVideoMapper;
import com.novedu.nov.edu.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        if (videoInfoDTO.getCreateTime() != null)
            queryWrapper.apply("video.create_time > date_format({0},'%Y-%m-%d')", videoInfoDTO.getCreateTime());
        Page page1 = (Page) videoMapper.queryPage(page, queryWrapper);
        List<EduCourseInfoVO> courses = page1.getRecords();
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

    @Override
    public BaseResult queryClientVideo(Long id) {
        String key = "video_play_count";
        boolean hasKey = redisTemplate.hasKey(key);
        Long playCount = 1l;
        Map videoPlayCounts;
        if (hasKey) {
            videoPlayCounts = (Map) redisTemplate.opsForValue().get(key);
            playCount = (Long) videoPlayCounts.get(id);
            if (playCount != null) {
                playCount++;
            }else
                playCount =1l;
        } else {
            videoPlayCounts = new HashMap();
        }
        videoPlayCounts.put(id, playCount);
        redisTemplate.opsForValue().set(key, videoPlayCounts);
        return BaseResult.success(getById(id));
    }
}
