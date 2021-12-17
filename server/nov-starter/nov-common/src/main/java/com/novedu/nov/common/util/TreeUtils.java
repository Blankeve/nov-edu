package com.novedu.nov.common.util;

import com.novedu.nov.common.object.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：juam
 * @date ：2021/12/17 14:35
 * @description：
 * @modified By：
 * @version:
 */
public class TreeUtils {

    public static <T extends Tree> List<T> build(List<T> source) {
        List<T> target = new ArrayList<>();
        getRoots(source, target);
        target.forEach(s ->
                s.setChildren(getChildren(s.getId(), (List<Tree>) source))
        );
        return target;
    }

    private static <T extends Tree> void getRoots(List<T> source, List<T> target) {
        source.forEach(s -> {
            if (s.getParentId() == 0) {
                target.add(s);
            }
        });
    }

    private static <T extends Tree> List<T> getChildren(int pid, List<T> root) {
        List<T> childList = new ArrayList<>();
        root.forEach(s -> {
            if (s.getParentId() == pid) {
                childList.add(s);
            }
        });
        childList.forEach(s ->
                s.setChildren(getChildren(s.getId(), (List<Tree>) root))
        );
        return childList;
    }
}
