package com.novedu.nov.edu.entity.vo;

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
 *
 * </p>
 *
 * @author juam
 * @since 2022-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CmsInfo对象", description = "")
public class CmsInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "文章分类")
    private Integer cate;

    @Excel(name = "文章分类", height = 20, width = 30, isImportField = "true_st", orderNum = "1")
    @ApiModelProperty(value = "文章分类")
    private String catename;

    @ApiModelProperty(value = "用户名")
    private String username;


    @Excel(name = "文章标题", height = 20, width = 60, isImportField = "true_st", orderNum = "1")
    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "资讯内容")
    private String content;

    @Excel(name = "点击量", height = 20, width = 30, isImportField = "true_st", orderNum = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "点击量")
    private long clickCount;

    @Excel(name = "作者", height = 20, width = 30, isImportField = "true_st", orderNum = "1")
    @ApiModelProperty(value = "用户昵称")
    private String createrNickname;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "创建人")
    private Long creater;

    @Excel(name = "发布时间", height = 20, width = 30, isImportField = "true_st", exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "1")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Excel(name = "更新时间", height = 20, width = 30, isImportField = "true_st", exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "1")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "更新人")
    private Long updater;

    @ApiModelProperty(value = "更新人昵称")
    private String updaterNickname;

    @ApiModelProperty(value = "逻辑删除")
    private Integer isDeleted;

}
