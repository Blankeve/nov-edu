package com.novedu.nov.edu.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ：juam
 * @date ：2021/12/21 9:18
 * @description：
 * @modified By：
 * @version:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "EduCourseInfoVO对象", description = "课程信息")
public class EduCourseInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程讲师ID")
    private Long teacherId;

    @Excel(name = "课程讲师", height = 20, width = 30, isImportField = "true_st",orderNum = "4")
    @ApiModelProperty(value = "课程讲师")
    private String teacherName;

    private String teacherAvatar;

    private String teacherCareer;

    @ApiModelProperty(value = "课程类别ID")
    private Integer subjectId;

    @ApiModelProperty(value = "课程类别ID")
    private Integer[] subjectIds;

    @Excel(name = "课程类别", height = 20, width = 30, isImportField = "true_st",orderNum = "1")
    @ApiModelProperty(value = "课程类别")
    private String subjectTitle;

    @ApiModelProperty(value = "排序字段")
    private Integer subjectSort;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date subjectCreateTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date subjectUpdateTime;

    @Excel(name = "课程ID", height = 20, width = 30, isImportField = "true_st",orderNum = "2")
    @ApiModelProperty(value = "课程ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long courseId;

    @Excel(name = "课程标题", height = 20, width = 30, isImportField = "true_st",orderNum = "3")
    @ApiModelProperty(value = "课程标题")
    private String courseTitle;

    @Excel(name = "课程价格", height = 20, width = 30, isImportField = "true_st",orderNum = "4")
    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private BigDecimal coursePrice;

    @Excel(name = "总课时", height = 20, width = 30, isImportField = "true_st",orderNum = "5")
    @ApiModelProperty(value = "总课时")
    private Integer courseLessonNum;

    @Excel(name = "课程封面", height = 20, width = 30, isImportField = "true_st",orderNum = "6")
    @ApiModelProperty(value = "课程封面图片路径")
    private String courseCover;

    @Excel(name = "销售数量", height = 20, width = 30, isImportField = "true_st",orderNum = "7")
    @ApiModelProperty(value = "销售数量")
    private Long courseBuyCount;

    @Excel(name = "播放数量", height = 20, width = 30, isImportField = "true_st",orderNum = "9")
    @ApiModelProperty(value = "播放数量")
    private Long courseViewCount;

    @Excel(name = "评论数量", height = 20, width = 30, isImportField = "true_st",orderNum = "10")
    @ApiModelProperty(value = "评论数量")
    private Long courseCommentCount;

    @Excel(name = "学习人数", height = 20, width = 30, isImportField = "true_st",orderNum = "8")
    @ApiModelProperty(value = "学习人数")
    private Integer courseApplyCount;

    @Excel(name = "课程状态", height = 20, width = 30,replace = {"已下架_0", "上架中_1"}, isImportField = "true_st",orderNum = "11")
    @ApiModelProperty(value = "课程状态 0未发布  1已发布")
    private Integer courseStatus;

    @Excel(name = "章节数量", height = 20, width = 30, isImportField = "true_st",orderNum = "12")
    @ApiModelProperty(value = "章节数量")
    private Integer chapterQty;

    @Excel(name = "视频数量", height = 20, width = 30, isImportField = "true_st",orderNum = "13")
    @ApiModelProperty(value = "视频数量")
    private Integer videoQty;

    @Excel(name = "创建时间", height = 20, width = 30, isImportField = "true_st",exportFormat = "yyyy-MM-dd HH:mm:ss",orderNum = "14")
    @ApiModelProperty(value = "创建时间")
    private Date courseCreateTime;

    @Excel(name = "更新时间", height = 20, width = 30, isImportField = "true_st",exportFormat = "yyyy-MM-dd HH:mm:ss",orderNum = "15")
    @ApiModelProperty(value = "更新时间")
    private Date courseUpdateTime;

    @ApiModelProperty(value = "课程简介")
    private String introId;

    @ApiModelProperty(value = "课程简介")
    private String introDescription;

}
