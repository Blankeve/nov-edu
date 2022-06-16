package com.novedu.nov.common.base;

/**
 * @author ：juam
 * @date ：2021/11/8 10:26
 * @description：
 * @modified By：
 * @version:
 */
public enum ResultCode {
    SUCCESS(200, "success"),
    ERROR(500, "error"),
    SERVICE_INVOKE_FAILURE(504, "网络出差了，请稍后再试"),
    LOGIN_FAIL(403, "登录失效，请重新登录"),
    OTHER_DEVICE_LOGIN(4031, "当前账号已在别处登录，请重新登录");

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
