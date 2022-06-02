package com.novedu.nov.ucenter.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author juam
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "AclUser对象", description = "会员表")
public class AclUserProfileDTO {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "会员id")
    @TableId(value = "id", type = IdType.NONE)
    private Long id;


    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^1[3|4|5|7|8]\\d{9}$",message = "手机号格式不正确")
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @NotEmpty(message = "昵称不能为空")
    @Pattern(regexp = "^.{1,15}$",message = "昵称格式不正确,请限制在16个字符以内")
    @ApiModelProperty(value = "昵称")
    private String nickname;

    @NotEmpty(message = "头像不能为空")
    @ApiModelProperty(value = "用户头像")
    private String avatar;
}
