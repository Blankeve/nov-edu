package com.novedu.nov.common.base;

/**
 * @author ：juam
 * @date ：2022/2/24 11:08
 * @description：
 * @modified By：
 * @version:
 */
public enum RoleType {
    ADMIN(0),
    TEACHER(5),
    STUDENT(9);

    private final int code;


    RoleType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
