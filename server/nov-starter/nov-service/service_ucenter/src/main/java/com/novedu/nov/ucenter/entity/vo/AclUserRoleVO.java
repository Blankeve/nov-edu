package com.novedu.nov.ucenter.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

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
@ApiModel(value="AclUser对象", description="会员表")
public class AclUserRoleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "会员id")
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @Excel(name = "角色名称", height = 20, width = 30, isImportField = "true_st",orderNum = "3")
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色编码")
    private String roleCode;

    @ApiModelProperty(value = "微信openid")
    private String openid;

    @Excel(name = "用户名", height = 20, width = 30, isImportField = "true_st",orderNum = "2")
    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @Excel(name = "用户昵称", height = 20, width = 30, isImportField = "true_st",orderNum = "1")
    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "性别 1 男，2 女")
    private Integer gender;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @Excel(name = "用户头像", height = 20, width = 30, isImportField = "true_st",orderNum = "1")
    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "用户签名")
    private String sign;

    @Excel(name = "用户状态", height = 20, width = 30,replace = {"正常_0", "异常_1"}, isImportField = "true_st",orderNum = "4")
    @ApiModelProperty(value = "是否禁用 1（true）已禁用，  0（false）未禁用")
    private Integer status;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Boolean isDeleted;

    @Excel(name = "注册时间", height = 20, width = 30, isImportField = "true_st",exportFormat = "yyyy-MM-dd HH:mm:ss",orderNum = "5")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @Excel(name = "最后登录时间", height = 20, width = 30, isImportField = "true_st",exportFormat = "yyyy-MM-dd HH:mm:ss",orderNum = "6")
    @ApiModelProperty(value = "最后登录时间")
    private Date lastLoginTime;

    @Excel(name = "最后登录ip", height = 20, width = 30, isImportField = "true_st",orderNum = "7")
    @ApiModelProperty(value = "最后登录ip")
    private String lastLoginIp;

    @TableField(exist = false)
    private Date startTime;

    @TableField(exist = false)
    private Date endTime;
}
