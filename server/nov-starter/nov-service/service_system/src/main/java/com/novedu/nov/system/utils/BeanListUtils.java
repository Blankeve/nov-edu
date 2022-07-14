package com.novedu.nov.system.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author liouwb
 */
public class BeanListUtils extends BeanUtils {

    /**
     * 转换对象 list
     *
     * @param sources        源对象list
     * @param targetSupplier 目标对象供应方 new
     * @param <S>            源对象类型
     * @param <T>            目标对象类型
     * @return 目标对象list
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> targetSupplier) {
        if (null == sources || null == targetSupplier) {
            return null;
        }

        return sources.stream().map(s -> {
            T target = targetSupplier.get();
            copyProperties(s, target);
            return target;
        }).collect(Collectors.toList());
    }

    /**
     * mybatis-plus Ipage对象复制
     *
     * @param sources 数据对对应的实体Page对象
     * @param target  复制之后的对象
     * @param <S>     数据库对象实体类
     * @param <T>     目标对象类型(VO)
     * @return 目标对象list
     */
    public static <S, T> IPage<T> copyPage(IPage<S> sources, IPage<T> target, Supplier<T> targetSupplier) {
        // 复制外层
        copyProperties(sources, target);

        // 复制records对象
        target.setRecords(copyListProperties(sources.getRecords(), targetSupplier));

        return target;
    }

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
        copyProperties(source, obj);
        return obj;
    }
}
