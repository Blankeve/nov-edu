package com.novedu.nov.common.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {

    @TableField(exist = false)
    private Date startTime;

    @TableField(exist = false)
    private Date endTime;

}
