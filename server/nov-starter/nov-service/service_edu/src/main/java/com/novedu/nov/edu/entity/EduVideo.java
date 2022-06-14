package com.novedu.nov.edu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 课程视频
 * </p>
 *
 * @author juam
 * @since 2021-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "EduVideo对象", description = "课程视频")
public class EduVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "视频ID")
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    @NotNull(message = "所属章节不能为空")
    @Min(value = 999999999,message = "所属章节格式不正确")
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "章节ID")
    private Long chapterId;

    @NotEmpty(message = "小节标题不能为空")
    @ApiModelProperty(value = "节点名称")
    private String title;

    @NotNull(message = "云端视频资源不能为空")
    @Pattern(regexp = ".*[.mp4|.rmvb|.flv|.mpeg|.avi]",message = "请先上传视频")
    @ApiModelProperty(value = "云端视频资源")
    private String videoSourcePath;

    @ApiModelProperty(value = "原始文件名称")
    private String videoOriginalName;

    @NotNull(message = "当前小节不能为空")
    @ApiModelProperty(value = "排序字段")
    private Integer sort;

    @ApiModelProperty(value = "播放次数")
    private Long playCount;

    @Min(value = 0,message = "是否收费格式不正确")
    @ApiModelProperty(value = "是否可以试听：0收费 1免费")
    private Integer isFree;

    @Min(value = 0,message = "视频时长格式不正确")
    @ApiModelProperty(value = "视频时长（秒）")
    private Float duration;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @Min(value = 0,message = "视频源文件大小格式不正确")
    @ApiModelProperty(value = "视频源文件大小（字节）")
    private Float size;

    @ApiModelProperty(value = "乐观锁")
    private Long version;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "是否删除：1删除 2未删除")
    private Integer isDeleted;

    @TableField(exist = false)
    private boolean courseIsFree;
}
