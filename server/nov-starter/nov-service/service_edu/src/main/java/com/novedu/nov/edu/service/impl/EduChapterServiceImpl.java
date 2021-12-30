package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduChapter;
import com.novedu.nov.edu.entity.EduVideo;
import com.novedu.nov.edu.entity.vo.EduCourseInfoVO;
import com.novedu.nov.edu.mapper.EduChapterMapper;
import com.novedu.nov.edu.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.edu.service.EduVideoService;
import javafx.print.Collation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
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
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    EduChapterMapper chapterMapper;

    @Autowired
    EduVideoService videoService;

    @Override
    public BaseResult saveChapter(EduChapter chapter) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("course_id",chapter.getCourseId());
        queryWrapper.eq("sort",chapter.getSort());
        if(!CollectionUtils.isEmpty(list(queryWrapper)))
            return BaseResult.error("当前章节已存在!");
        saveOrUpdate(chapter);
        return BaseResult.success(chapter.getId());
    }

    @Override
    public BaseResult queryChaptersByCourseId(Long id) {
        return BaseResult.success(query().eq("course_id",id).list());
    }

    @Override
    public BaseResult queryChapterPage(Page page, EduCourseInfoVO courseInfoVO) {
        return BaseResult.success(chapterMapper.queryPage(page,null));
    }

    @Override
    public BaseResult updateChapterById(EduChapter id) {
        return BaseResult.successOrError(updateById(id));
    }

    @Override
    public BaseResult queryChapterDetail(Integer id) {
        return BaseResult.success(getById(id));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult removeChapter(Integer id) {
        List<EduVideo>videos = videoService.list();
        List<Long> videos1 = videos.stream().filter(o -> o.getChapterId().equals(id)).map(EduVideo::getId).collect(Collectors.toList());
        removeById(id);
        videoService.removeByIds(videos1);
        return BaseResult.success();
    }


}
