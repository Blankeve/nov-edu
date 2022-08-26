package com.novedu.nov.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.EduComment;
import com.novedu.nov.edu.entity.dto.EduUserCommentDTO;
import com.novedu.nov.edu.entity.vo.EduUserCommentVO;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author juam
 * @since 2022-01-26
 */
public interface EduCommentService extends IService<EduComment> {

    BaseResult saveComment(EduComment eduComment, HttpServletRequest request);

    BaseResult queryClientCommentPage(Page page, EduUserCommentDTO eduComment);

    IPage<EduUserCommentVO> queryCommentPage(Page page, EduUserCommentDTO eduComment);

    BaseResult removeComment(Long id);

    BaseResult reportComment(Long id, HttpServletRequest request);

}
