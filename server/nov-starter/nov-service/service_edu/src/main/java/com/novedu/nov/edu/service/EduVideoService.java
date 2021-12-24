package com.novedu.nov.edu.service;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author juam
 * @since 2021-12-23
 */
public interface EduVideoService extends IService<EduVideo> {

    BaseResult saveVideo(EduVideo video);
}
