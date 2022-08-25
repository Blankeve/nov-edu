package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
    public IPage<EduChapterInfoVO> queryChapterPage(Page page, EduChapterInfoDTO chapterInfoDTO) {
        return chapterMapper.queryPage(page, chapterInfoDTO);
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
    public void exportChapterPage(HttpServletResponse response, EduChapterInfoDTO chapterInfoDTO) {
        ExcelUtils.exportExcel(queryChapterPage(new Page(1, -1), chapterInfoDTO).getRecords(), "章节信息", "章节信息", EduChapterInfoVO.class, "课程信息", response);
    }




}
