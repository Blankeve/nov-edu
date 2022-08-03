package com.novedu.nov.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.annotation.UserMultiSubmitLimit;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.edu.entity.CmsInfo;
import com.novedu.nov.edu.entity.CmsInfoDetail;
import com.novedu.nov.edu.entity.vo.CmsInfoVO;
import com.novedu.nov.edu.service.CmsInfoDetailService;
import com.novedu.nov.edu.service.CmsInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @DeleteMapping("/remove/{id}")
    public BaseResult remove(@PathVariable String id) {
        return BaseResult.successOrError(cmsInfoService.removeById(id));
    }

    @GetMapping("/page")
    public BaseResult queryPage(Page page, CmsInfo cmsInfo) {
        return cmsInfoService.queryPage(page, cmsInfo);
    }

    @GetMapping("/detail/{id}")
    public BaseResult detail(@PathVariable String id) {
        return cmsInfoService.getDetail(id);
    }

    @GetMapping("/detail-client/{id}")
    public BaseResult getDetail(@PathVariable String id) {
        return cmsInfoService.getClientDetail(id);
    }
}

