package com.novedu.nov.ucenter.controller;


import com.novedu.nov.common.annotation.UserMultiSubmitLimit;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.ucenter.entity.AclPermission;
import com.novedu.nov.ucenter.entity.dto.AssignRolePermissionForm;
import com.novedu.nov.ucenter.service.AclPermissionService;
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

    @UserMultiSubmitLimit
    @PostMapping("/save")
    public BaseResult saveOrUpdate(@Validated @RequestBody AclPermission permission) {
        return permissionService.saveOrUpdatePermission(permission);
    }

    @ApiOperation("查询角色权限")
    @GetMapping("/role/{id}")
    public BaseResult queryPermissionByRoleId(@PathVariable Long id) {
        return permissionService.queryPermissionByRoleId(id);
    }

    @ApiOperation("分配角色权限")
    @PostMapping("/save-role-sel")
    public BaseResult assignRolePermission(@RequestBody AssignRolePermissionForm params) {
        return permissionService.assignRolePermission(params);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{id}")
    public BaseResult remove(@PathVariable Long id) {
        return BaseResult.error("演示模式下暂不支持删除菜单");
        // return permissionService.removePermission(id);
    }

    @GetMapping("/tree")
    public BaseResult queryTree() {
        return permissionService.queryTree();
    }

}

