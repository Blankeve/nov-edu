package com.novedu.nov.edu.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 讲师
 * </p>
 *
 * @author juam
 * @since 2021-12-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="EduTeacher对象", description="讲师")
public class EduTeacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "讲师id", height = 20, width = 30, isImportField = "true_st",orderNum = "1")
    @ApiModelProperty(value = "讲师ID")
    @TableId(value = "id", type = IdType.NONE)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Excel(name = "讲师姓名", height = 20, width = 30, isImportField = "true_st",orderNum = "2")
    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "讲师姓名")
    private String name;

    @Excel(name = "讲师简介", height = 20, width = 30, isImportField = "true_st",orderNum = "3")
    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "讲师简介")
    private String intro;

    @Excel(name = "讲师资历", height = 20, width = 30, isImportField = "true_st",orderNum = "4")
    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "讲师资历,一句话说明讲师")
    private String career;

    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "头衔 1高级讲师 2首席讲师")
    private Integer level;

    @ApiModelProperty(value = "讲师头像")
    private String avatar;

    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "入驻时间")
    private Date joinDate;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Integer isDeleted;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
