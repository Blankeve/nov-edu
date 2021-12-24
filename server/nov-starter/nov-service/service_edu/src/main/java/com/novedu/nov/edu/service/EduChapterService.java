package com.novedu.nov.edu.service;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
