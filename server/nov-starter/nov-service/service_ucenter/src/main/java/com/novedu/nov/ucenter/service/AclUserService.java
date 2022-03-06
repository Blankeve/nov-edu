package com.novedu.nov.ucenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.ucenter.entity.AclUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.ucenter.entity.dto.AclUserRoleDTO;
import com.novedu.nov.ucenter.entity.vo.AclUserRoleVO;

import javax.servlet.http.HttpServletResponse;
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

    BaseResult<List<AclUserRoleVO>> queryUserPage(Page page, AclUserRoleDTO user);

    BaseResult loginBg(AclUser user);

    BaseResult getInfoBg(String token);

    BaseResult resetPwd(Long uid);

    void exportUserPage(HttpServletResponse response, Page page, AclUserRoleDTO user);

    void exportAll(HttpServletResponse response);

    BaseResult getDashBoardInfo();

    BaseResult syncRegisterLoginCount();
}
