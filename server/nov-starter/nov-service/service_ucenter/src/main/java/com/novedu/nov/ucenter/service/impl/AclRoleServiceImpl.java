package com.novedu.nov.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.base.RoleType;
import com.novedu.nov.system.service.SysConfigService;
import com.novedu.nov.ucenter.client.OpenEduService;
import com.novedu.nov.ucenter.entity.AclRole;
import com.novedu.nov.ucenter.entity.AclUser;
import com.novedu.nov.ucenter.entity.AclUserRole;
import com.novedu.nov.ucenter.entity.dto.AssignUserRoleForm;
import com.novedu.nov.ucenter.mapper.AclRoleMapper;
import com.novedu.nov.ucenter.service.AclRoleService;
import com.novedu.nov.ucenter.service.AclUserRoleService;
import com.novedu.nov.ucenter.service.AclUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-02-22
 */
@Service
public class AclRoleServiceImpl extends ServiceImpl<AclRoleMapper, AclRole> implements AclRoleService {

    @Autowired
    AclUserRoleService userRoleService;

    @Autowired
    OpenEduService openEduService;

    @Autowired
    AclUserService userService;

    @Autowired
    SysConfigService configService;

    @Override
    public BaseResult queryRoleList() {
        return BaseResult.success(list());
    }

    @Override
    public BaseResult queryRolePage(Page page) {
        return BaseResult.success(page(page, null));
    }

    @Override
    public BaseResult saveOrUpdateRole(AclRole role) {
        if (role.getId() == null) {
            if (query().eq("name", role.getName()).count() > 0)
                return BaseResult.error("该角色名已被使用");
            if (query().eq("code", role.getCode()).count() > 0)
                return BaseResult.error("该角色编码已被使用");
        } else {
            AclRole aclRole = getById(role);
            if (!aclRole.getName().equals(role.getName()) && query().eq("name", role.getName()).count() > 0)
                return BaseResult.error("该角色名已被使用");
            if (!aclRole.getCode().equals(role.getCode()) && query().eq("code", role.getCode()).count() > 0)
                return BaseResult.error("该角色编码已被使用");
        }
        return BaseResult.successOrError(saveOrUpdate(role));
    }

    @Override
    public BaseResult removeRole(Long id) {
        return BaseResult.successOrError(removeById(id));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult assignRoleByUid(AssignUserRoleForm params) {
        Long uid = params.getUid();
        AclUser user = userService.getById(uid);
        Integer code = query().eq("id", params.getRoleId()).one().getCode();
        if (code == RoleType.TEACHER.getCode()) {
            BaseResult baseResult = openEduService.clearBind(uid + "");
            if (baseResult == null)
                return BaseResult.error("分配角色失败");
            user.setAvatar(configService.getSysConfigByKey("teacher_def_avatar").getData().getConfigValue());
        } else if (code == RoleType.ADMIN.getCode()) {
            user.setAvatar(configService.getSysConfigByKey("admin_def_avatar").getData().getConfigValue());
        } else if (code == RoleType.STUDENT.getCode()) {
            user.setAvatar(configService.getSysConfigByKey("stu_def_avatar").getData().getConfigValue());
        } else
            user.setAvatar(configService.getSysConfigByKey("other_def_avatar").getData().getConfigValue());
        userService.updateById(user);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", uid);
        userRoleService.remove(queryWrapper);
        AclUserRole userRole = new AclUserRole();
        BeanUtils.copyProperties(params, userRole);
        return BaseResult.successOrError(userRoleService.save(userRole));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult queryUserRole(Long uid) {
        AclUserRole userRole = userRoleService.query().eq("uid", uid).one();
        AclRole role = query().eq("id", userRole.getRoleId()).one();
        return BaseResult.success(role);
    }
}
