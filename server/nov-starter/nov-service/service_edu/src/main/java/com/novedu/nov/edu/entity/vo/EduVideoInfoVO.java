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
public class EduVideoInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "章节ID")
    private Long chapterId;

    @ApiModelProperty(value = "章节名称")
    private String chapterTitle;

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
