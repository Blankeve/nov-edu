package com.novedu.nov.edu.entity;

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
public class EduStudyRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long courseId;

    private Long chapterId;

    private Long videoId;

    private Long uid;

    private Date createTime;


}
