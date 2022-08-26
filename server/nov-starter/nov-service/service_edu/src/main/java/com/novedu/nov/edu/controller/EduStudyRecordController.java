package com.novedu.nov.edu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.edu.entity.dto.EduStudyRecordDTO;
import com.novedu.nov.edu.entity.vo.EduVideoInfoVO;
import com.novedu.nov.edu.service.EduStudyRecordService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author juam
 * @since 2022-8-26
 */
@Api("学习记录管理的接口文档")
@RestController
@RequestMapping("/edu/study")
public class EduStudyRecordController {

    @Autowired
    EduStudyRecordService studyRecordService;

    @GetMapping("/study/record/page")
    public BaseResult queryStudyRecordPage(Page page, EduStudyRecordDTO studyRecordDTO) {
        return BaseResult.success(studyRecordService.queryStudyRecordPage(page, studyRecordDTO));
    }

    @GetMapping("/export")
    public void exportCoursePage(HttpServletResponse response, EduStudyRecordDTO studyRecordDTO) {
        ExcelUtils.exportExcel(studyRecordService.queryStudyRecordPage(new Page(1, -1), studyRecordDTO).getRecords(), "学习记录", "学习记录", EduVideoInfoVO.class, "学习记录", response);
    }
}
