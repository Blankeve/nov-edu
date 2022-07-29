package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.edu.entity.EduChapter;
import com.novedu.nov.edu.entity.EduVideo;
import com.novedu.nov.edu.entity.dto.EduChapterInfoDTO;
import com.novedu.nov.edu.entity.vo.EduChapterInfoVO;
import com.novedu.nov.edu.mapper.EduChapterMapper;
import com.novedu.nov.edu.service.EduChapterService;
import com.novedu.nov.edu.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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
        LambdaQueryWrapper<EduChapter> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(EduChapter::getCourseId, chapter.getCourseId());
        List<EduChapter> eduChapters = list(queryWrapper);
        if (!ObjectUtils.isEmpty(chapter.getId()))
            chapter.setSort(null);
        else {
            if (eduChapters.stream().filter(o -> o.getSort() < (chapter.getSort())).count() != chapter.getSort() - 1) {
                return BaseResult.error("请先添加之前章节!");
            }
            if (eduChapters.stream().filter(o -> o.getSort().equals(chapter.getSort())).count() > 0)
                return BaseResult.error("当前章节已存在!");
        }
        if (StringUtils.hasText(chapter.getTitle()))
            chapter.setTitle(chapter.getTitle().trim());
        saveOrUpdate(chapter);
        return BaseResult.success(chapter.getId());
    }

    @Override
    public BaseResult queryChaptersByCourseId(Long id) {
        return BaseResult.success(lambdaQuery().eq(EduChapter::getCourseId, id).list());
    }

    @Override
    public BaseResult queryChapterPage(Page page, EduChapterInfoDTO chapterInfoDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.hasText(chapterInfoDTO.getTitle()))
            queryWrapper.like("chapter.title", chapterInfoDTO.getTitle());
        if (chapterInfoDTO.getCourseId() != null)
            queryWrapper.eq("course_id", chapterInfoDTO.getCourseId());
        if (chapterInfoDTO.getSort() != null && chapterInfoDTO.getSort() > 0)
            queryWrapper.eq("chapter.sort", chapterInfoDTO.getSort());
        Date start = chapterInfoDTO.getStartTime();
        Date end = chapterInfoDTO.getEndTime();
        if (start != null && end != null && end.getTime() > start.getTime())
            queryWrapper.apply("chapter.create_time > date_format({0},'%Y-%m-%d %H:%i:%s') and chapter.create_time < date_format({1},'%Y-%m-%d %H:%i:%s')", start, end);
        return BaseResult.success(chapterMapper.queryPage(page, queryWrapper));
    }

    @Override
    public BaseResult updateChapterById(EduChapter id) {
        return BaseResult.successOrError(updateById(id));
    }

    @Override
    public BaseResult queryChapterDetail(Long id) {
        return BaseResult.success(getById(id));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult removeChapter(Long id) {
        List<EduVideo> videos = videoService.list();
        List<Long> videos1 = videos.stream().filter(o -> o.getChapterId().equals(id)).map(EduVideo::getId).collect(Collectors.toList());
        removeById(id);
        videoService.removeByIds(videos1);
        return BaseResult.success();
    }

    @Override
    public BaseResult queryChapterList() {
        return BaseResult.success(list());
    }

    @Override
    public void exportChapterPage(HttpServletResponse response, Page page, EduChapterInfoDTO chapterInfoDTO) {
        BaseResult baseResult = queryChapterPage(page, chapterInfoDTO);
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            Page page1 = (Page) baseResult.getData();
            ExcelUtils.exportExcel(page1.getRecords(), "章节信息", "章节信息", EduChapterInfoVO.class, "章节信息", response);
        }
    }

    @Override
    public void exportAll(HttpServletResponse response) {
        BaseResult baseResult = queryChapterPage(new Page(1, count()), new EduChapterInfoDTO());
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            Page page1 = (Page) baseResult.getData();
            ExcelUtils.exportExcel(page1.getRecords(), "章节信息", "章节信息", EduChapterInfoVO.class, "章节信息", response);
        }
    }


}
