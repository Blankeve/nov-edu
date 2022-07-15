package com.novedu.nov.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.constants.AuthConstant;
import com.novedu.nov.common.util.TreeUtils;
import com.novedu.nov.ucenter.component.InitRolePermissionHandler;
import com.novedu.nov.ucenter.entity.AclPermission;
import com.novedu.nov.ucenter.entity.AclRolePermission;
import com.novedu.nov.ucenter.entity.dto.AssignRolePermissionForm;
import com.novedu.nov.ucenter.mapper.AclPermissionMapper;
import com.novedu.nov.ucenter.service.AclPermissionService;
import com.novedu.nov.ucenter.service.AclRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-02-22
 */
@Service
public class AclPermissionServiceImpl extends ServiceImpl<AclPermissionMapper, AclPermission> implements AclPermissionService {

    @Autowired
    AclPermissionMapper aclPermissionMapper;

    @Autowired
    AclRolePermissionService rolePermissionService;

    @Autowired
    InitRolePermissionHandler rolePermissionHandler;

    @Override
    public BaseResult saveOrUpdatePermission(AclPermission permission) {
        if (AuthConstant.BUTTON_PERMISSION_TYPE == permission.getType() && permission.getTitle().indexOf("【") == -1)
            permission.setTitle("【操作】" + permission.getTitle());
        boolean res = saveOrUpdate(permission);
        rolePermissionHandler.InitRolePermission();
        return BaseResult.successOrError(res);
    }

    @Override
    public BaseResult removePermission(Long id) {
        boolean res = removeById(id);
        rolePermissionHandler.InitRolePermission();
        return BaseResult.successOrError(res);
    }

    @Override
    public BaseResult<List<AclPermission>> queryTree() {
        List permissions = list();
        return BaseResult.success(TreeUtils.toTree(permissions, AclPermission.class));
    }

    @Override
    public BaseResult queryPermissionByRoleId(Long id) {
        return BaseResult.success(aclPermissionMapper.queryPermissionByRoleId(id));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult assignRolePermission(AssignRolePermissionForm params) {
        Long id = params.getId();
        Long[] checkMenuIds = params.getCheckMenu();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("role_id", id);
        rolePermissionService.remove(queryWrapper);
        if (checkMenuIds.length == 0)
            return BaseResult.success();
        List<AclRolePermission> rolePermissions = new ArrayList<>();
        for (int i = 0; i < checkMenuIds.length; i++) {
            AclRolePermission rolePermission = new AclRolePermission();
            rolePermission.setRoleId(id);
            rolePermission.setPermissionId(checkMenuIds[i]);
            rolePermissions.add(rolePermission);
        }
        boolean res = rolePermissionService.saveBatch(rolePermissions);
        rolePermissionHandler.InitRolePermission();
        return BaseResult.successOrError(res);
    }


}
