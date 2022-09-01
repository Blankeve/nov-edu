package com.novedu.nov.system.entity;

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

import java.util.Date;

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
@ApiModel(value = "SysOperLog对象", description = "")
public class SysOperLog {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @Excel(name = "操作人", height = 20, width = 30, isImportField = "true_st",orderNum = "3")
    @ApiModelProperty(value = "操作人")
    private String operName;

    @Excel(name = "操作ip", height = 20, width = 30, isImportField = "true_st",orderNum = "3")
    @ApiModelProperty(value = "操作ip")
    private String operIp;

    @Excel(name = "操作地址", height = 20, width = 30, isImportField = "true_st",orderNum = "3")
    @ApiModelProperty(value = "操作地址")
    private String operAddr;

    @Excel(name = "请求地址", height = 20, width = 30, isImportField = "true_st",orderNum = "3")
    @ApiModelProperty(value = "请求地址")
    private String reqUrl;

    @Excel(name = "请求方式", height = 20, width = 30, isImportField = "true_st",orderNum = "3")
    @ApiModelProperty(value = "请求方式")
    private String method;

    @Excel(name = "请求类", height = 20, width = 30, isImportField = "true_st",orderNum = "3")
    @ApiModelProperty(value = "请求类")
    private String reqClass;

    @Excel(name = "请求方法", height = 20, width = 30, isImportField = "true_st",orderNum = "3")
    @ApiModelProperty(value = "请求方法")
    private String reqMethod;

    @Excel(name = "请求参数", height = 20, width = 30, isImportField = "true_st",orderNum = "3")
    @ApiModelProperty(value = "请求参数")
    private String reqArgs;

    @ApiModelProperty(value = "请求状态")
    private String reqStatus;

    @Excel(name = "请求结果", height = 20, width = 30, isImportField = "true_st",orderNum = "3")
    @ApiModelProperty(value = "请求结果")
    private String reqResult;

    @Excel(name = "请求耗时(ms)", height = 20, width = 30, isImportField = "true_st",orderNum = "3")
    @ApiModelProperty(value = "请求耗时")
    private Long reqTimeSpend;

    @Excel(name = "请求时间", height = 20, width = 30, isImportField = "true_st",exportFormat = "yyyy-MM-dd HH:mm:ss",orderNum = "5")
    @ApiModelProperty(value = "请求时间")
    private Date reqTime;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
