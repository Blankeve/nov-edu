package com.novedu.nov.edu.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
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

    @Excel(name = "视频ID", height = 20, width = 30, isImportField = "true_st", orderNum = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "视频ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long videoId;

    @ApiModelProperty(value = "章节ID")
    private Integer videoChapterId;

    @Excel(name = "小节标题", height = 20, width = 30, isImportField = "true_st", orderNum = "2")
    @ApiModelProperty(value = "小节标题")
    private String videoTitle;

    @Excel(name = "源视频", height = 20, width = 30, isImportField = "true_st", orderNum = "4")
    @ApiModelProperty(value = "源视频")
    private String videoSourcePath;

    @ApiModelProperty(value = "原始文件名称")
    private String videoOriginalName;

    @Excel(name = "第几小节", height = 20, width = 30, isImportField = "true_st", orderNum = "3")
    @ApiModelProperty(value = "第几小节")
    private Integer videoSort;

    @Excel(name = "播放次数", height = 20, width = 30, isImportField = "true_st", orderNum = "8")
    @ApiModelProperty(value = "播放次数")
    private Long videoPlayCount;

    @Excel(name = "是否试听", height = 20, replace = {"否_0", "是_1"}, width = 30, isImportField = "true_st", orderNum = "7")
    @ApiModelProperty(value = "是否可以试听：0收费 1免费")
    private Integer videoIsFree;

    @Excel(name = "视频时长/秒", height = 20, width = 30, isImportField = "true_st", orderNum = "5")
    @ApiModelProperty(value = "视频时长（秒）")
    private Long videoDuration;

    @Excel(name = "视频状态", height = 20, width = 30, replace = {"异常_0", "正常_1"}, isImportField = "true_st", orderNum = "9")
    @ApiModelProperty(value = "视频状态")
    private Integer videoStatus;

    @Excel(name = "视频大小/MB", height = 20, width = 30, isImportField = "true_st", orderNum = "6")
    @ApiModelProperty(value = "视频源文件大小（字节）")
    private Float videoSize;

    @ApiModelProperty(value = "乐观锁")
    private Long videoVersion;

    @Excel(name = "创建时间", height = 20, width = 30, isImportField = "true_st", exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "10")
    @ApiModelProperty(value = "创建时间")
    private Date videoCreateTime;

    @Excel(name = "更新时间", height = 20, width = 30, isImportField = "true_st", exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "11")
    @ApiModelProperty(value = "更新时间")
    private Date videoUpdateTime;
}
