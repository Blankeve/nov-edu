package com.novedu.nov.ucenter.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.entity.UserDTO;
import com.novedu.nov.ucenter.entity.AclUser;
import com.novedu.nov.ucenter.entity.dto.AclUserDTO;
import com.novedu.nov.ucenter.entity.dto.AclUserPasswordDTO;
import com.novedu.nov.ucenter.entity.dto.AclUserProfileDTO;
import com.novedu.nov.ucenter.entity.dto.AclUserRoleDTO;
import com.novedu.nov.ucenter.entity.vo.AclUserRoleVO;

import java.util.List;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author juam
 * @since 2022-02-14
 */
public interface AclUserService extends IService<AclUser> {
    BaseResult login(AclUser user);

    BaseResult register(AclUser user);

    BaseResult getMemberInfo(Long id);

    IPage<List<AclUserRoleVO>> queryUserPage(Page page, AclUserRoleDTO user);

    BaseResult loginBg(AclUserDTO user);

    BaseResult getInfoBg(String token);

    BaseResult resetPwd(Long []uids);

    BaseResult getDashBoardInfo();

    BaseResult syncRegisterLoginCount();

    BaseResult updatePassword(AclUserPasswordDTO userPasswordDto);

    BaseResult updateProfile(AclUserProfileDTO userProfileDto);

    BaseResult syncUsersCache();

    BaseResult getInfoClient(String token);

    BaseResult getCode();

    UserDTO loadUserByUsername(String username);
}
