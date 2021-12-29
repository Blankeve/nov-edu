package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduChapter;
import com.novedu.nov.edu.entity.vo.EduCourseInfoVO;
import com.novedu.nov.edu.mapper.EduChapterMapper;
import com.novedu.nov.edu.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public BaseResult saveChapter(EduChapter chapter) {
        saveOrUpdate(chapter);
        return BaseResult.success();
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

    @Override
    public BaseResult removeChapter(Integer id) {
        return BaseResult.successOrError(removeById(id));
    }


}
