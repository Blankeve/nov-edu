package com.novedu.nov.ucenter.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "LoginInfo对象", description = "")
public class LoginInfo {

    private String ip;
    private String location;
    private String OS;
    private String device;
}
