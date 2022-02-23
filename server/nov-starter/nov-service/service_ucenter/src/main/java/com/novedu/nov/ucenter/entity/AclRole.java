package com.novedu.nov.ucenter.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 
 * </p>
 *
 * @author juam
 * @since 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="AclRole对象", description="")
public class AclRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "角色id")
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    @Pattern(regexp = "^\\S{1,10}$",message = "角色名称格式不正确")
    @ApiModelProperty(value = "角色名称")
    private String name;

    @NotNull(message = "角色编码不能为空")
    @Min(value = 0,message = "角色编码格式不正确")
    @Max(value = 20,message = "角色编码格式不正确")
    @ApiModelProperty(value = "角色编码")
    private Integer code;

    @Pattern(regexp = "^.{1,50}$",message = "备注格式不正确")
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "1:有效 0:无效")
    private Integer status;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
