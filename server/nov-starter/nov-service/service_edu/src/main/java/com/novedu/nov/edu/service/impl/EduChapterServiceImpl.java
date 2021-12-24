package com.novedu.nov.edu.service.impl;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduChapter;
import com.novedu.nov.edu.mapper.EduChapterMapper;
import com.novedu.nov.edu.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    @Override
    public BaseResult saveChapter(EduChapter chapter) {
        save(chapter);
        return BaseResult.success();
    }

    @Override
    public BaseResult queryChaptersByCourseId(Long id) {
        return BaseResult.success(query().eq("course_id",id).list());
    }
}
