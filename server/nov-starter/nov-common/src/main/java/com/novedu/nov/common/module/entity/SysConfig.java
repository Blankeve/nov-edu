package com.novedu.nov.common.module.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 *
 * </p>
 *
 * @author juam
 * @since 2022-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SysConfig对象", description = "")
public class SysConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "配置id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotEmpty(message = "配置键不能为空")
    @ApiModelProperty(value = "配置键")
    private String configKey;

    @NotEmpty(message = "配置值不能为空")
    @ApiModelProperty(value = "配置值")
    private String configValue;

    @NotEmpty(message = "配置名称不能为空")
    @ApiModelProperty(value = "配置名称")
    private String configName;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


    @ApiModelProperty(value = "1已删除 0未删除")
    private Integer isDeleted;

    @JsonSerialize(using = ToStringSerializer.class)
    private Integer status;
    private Integer parentId;
    private Integer grade;
    @TableField(exist = false)
    private boolean hasChildren;
    @TableField(exist = false)
    private List<SysConfig> children;
}
