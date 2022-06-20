package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.base.RoleType;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.common.util.JwtUtils;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.edu.client.OrderClient;
import com.novedu.nov.edu.client.UserRoleClient;
import com.novedu.nov.edu.entity.*;
import com.novedu.nov.edu.entity.dto.EduUserCommentDTO;
import com.novedu.nov.edu.entity.vo.EduUserCommentVO;
import com.novedu.nov.edu.mapper.EduCommentMapper;
import com.novedu.nov.edu.service.EduCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.edu.service.EduCourseApplyService;
import com.novedu.nov.edu.service.EduCourseService;
import com.novedu.nov.edu.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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
    UserRoleClient userRoleClient;

    @Autowired
    EduTeacherService teacherService;

    @Autowired
    OrderClient orderClient;

    public boolean queryOrderByUidAndCourseId(Long id, Long uid) {
        BaseResult baseResult = orderClient.queryOrderByUidAndCourseId(id, uid);
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
    public BaseResult queryCommentPage(Page page, EduComment eduComment) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("course_id", eduComment.getCourseId());
        IPage<EduUserCommentVO> page1 = commentMapper.queryPage(page, queryWrapper);
        for (EduUserCommentVO record : page1.getRecords()) {
            if (!StringUtils.hasText(record.getNickname()))
                record.setNickname("已注销");
        }
        return BaseResult.success(page1);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult queryCommentPage(HttpServletRequest request, Page page, EduUserCommentDTO eduComment) {
        String token = request.getHeader("X-Token");
        String uid = JwtUtils.getAudience(token).get("uid");
        BaseResult baseResult = userRoleClient.queryUserRole(Long.valueOf(uid));
        if (baseResult == null) {
            return BaseResult.success();
        }
        Map role = (Map) baseResult.getData();
        Integer code = (Integer) role.get("code");
        if (code == RoleType.TEACHER.getCode()) {
            Long teacherId = teacherService.query().eq("uid", uid).one().getId();
            eduComment.setTeacherId(teacherId);
        }
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
        Date start = eduComment.getStartTime();
        Date end = eduComment.getEndTime();
        if (start != null && end != null && end.getTime() > start.getTime())
            queryWrapper.apply("comment.create_time > date_format({0},'%Y-%m-%d %H:%i:%s') and comment.create_time < date_format({1},'%Y-%m-%d %H:%i:%s')", start, end);
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


    @Override
    public void exportCommentPage(HttpServletResponse response, Page page, EduUserCommentDTO eduComment) {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        BaseResult baseResult = queryCommentPage(request, page, eduComment);
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            Page page1 = (Page) baseResult.getData();
            ExcelUtils.exportExcel(page1.getRecords(), "评论信息", "评论信息", EduUserCommentVO.class, "评论信息", response);
        }
    }

    @Override
    public void exportAll(HttpServletResponse response, EduUserCommentDTO eduComment) {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        BaseResult baseResult = queryCommentPage(request, new Page(1, count()), eduComment);
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            Page page1 = (Page) baseResult.getData();
            ExcelUtils.exportExcel(page1.getRecords(), "评论信息", "评论信息", EduUserCommentVO.class, "评论信息", response);
        }
    }

}
