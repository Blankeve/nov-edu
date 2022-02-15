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
public class EduChapterInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;


    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程讲师ID")
    private Long teacherId;

    @Excel(name = "课程讲师", height = 20, width = 30, isImportField = "true_st",orderNum = "3")
    @ApiModelProperty(value = "课程讲师")
    private String teacherName;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date subjectCreateTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date subjectUpdateTime;

    @ApiModelProperty(value = "课程ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long courseId;

    @Excel(name = "所属课程", height = 20, width = 30, isImportField = "true_st",orderNum = "2")
    @ApiModelProperty(value = "所属课程")
    private String courseTitle;

    @Excel(name = "视频数量", height = 20, width = 30, isImportField = "true_st",orderNum = "6")
    @ApiModelProperty(value = "视频数量")
    private Integer videoQty;

    @Excel(name = "章节ID", height = 20, width = 30, isImportField = "true_st",orderNum = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "章节ID")
    private Long chapterId;

    @Excel(name = "章节名称", height = 20, width = 30, isImportField = "true_st",orderNum = "4")
    @ApiModelProperty(value = "章节名称")
    private String chapterTitle;

    @Excel(name = "第几章节", height = 20, width = 30, isImportField = "true_st",orderNum = "5")
    @ApiModelProperty(value = "第几章节")
    private Integer chapterSort;

    @Excel(name = "创建时间", height = 20, width = 30, isImportField = "true_st",exportFormat = "yyyy-MM-dd HH:mm:ss",orderNum = "7")
    @ApiModelProperty(value = "创建时间")
    private Date chapterCreateTime;


    @Excel(name = "更新时间", height = 20, width = 30, isImportField = "true_st",exportFormat = "yyyy-MM-dd HH:mm:ss",orderNum = "8")
    @ApiModelProperty(value = "更新时间")
    private Date chapterUpdateTime;


}
