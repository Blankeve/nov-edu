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
 * @author ：juam
 * @date ：2022/1/26 13:26
 * @description：
 * @modified By：
 * @version:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="EduComment对象", description="评论")
public class EduUserCommentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "评论id", height = 20, width = 30, isImportField = "true_st",orderNum = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "讲师ID")
    @TableId(value = "评论id", type = IdType.NONE)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程id")
    private Long courseId;

    @Excel(name = "评论课程", height = 20, width = 30, isImportField = "true_st",orderNum = "3")
    @ApiModelProperty(value = "评论课程")
    private String courseTitle;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "讲师id")
    private Long teacherId;

    @Excel(name = "课程讲师", height = 20, width = 30, isImportField = "true_st",orderNum = "4")
    @ApiModelProperty(value = "课程讲师")
    private String teacherName;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "会员id")
    private Long uid;

    @Excel(name = "用户昵称", height = 20, width = 30, isImportField = "true_st",orderNum = "2")
    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @Excel(name = "评论内容", height = 20, width = 30, isImportField = "true_st",orderNum = "5")
    @ApiModelProperty(value = "评论内容")
    private String content;

    @Excel(name = "评论时间", height = 20, width = 30, isImportField = "true_st",exportFormat = "yyyy-MM-dd HH:mm:ss",orderNum = "7")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Excel(name = "更新时间", height = 20, width = 30, isImportField = "true_st",exportFormat = "yyyy-MM-dd HH:mm:ss",orderNum = "8")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    private Integer isDeleted;

    @Excel(name = "评论星级", height = 20, width = 30, isImportField = "true_st",orderNum = "6")
    @ApiModelProperty(value = "评论星级")
    private Integer mark;

    private Integer reported;
}
