package com.novedu.nov.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import com.novedu.nov.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author juam
 * @since 2022-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysOperLog对象", description="")
public class SysOperLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "操作人")
    private String operName;

    @ApiModelProperty(value = "操作ip")
    private String operIp;

    @ApiModelProperty(value = "操作地址")
    private String operAddr;

    @ApiModelProperty(value = "请求地址")
    private String reqUrl;

    @ApiModelProperty(value = "请求方式")
    private String method;

    @ApiModelProperty(value = "请求类")
    private String reqClass;

    @ApiModelProperty(value = "请求方法")
    private String reqMethod;

    @ApiModelProperty(value = "请求参数")
    private String reqArgs;

    @ApiModelProperty(value = "请求状态")
    private String reqStatus;

    @ApiModelProperty(value = "请求结果")
    private String reqResult;

    @ApiModelProperty(value = "请求耗时")
    private Long reqTimeSpend;

    @ApiModelProperty(value = "请求时间")
    private Date reqTime;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
