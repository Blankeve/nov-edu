package com.nov.common.model;

/**
 * @author ：juam
 * @date ：2021/12/7 17:02
 * @description：
 * @modified By：
 * @version:
 */
public enum RESULT_STATUS {

        SUCCESS(200, "操作成功"),
        ERROR(500, "操作失败"),
        LOGIN_FAIL(403, "登录失效，请重新登录"),
        MULTI_DEVICE_LOGIN(4031, "当前账号已在别处登录，请重新登录");

        private final int code;
        private final String msg;

        RESULT_STATUS(int code, String msg) {
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
