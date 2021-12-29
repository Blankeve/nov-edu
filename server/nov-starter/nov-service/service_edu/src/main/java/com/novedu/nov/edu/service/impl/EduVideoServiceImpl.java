package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduVideo;
import com.novedu.nov.edu.entity.vo.EduCourseInfoVO;
import com.novedu.nov.edu.mapper.EduVideoMapper;
import com.novedu.nov.edu.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public BaseResult saveVideo(EduVideo video) {
        save(video);
        return BaseResult.success();
    }


    @Override
    public BaseResult queryVideoPage(Page page, EduCourseInfoVO courseInfoVO) {
        return BaseResult.success(videoMapper.queryPage(page,null));
    }

    @Override
    public BaseResult queryVideoDetail(Long id) {
        return BaseResult.success(getById(id));
    }

    @Override
    public BaseResult removeVideo(Long id) {
        return BaseResult.successOrError(removeById(id));
    }
}
