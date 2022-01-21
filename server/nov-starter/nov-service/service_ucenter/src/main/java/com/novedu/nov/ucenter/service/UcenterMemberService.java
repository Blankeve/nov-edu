package com.novedu.nov.ucenter.service;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.ucenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.ucenter.entity.dto.UcenterMemberDto;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author juam
 * @since 2022-01-19
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    BaseResult login(UcenterMember ucenterMemberDt);

    BaseResult register(UcenterMember ucenterMemberDto);
}
