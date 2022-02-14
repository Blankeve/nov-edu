package com.novedu.nov.edu.entity.vo;

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

    @ApiModelProperty(value = "课程讲师")
    private String teacherName;

    @ApiModelProperty(value = "课程讲师头像")
    private String teacherAvatar;

    @ApiModelProperty(value = "课程讲师头像")
    private String teacherCareer;

    @ApiModelProperty(value = "课程类别ID")
    private Integer subjectId;

    @ApiModelProperty(value = "课程类别ID")
    private Integer[] subjectIds;

    @ApiModelProperty(value = "类别名称")
    private String subjectTitle;

    @ApiModelProperty(value = "排序字段")
    private Integer subjectSort;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date subjectCreateTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date subjectUpdateTime;

    @ApiModelProperty(value = "课程ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long courseId;

    @ApiModelProperty(value = "课程标题")
    private String courseTitle;

    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private BigDecimal coursePrice;

    @ApiModelProperty(value = "总课时")
    private Integer courseLessonNum;

    @ApiModelProperty(value = "课程封面图片路径")
    private String courseCover;

    @ApiModelProperty(value = "销售数量")
    private Long courseBuyCount;

    @ApiModelProperty(value = "播放数量")
    private Long courseViewCount;

    @ApiModelProperty(value = "浏览数量")
    private Long courseCommentCount;

    @ApiModelProperty(value = "报名数量")
    private Integer courseApplyCount;

    @ApiModelProperty(value = "课程状态 0未发布  1已发布")
    private Integer courseStatus;

    @ApiModelProperty(value = "章节数量")
    private Integer chapterQty;

    @ApiModelProperty(value = "视频数量")
    private Integer videoQty;

    @ApiModelProperty(value = "创建时间")
    private Date courseCreateTime;


    @ApiModelProperty(value = "更新时间")
    private Date courseUpdateTime;

    @ApiModelProperty(value = "课程简介")
    private String introId;

    @ApiModelProperty(value = "课程简介")
    private String introDescription;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "章节ID")
    private Long chapterId;

    @ApiModelProperty(value = "章节名称")
    private String chapterTitle;

    @ApiModelProperty(value = "显示排序")
    private Integer chapterSort;


    @ApiModelProperty(value = "创建时间")
    private Date chapterCreateTime;


    @ApiModelProperty(value = "更新时间")
    private Date chapterUpdateTime;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "视频ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long videoId;

    @ApiModelProperty(value = "章节ID")
    private Integer videoChapterId;

    @ApiModelProperty(value = "节点名称")
    private String videoTitle;

    @ApiModelProperty(value = "云端视频资源")
    private String videoSourcePath;

    @ApiModelProperty(value = "原始文件名称")
    private String videoOriginalName;

    @ApiModelProperty(value = "排序字段")
    private Integer videoSort;

    @ApiModelProperty(value = "播放次数")
    private Long videoPlayCount;

    @ApiModelProperty(value = "是否可以试听：0收费 1免费")
    private Integer videoIsFree;

    @ApiModelProperty(value = "视频时长（秒）")
    private Long videoDuration;

    @ApiModelProperty(value = "状态")
    private Integer videoStatus;

    @ApiModelProperty(value = "视频源文件大小（字节）")
    private Float videoSize;

    @ApiModelProperty(value = "乐观锁")
    private Long videoVersion;


    @ApiModelProperty(value = "创建时间")
    private Date videoCreateTime;


    @ApiModelProperty(value = "更新时间")
    private Date videoUpdateTime;
}
