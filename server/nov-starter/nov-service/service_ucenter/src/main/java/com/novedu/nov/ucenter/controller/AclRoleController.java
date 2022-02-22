package com.novedu.nov.ucenter.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.ucenter.entity.AclRole;
import com.novedu.nov.ucenter.service.AclRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
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
    public BaseResult saveOrUpdateRole(@RequestBody AclRole role) {
        return roleService.saveOrUpdateRole(role);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{id}")
    public BaseResult removeRole(@PathVariable Long id) {
        return roleService.removeRole(id);
    }

    @GetMapping("/list")
    public BaseResult queryRoleList() {
        return roleService.queryRoleList();
    }

    @PostMapping("/page")
    public BaseResult queryRolePage(Page page, AclRole role) {
        return roleService.queryRolePage(page, role);
    }
}

