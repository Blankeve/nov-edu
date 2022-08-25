package com.novedu.nov.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.EduChapter;
import com.novedu.nov.edu.entity.dto.EduChapterInfoDTO;
import com.novedu.nov.edu.entity.vo.EduChapterInfoVO;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author juam
 * @since 2021-12-23
 */
public interface EduChapterService extends IService<EduChapter> {

    BaseResult saveChapter(EduChapter chapter);

    BaseResult queryChaptersByCourseId(Long id);

    IPage<EduChapterInfoVO> queryChapterPage(Page page, EduChapterInfoDTO chapterInfoDTO);

    BaseResult updateChapterById(EduChapter id);

    BaseResult queryChapterDetail(Long id);

    BaseResult removeChapter(Long id);

    BaseResult queryChapterList();

    void exportChapterPage(HttpServletResponse response, EduChapterInfoDTO chapterInfoDTO);

}
