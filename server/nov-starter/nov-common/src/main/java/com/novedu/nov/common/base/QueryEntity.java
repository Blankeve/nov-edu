package com.novedu.nov.common.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class QueryEntity implements Serializable {

    private Date startTime;

    private Date endTime;

}
