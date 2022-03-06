package com.novedu.nov.statistics.entity.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class EchartsVO {

    private String type;
    private Map title;
    private Integer xRorate;
    private List labels;
    private List<Map> datasets;
}
