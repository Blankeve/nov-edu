package com.novedu.nov.ucenter.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.ucenter.entity.AclPermission;
import com.novedu.nov.ucenter.entity.AclRole;
import com.novedu.nov.ucenter.service.AclPermissionService;
import com.novedu.nov.ucenter.service.AclRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 权限 前端控制器
 * </p>
 *
 * @author juam
 * @since 2022-02-22
 */
@RestController
@RequestMapping("/ucenter/permission")
public class AclPermissionController {

    @Autowired
    AclPermissionService permissionService;

    @PostMapping("/save")
    public BaseResult saveOrUpdate(@Validated @RequestBody AclPermission permission) {
        return permissionService.saveOrUpdatePermission(permission);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{id}")
    public BaseResult remove(@PathVariable Long id) {
        return permissionService.removePermission(id);
    }

    @GetMapping("/tree")
    public BaseResult queryTree() {
        return permissionService.queryTree();
    }

}

