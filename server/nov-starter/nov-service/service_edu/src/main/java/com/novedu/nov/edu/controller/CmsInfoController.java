package com.novedu.nov.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.annotation.UserMultiSubmitLimit;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.edu.entity.CmsInfo;
import com.novedu.nov.edu.entity.vo.CmsInfoVO;
import com.novedu.nov.edu.service.CmsInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author juam
 * @since 2022-05-31
 */
@RestController
@RequestMapping("/edu/info")
public class CmsInfoController {

    @Autowired
    private CmsInfoService cmsInfoService;

    @UserMultiSubmitLimit
    @PostMapping("/save")
    public BaseResult saveOrUpdate(@Validated @RequestBody CmsInfoVO cmsInfoVO) {
            return cmsInfoService.saveOrUpdateInfo(cmsInfoVO);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{ids}")
    public BaseResult remove(@PathVariable Long[] ids) {
        return BaseResult.successOrError(cmsInfoService.removeByIds(Arrays.asList(ids)));
    }
    @GetMapping("/export")
    public void exportCoursePage(HttpServletResponse response, CmsInfo cmsInfo) {
        ExcelUtils.exportExcel(cmsInfoService.queryPage(new Page(1, -1), cmsInfo).getRecords(), "文章列表", "文章列表", CmsInfoVO.class, "文章列表", response);
    }
    @GetMapping("/page/whi")
    public BaseResult queryPage(Page page, CmsInfo cmsInfo) {
        return BaseResult.success(cmsInfoService.queryPage(page, cmsInfo));
    }

    @GetMapping("/detail/{id}")
    public BaseResult detail(@PathVariable String id) {
        return cmsInfoService.queryDetail(id);
    }

    @GetMapping("/detail/{id}/whi")
    public BaseResult queryDetail(@PathVariable String id) {
        return cmsInfoService.queryClientDetail(id);
    }
}

