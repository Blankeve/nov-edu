package com.novedu.nov.edu.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


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
public class EduStudyRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程讲师ID")
    private Long teacherId;

    @ApiModelProperty(value = "课程讲师")
    private String teacherName;

    private String teacherAvatar;

    private String teacherCareer;

    @Excel(name = "用户昵称", height = 20, width = 30, isImportField = "true_st", orderNum = "1")
    private String nickname;

    @ApiModelProperty(value = "课程ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long courseId;

    @Excel(name = "课程名称", height = 20, width = 30, isImportField = "true_st", orderNum = "1")
    @ApiModelProperty(value = "课程标题")
    private String courseTitle;

    @Excel(name = "课程封面", height = 20, width = 30, isImportField = "true_st", orderNum = "1")
    @ApiModelProperty(value = "课程封面图片路径")
    private String courseCover;

    @ApiModelProperty(value = "章节ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long chapterId;

    @ApiModelProperty(value = "章节标题")
    private String chapterTitle;

    @ApiModelProperty(value = "小节id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long videoId;

    @Excel(name = "章节", height = 20, width = 30, isImportField = "true_st", orderNum = "1")
    @ApiModelProperty(value = "排序字段")
    private Integer chapterSort;

    @Excel(name = "小节", height = 20, width = 30, isImportField = "true_st", orderNum = "1")
    @ApiModelProperty(value = "排序字段")
    private Integer videoSort;

    @Excel(name = "视频名称", height = 20, width = 30, isImportField = "true_st", orderNum = "1")
    @ApiModelProperty(value = "小节标题")
    private String videoTitle;

    @Excel(name = "观看日期", height = 20, width = 30, isImportField = "true_st", exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "1")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EduStudyRecordVO that = (EduStudyRecordVO) o;
        return courseId.equals(that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }
}
