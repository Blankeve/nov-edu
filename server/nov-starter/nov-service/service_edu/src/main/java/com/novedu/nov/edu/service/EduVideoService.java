package com.novedu.nov.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.EduVideo;
import com.novedu.nov.edu.entity.dto.EduVideoInfoDTO;
import com.novedu.nov.edu.entity.vo.EduVideoInfoVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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


    IPage<EduVideoInfoVO> queryVideoPage(Page page, EduVideoInfoDTO videoInfoDTO);

    BaseResult queryVideoDetail(Long id);

    BaseResult removeVideo(Long id);

    BaseResult queryClientVideo(Long id, HttpServletRequest request);

    void exportVideoPage(HttpServletResponse response, EduVideoInfoDTO videoInfoDTO);

    BaseResult queryHistoryWatchPage(Page page);

}
