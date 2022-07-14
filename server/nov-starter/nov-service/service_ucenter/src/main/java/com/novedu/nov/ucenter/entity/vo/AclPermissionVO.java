package com.novedu.nov.ucenter.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
public class AclPermissionVO implements Serializable , Comparable<AclPermissionVO>{

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "所属上级")
    private Long parentId;

    @ApiModelProperty(value = "访问路径")
    private String path;

    @NotBlank(message = "组件路径不能为空")
    @ApiModelProperty(value = "组件路径")
    private String component;

    private Map meta;

    @ApiModelProperty(value = "状态(0:禁止,1:正常)")
    private boolean hidden;

    @TableField(exist = false)
    private List<AclPermissionVO> children;

    private Integer sort;


    @Override
    public int compareTo(AclPermissionVO o) {
        return this.getSort() - o.getSort();
    }
}
