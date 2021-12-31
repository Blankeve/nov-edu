package com.novedu.nov.edu.entity.dto;

/**
 * @author ：juam
 * @date ：2021/12/31 17:21
 * @description：
 * @modified By：
 * @version:
 */

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.novedu.nov.edu.entity.EduVideo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="EduChapter对象", description="课程")
public class EduChapterInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "章节ID")
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    @ApiModelProperty(value = "课程ID")
    private Integer courseId;

    @ApiModelProperty(value = "章节名称")
    private String title;

    @ApiModelProperty(value = "显示排序")
    private Integer sort;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "是否删除：1删除 2未删除")
    private Integer isDeleted;

    @TableField(exist = false)
    private List<EduVideo> children;
}