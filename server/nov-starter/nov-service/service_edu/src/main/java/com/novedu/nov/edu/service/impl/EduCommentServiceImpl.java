package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.util.JwtUtils;
import com.novedu.nov.edu.entity.EduComment;
import com.novedu.nov.edu.entity.EduCourse;
import com.novedu.nov.edu.entity.EduCourseApply;
import com.novedu.nov.edu.entity.vo.EduUserCommentVO;
import com.novedu.nov.edu.mapper.EduCommentMapper;
import com.novedu.nov.edu.service.EduCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.edu.service.EduCourseApplyService;
import com.novedu.nov.edu.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Autowired
    EduCourseService courseService;

    @Autowired
    EduCourseApplyService courseApplyService;

    @Override
    public BaseResult saveComment(EduComment eduComment, HttpServletRequest request) {
        String token = request.getHeader("X-Token");
        Long uid = Long.valueOf(JwtUtils.getAudience(token).get("uid"));
        Long courseId = eduComment.getCourseId();
        EduCourse course = courseService.getById(courseId);
        EduCourseApply courseApply = new EduCourseApply();
        courseApply.setCourseId(courseId);
        courseApply.setUid(uid);
        BaseResult baseResult = courseApplyService.queryCourseApplyByCourseIdAndUid(courseApply);
        if (baseResult == null || BaseResult.error().getCode().equals(baseResult.getCode())) {
            if (Float.parseFloat(course.getPrice().toString()) > 0) {
                return BaseResult.error("购买该课程后才能评论哦");
            } else {
                return BaseResult.error("报名该课程后才能评论哦");
            }
        }
        eduComment.setUid(uid);
        if (!StringUtils.hasText(eduComment.getContent())) {
            Integer mark = eduComment.getMark();
            String content = "该用户觉得";
            if (mark > 3) {
                content += "很好";
            } else if (mark == 3) {
                content += "一般";
            } else
                content += "不尽人意";
            eduComment.setContent(content);
        }
        return BaseResult.successOrError(save(eduComment));
    }

    @Override
    public BaseResult queryCommentPage(Page page, EduComment eduComment) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("course_id", eduComment.getCourseId());
        return BaseResult.success(commentMapper.queryPage(page, queryWrapper));
    }

    @Override
    public BaseResult queryCommentPage(Page page, EduUserCommentVO eduComment) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.hasText(eduComment.getNickname()))
            queryWrapper.like("u.nickname", eduComment.getNickname());
        if (eduComment.getReported() != null)
            queryWrapper.eq("comment.reported", eduComment.getReported());
        if (eduComment.getCourseId() != null)
            queryWrapper.eq("comment.course_id", eduComment.getCourseId());
        if (eduComment.getUid() != null)
            queryWrapper.eq("comment.uid", eduComment.getUid());
        if (eduComment.getTeacherId() != null)
            queryWrapper.eq("comment.teacher_id", eduComment.getTeacherId());
        return BaseResult.success(commentMapper.queryPage(page, queryWrapper));
    }

    @Override
    public BaseResult removeComment(Long id) {
        return BaseResult.successOrError(removeById(id));
    }

    @Override
    public BaseResult reportComment(Long id, HttpServletRequest request) {
        EduComment comment = getById(id);
        String token = request.getHeader("X-Token");
        Long uid = Long.valueOf(JwtUtils.getAudience(token).get("uid"));
        if (comment.getUid().equals(uid))
            return BaseResult.error("不能举报自己哦");
        Integer count = query().eq("id", id).eq("report_uid", uid).count();
        if (count > 0)
            return BaseResult.error("您已经举报过该条评论啦");
        comment.setReported(1);
        comment.setReportUid(uid);
        return BaseResult.successOrError(updateById(comment));
    }

}
