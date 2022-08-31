package com.novedu.nov.system.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.system.entity.SysOperLog;
import com.novedu.nov.system.entity.dto.SysOperLogDTO;
import com.novedu.nov.system.service.SysOperLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author juam
 * @since 2022-06-16
 */
@RestController
@RequestMapping("/ucenter/oper-log")
public class SysOperLogController {

    @Autowired
    SysOperLogService sysOperLogService;

    @GetMapping("/page")
    public BaseResult getOperLogPage(Page page, SysOperLogDTO sysOperLog) {
        return BaseResult.success(sysOperLogService.getOperLogPage(page,sysOperLog));
    }

    @GetMapping("/export")
    public void exportCoursePage(HttpServletResponse response, SysOperLogDTO sysOperLog) {
        ExcelUtils.exportExcel(sysOperLogService.getOperLogPage(new Page(1, -1), sysOperLog).getRecords(), "操作日志", "操作日志", SysOperLog.class, "操作日志", response);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{ids}")
    public BaseResult remove(@PathVariable Long[] ids) {
        return BaseResult.successOrError(sysOperLogService.removeByIds(Arrays.asList(ids)));
    }
}

