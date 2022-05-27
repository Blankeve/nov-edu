package com.novedu.nov.ucenter.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.ucenter.entity.AclRole;
import com.novedu.nov.ucenter.entity.dto.AssignUserRoleForm;
import com.novedu.nov.ucenter.service.AclRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author juam
 * @since 2022-02-22
 */
@RestController
@RequestMapping("/ucenter/role")
public class AclRoleController {

    @Autowired
    AclRoleService roleService;

    @PostMapping("/save")
    public BaseResult saveOrUpdateRole(@Validated @RequestBody AclRole role) {
        return roleService.saveOrUpdateRole(role);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{id}")
    public BaseResult removeRole(@PathVariable Long id) {
        return BaseResult.error("演示模式下暂不支持删除角色");
        // return roleService.removeRole(id);
    }


    @ApiOperation("查询用户角色")
    @PostMapping("/by-uid/{uid}")
    public BaseResult queryUserRole(@PathVariable Long uid) {
        return roleService.queryUserRole(uid);
    }

    @ApiOperation("分配用户角色")
    @PostMapping("/assign-role-uid")
    public BaseResult assignRoleByUid(@RequestBody AssignUserRoleForm params) {
        return roleService.assignRoleByUid(params);
    }

    @GetMapping("/list")
    public BaseResult queryRoleList() {
        return roleService.queryRoleList();
    }

    @PostMapping("/page")
    public BaseResult queryRolePage(Page page) {
        return roleService.queryRolePage(page);
    }
}

