package com.novedu.nov.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.annotation.UserMultiSubmitLimit;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.EduComment;
import com.novedu.nov.edu.entity.dto.EduUserCommentDTO;
import com.novedu.nov.edu.service.EduCommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @UserMultiSubmitLimit
    @PostMapping("/save")
    public BaseResult saveComment(@RequestBody EduComment eduComment , HttpServletRequest request) {
        return eduCommentService.saveComment(eduComment,request);
    }

    @PostMapping("/export")
    public void exportCommentPage(HttpServletResponse response, Page page, EduUserCommentDTO eduComment) {
        eduCommentService.exportCommentPage(response,page, eduComment);
    }

    @GetMapping("/export-all")
    public void exportAll(HttpServletResponse response, EduUserCommentDTO eduComment) {
        eduCommentService.exportAll(response,eduComment);
    }

    @PostMapping("/page")
    public BaseResult queryCommentPage(HttpServletRequest request,Page page, EduUserCommentDTO eduComment) {
        return eduCommentService.queryCommentPage(request,page, eduComment);
    }

    @PostMapping("/page-client")
    public BaseResult queryCommentPage(Page page, EduUserCommentDTO eduComment) {
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

