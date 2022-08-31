package com.novedu.nov.system.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.system.entity.SysLoginHistory;
import com.novedu.nov.system.service.SysLoginHistoryService;
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
 * @since 2022-05-18
 */
@RestController
@RequestMapping("/ucenter/sys-login-history")
public class SysLoginHistoryController {

    @Autowired
    SysLoginHistoryService sysLoginHistoryService;

    @GetMapping("/page")
    public BaseResult queryLoginHistoryPage(Page page, SysLoginHistory loginHistory) {
        return BaseResult.success(sysLoginHistoryService.queryLoginHistoryPage(page, loginHistory));
    }

    @GetMapping("/export")
    public void exportCoursePage(HttpServletResponse response, SysLoginHistory loginHistory) {
        ExcelUtils.exportExcel(sysLoginHistoryService.queryLoginHistoryPage(new Page(1, -1), loginHistory).getRecords(), "历史登录", "历史登录", SysLoginHistory.class, "历史登录", response);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{ids}")
    public BaseResult remove(@PathVariable Long[] ids) {
        return BaseResult.successOrError(sysLoginHistoryService.removeByIds(Arrays.asList(ids)));
    }
}

