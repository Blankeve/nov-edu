package com.novedu.nov.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.edu.entity.CmsConsult;
import com.novedu.nov.edu.entity.CmsNotice;
import com.novedu.nov.edu.service.CmsConsultService;
import com.novedu.nov.edu.service.CmsNoticeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author juam
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/edu/consult")
public class CmsConsultController {

    @Autowired
    private CmsConsultService cmsConsultService;

    @PostMapping("/save")
    public BaseResult saveConsult(@Validated CmsConsult cmsConsult) {
        cmsConsult.setUid(RequestUtils.getUid());
        return BaseResult.successOrError(cmsConsultService.save(cmsConsult));
    }

    @PutMapping("/update")
    public BaseResult updateConsult(CmsConsult cmsConsult) {
        if(StringUtils.isEmpty(cmsConsult.getReplyContent())){
            return BaseResult.error("回复内容不能为空");
        }
        return cmsConsultService.update(cmsConsult);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{id}")
    public BaseResult removeNotice(@PathVariable String id) {
        return BaseResult.successOrError(cmsConsultService.removeById(id));
    }

    @PostMapping("/page")
    public BaseResult queryNoticePage(Page page, CmsConsult cmsConsult) {
        return cmsConsultService.queryPage(page, cmsConsult);
    }

}

