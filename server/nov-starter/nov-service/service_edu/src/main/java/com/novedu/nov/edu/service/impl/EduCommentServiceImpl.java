package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.base.RoleType;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.edu.client.OpenOrderService;
import com.novedu.nov.edu.client.OpenUcenterService;
import com.novedu.nov.edu.entity.EduComment;
import com.novedu.nov.edu.entity.EduCourse;
import com.novedu.nov.edu.entity.dto.EduUserCommentDTO;
import com.novedu.nov.edu.entity.vo.EduUserCommentVO;
import com.novedu.nov.edu.mapper.EduCommentMapper;
import com.novedu.nov.edu.service.EduCommentService;
import com.novedu.nov.edu.service.EduCourseApplyService;
import com.novedu.nov.edu.service.EduCourseService;
import com.novedu.nov.edu.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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

    @Autowired
    OpenUcenterService openUcenterService;

    @Autowired
    EduTeacherService teacherService;

    @Autowired
    OpenOrderService openOrderService;

    public boolean queryOrderByUidAndCourseId(Long id, Long uid) {
        BaseResult baseResult = openOrderService.queryOrderByUidAndCourseId(id, uid);
        if (BaseResult.success().getCode().equals(baseResult.getCode())) {
            Map paid = (Map) baseResult.getData();
            if (paid.get("paid").equals(true))
                return true;
        }
        return false;
    }

    @Override
    public BaseResult saveComment(EduComment eduComment, HttpServletRequest request) {
        Long uid = RequestUtils.getUid();
        Long courseId = eduComment.getCourseId();
        EduCourse course = courseService.getById(courseId);
        boolean hasBuy = queryOrderByUidAndCourseId(courseId, uid);
        if (!hasBuy) {
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
    public BaseResult queryClientCommentPage(Page page, EduUserCommentDTO eduComment) {
        IPage<EduUserCommentVO> page1 = commentMapper.queryPage(page, eduComment);
        for (EduUserCommentVO record : page1.getRecords()) {
            if (!StringUtils.hasText(record.getNickname()))
                record.setNickname("已注销");
        }
        return BaseResult.success(page1);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public IPage<EduUserCommentVO> queryCommentPage(Page page, EduUserCommentDTO eduComment) {
        Long uid = RequestUtils.getUid();
        BaseResult baseResult = openUcenterService.queryUserRole(uid);
        if (baseResult == null) {
            return new Page<>();
        }
        Map role = (Map) baseResult.getData();
        Integer code = (Integer) role.get("code");
        if (code == RoleType.TEACHER.getCode()) {
            Long teacherId = teacherService.query().eq("uid", uid).one().getId();
            eduComment.setTeacherId(teacherId);
        }
        return commentMapper.queryPage(page, eduComment);
    }

    @Override
    public BaseResult removeComment(Long id) {
        return BaseResult.successOrError(removeById(id));
    }

    @Override
    public BaseResult reportComment(Long id, HttpServletRequest request) {
        EduComment comment = getById(id);
        Long uid = RequestUtils.getUid();
        if (comment.getUid().equals(uid))
            return BaseResult.error("不能举报自己的评论");
        Integer count = lambdaQuery().eq(EduComment::getId, id).eq(EduComment::getReportUid, uid).count();
        if (count > 0)
            return BaseResult.error("您已经举报过该条评论啦");
        comment.setReported(1);
        comment.setReportUid(uid);
        return BaseResult.successOrError(updateById(comment));
    }

}
