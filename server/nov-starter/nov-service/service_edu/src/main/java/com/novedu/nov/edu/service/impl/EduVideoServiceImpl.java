package com.novedu.nov.edu.service.impl;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduVideo;
import com.novedu.nov.edu.mapper.EduVideoMapper;
import com.novedu.nov.edu.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    @Override
    public BaseResult saveVideo(EduVideo video) {
        save(video);
        return BaseResult.success();
    }
}
