package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.util.JwtUtils;
import com.novedu.nov.edu.entity.EduComment;
import com.novedu.nov.edu.mapper.EduCommentMapper;
import com.novedu.nov.edu.service.EduCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-01-26
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    @Autowired
    EduCommentMapper commentMapper;

    @Override
    public BaseResult saveComment(EduComment eduComment, HttpServletRequest request) {
        String token = request.getHeader("X-Token");
        Long uid = Long.valueOf(JwtUtils.getAudience(token).get("uid"));
        eduComment.setUid(uid);
        return BaseResult.successOrError(save(eduComment));
    }

    @Override
    public BaseResult queryCommentPage(Page page, EduComment eduComment) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("course_id",eduComment.getCourseId());
        return BaseResult.success(commentMapper.queryPage(page,queryWrapper));
    }

}
