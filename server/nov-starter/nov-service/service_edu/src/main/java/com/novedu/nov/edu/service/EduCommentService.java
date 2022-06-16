package com.novedu.nov.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.edu.entity.dto.EduUserCommentDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    BaseResult queryCommentPage(Page page, EduComment eduComment);

    BaseResult queryCommentPage(HttpServletRequest request,Page page, EduUserCommentDTO eduComment);

    BaseResult removeComment(Long id);

    BaseResult reportComment(Long id, HttpServletRequest request);

    void exportCommentPage(HttpServletResponse response, Page page, EduUserCommentDTO eduComment);

    void exportAll(HttpServletResponse response, EduUserCommentDTO eduComment);
}
