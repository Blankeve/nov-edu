package com.nov.common.model;

/**
 * @author ：juam
 * @date ：2021/12/7 16:59
 * @description：
 * @modified By：
 * @version:
 */
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
        return new BaseResult(RESULT_STATUS.SUCCESS.getCode(), RESULT_STATUS.SUCCESS.getMsg(), data);
    }

    public static BaseResult success(String msg) {
        return new BaseResult(RESULT_STATUS.SUCCESS.getCode(), msg, "");
    }

    public static <T> BaseResult success(String msg, T data) {
        return new BaseResult(RESULT_STATUS.SUCCESS.getCode(), msg, data);
    }

    public static <T> BaseResult error(T data) {
        return new BaseResult(RESULT_STATUS.ERROR.getCode(), RESULT_STATUS.ERROR.getMsg(), data);
    }

    public static <T> BaseResult error(String msg) {
        return new BaseResult(RESULT_STATUS.ERROR.getCode(), msg, "");
    }

    public static <T> BaseResult error(String msg, T data) {
        return new BaseResult(RESULT_STATUS.ERROR.getCode(), msg, data);
    }

    public static  BaseResult setStatus(RESULT_STATUS result) {
        return new BaseResult(result.getCode(), result.getMsg(),null);
    }
    public static <T> BaseResult setStatus(RESULT_STATUS result, T data) {
        return new BaseResult(result.getCode(), result.getMsg(), data);
    }
}
