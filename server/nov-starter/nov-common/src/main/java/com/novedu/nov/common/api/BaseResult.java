package com.novedu.nov.common.api;

import lombok.Data;

@Data
public class BaseResult<T> {
    private Integer code;
    private String msg;
    private T data;

    public BaseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> BaseResult success(T data) {
        return new BaseResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }

    public static BaseResult success(String msg) {
        return new BaseResult(ResultCode.SUCCESS.getCode(), msg, "");
    }

    public static <T> BaseResult success(String msg, T data) {
        return new BaseResult(ResultCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> BaseResult error(T data) {
        return new BaseResult(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMsg(), data);
    }

    public static <T> BaseResult error(String msg) {
        return new BaseResult(ResultCode.ERROR.getCode(), msg, "");
    }

    public static <T> BaseResult error(String msg, T data) {
        return new BaseResult(ResultCode.ERROR.getCode(), msg, data);
    }

    public static  BaseResult setStatus(ResultCode result) {
        return new BaseResult(result.getCode(), result.getMsg(),null);
    }
    public static <T> BaseResult setStatus(ResultCode result, T data) {
        return new BaseResult(result.getCode(), result.getMsg(), data);
    }
}
