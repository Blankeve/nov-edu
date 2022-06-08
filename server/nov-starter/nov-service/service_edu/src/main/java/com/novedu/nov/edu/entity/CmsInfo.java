package com.novedu.nov.edu.entity;

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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author juam
 * @since 2022-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CmsInfo对象", description="")
public class CmsInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotNull(message = "资讯分类不能为空")
    @ApiModelProperty(value = "资讯分类  ")
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer cate;

    @NotEmpty(message = "资讯标题不能为空")
    @ApiModelProperty(value = "资讯标题")
    private String title;

    @NotEmpty(message = "资讯标题不能为空")
    @ApiModelProperty(value = "资讯内容")
    private String content;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "创建人")
    private Long creater;

    @ApiModelProperty(value = "创建人昵称")
    @TableField(exist = false)
    private String createrNickname;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "更新人")
    private Long updater;

    @ApiModelProperty(value = "逻辑删除")
    private Integer isDeleted;

    @TableField(exist = false)
    private Date startTime;

    @TableField(exist = false)
    private Date endTime;
}
