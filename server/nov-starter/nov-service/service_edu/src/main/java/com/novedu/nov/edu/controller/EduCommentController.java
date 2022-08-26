package com.novedu.nov.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.annotation.UserMultiSubmitLimit;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.edu.entity.EduComment;
import com.novedu.nov.edu.entity.dto.EduUserCommentDTO;
import com.novedu.nov.edu.entity.vo.EduUserCommentVO;
import com.novedu.nov.edu.service.EduCommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

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

    @GetMapping("/export")
    public void exportCommentPage(HttpServletResponse response, EduUserCommentDTO eduComment) {
        ExcelUtils.exportExcel(eduCommentService.queryCommentPage(new Page(1, -1), eduComment).getRecords(), "评论信息", "评论信息", EduUserCommentVO.class, "评论信息", response);
    }

    @GetMapping("/page")
    public BaseResult queryCommentPage(Page page, EduUserCommentDTO eduComment) {
        return BaseResult.success(eduCommentService.queryCommentPage(page, eduComment));
    }

    @GetMapping("/page-client")
    public BaseResult queryClientCommentPage(Page page, EduUserCommentDTO eduComment) {
        return eduCommentService.queryClientCommentPage(page, eduComment);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{ids}")
    public BaseResult remove(@PathVariable Long[] ids) {
        return BaseResult.successOrError(eduCommentService.removeByIds(Arrays.asList(ids)));
    }

    @ApiOperation("举报")
    @PutMapping("/report/{id}")
    public BaseResult reportComment(@PathVariable Long id,HttpServletRequest request) {
        return eduCommentService.reportComment(id,request);
    }
}

