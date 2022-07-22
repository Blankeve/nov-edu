package com.novedu.nov.ucenter.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.novedu.nov.ucenter.entity.vo.AclPermissionVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 权限
 * </p>
 *
 * @author juam
 * @since 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="AclPermission对象", description="权限")
public class AclPermission implements Serializable , Comparable<AclPermission>{

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "所属上级")
    private Long parentId;

    @NotBlank(message = "权限名称不能为空")
    @ApiModelProperty(value = "权限名称")
    private String title;

    @ApiModelProperty(value = "权限组件")
    private String name;

    @NotNull(message = "权限类型不能为空")
    @ApiModelProperty(value = "类型(1:菜单,2:按钮)")
    private Integer type;

    @ApiModelProperty(value = "权限值")
    private String value;

    @NotBlank(message = "访问路径不能为空")
    @ApiModelProperty(value = "访问路径")
    private String path;

    @NotBlank(message = "组件路径不能为空")
    @ApiModelProperty(value = "组件路径")
    private String component;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "状态(0:禁止,1:正常)")
    private Integer status;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Integer isDeleted;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @TableField(exist = false)
    private List<AclPermission> children;

    private Integer sort;

    @Override
    public int compareTo(AclPermission o) {
        return this.getSort() - o.getSort();
    }
}
