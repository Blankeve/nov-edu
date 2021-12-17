package com.novedu.nov.common.util;

/**
 * @author ：juam
 * @date ：2021/12/17 10:29
 * @description：
 * @modified By：
 * @version:
 */
public class BeanUtils {

    public static <T> T copyObject(Object source, Class target) {
        T obj = null;
        try {
            obj = (T) Class.forName(target.getName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        org.springframework.beans.BeanUtils.copyProperties(source, obj);
        return obj;
    }

}
