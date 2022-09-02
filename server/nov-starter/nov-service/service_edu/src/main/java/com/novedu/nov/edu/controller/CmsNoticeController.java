package com.novedu.nov.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.annotation.UserMultiSubmitLimit;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.CmsNotice;
import com.novedu.nov.edu.service.CmsNoticeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 广告推荐 前端控制器
 * </p>
 *
 * @author juam
 * @since 2022-02-28
 */
@RestController
@RequestMapping("/edu/notice")
public class CmsNoticeController {

    @Autowired
    private CmsNoticeService cmsNoticeService;

    @UserMultiSubmitLimit
    @PostMapping("/save")
    public BaseResult saveOrUpdateNotice(@Validated @RequestBody CmsNotice cmsNotice) {
        return cmsNoticeService.saveOrUpdateNotice(cmsNotice);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{id}")
    public BaseResult removeNotice(@PathVariable String id) {
        return cmsNoticeService.removeNotice(id);
    }

    @PostMapping("/page")
    public BaseResult queryNoticePage(Page page,@RequestBody CmsNotice cmsNotice) {
        return cmsNoticeService.queryNoticePage(page, cmsNotice);
    }

    @GetMapping("/receive/whi")
    public BaseResult receiveNotice(String id) {
        return cmsNoticeService.receiveNotice(id);
    }
}

