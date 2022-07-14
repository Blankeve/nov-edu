package com.novedu.nov.edu.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
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
 * @since 2022-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="EduStudyRecord对象", description="")
public class EduStudyRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long courseId;

    private Long chapterId;

    private Long videoId;

    private Date createTime;

    private String nickname;

    private String courseTitle;

    private String chapterTitle;

    private String videoTitle;

    private Integer chapterSort;

    private Integer videoSort;

    private Date startTime;

    private Date endTime;

}
