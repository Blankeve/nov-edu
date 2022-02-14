package com.novedu.nov.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduComment;
import com.novedu.nov.edu.entity.dto.EduUserCommentDTO;
import com.novedu.nov.edu.entity.vo.EduUserCommentVO;
import com.novedu.nov.edu.service.EduCommentService;
import com.novedu.nov.edu.service.impl.EduCommentServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author juam
 * @since 2022-01-26
 */
@RestController
@RequestMapping("/edu/comment")
public class EduCommentController {

    @Autowired
    EduCommentService eduCommentService;

    @PostMapping("/save")
    public BaseResult saveComment(@RequestBody EduComment eduComment , HttpServletRequest request) {
        return eduCommentService.saveComment(eduComment,request);
    }

    @PostMapping("/page")
    public BaseResult queryCommentPage(Page page, EduUserCommentDTO eduComment) {
        return eduCommentService.queryCommentPage(page, eduComment);
    }

    @PostMapping("/page-client")
    public BaseResult queryCommentPage(Page page, EduComment eduComment) {
        return eduCommentService.queryCommentPage(page, eduComment);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{id}")
    public BaseResult removeComment(@PathVariable Long id) {
        return eduCommentService.removeComment(id);
    }

    @ApiOperation("举报")
    @PutMapping("/report/{id}")
    public BaseResult reportComment(@PathVariable Long id,HttpServletRequest request) {
        return eduCommentService.reportComment(id,request);
    }
}

