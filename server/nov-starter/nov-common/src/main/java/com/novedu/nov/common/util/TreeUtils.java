package com.novedu.nov.common.util;

import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author ：大佬写的,me引用
 * @date ：2021/12/17 14:35
 * @description：
 * @modified By：
 * @version:
 */
public class TreeUtils {


    private static String id = "id";
    private static String parent = "parentId";
    private static String children = "children";
    private static String title = "title";
    private static Field idField;
    private static Field parentField;
    private static Field childrenField;
    private static Field titleField;

    /**
     * 集合转树结构
     *
     * @param collection 目标集合
     * @param clazz      集合元素类型
     * @return 转换后的树形结构
     */
    public static <T> Collection<T> toTree(@NotNull Collection<T> collection, @NotNull Class<T> clazz) {
        return toTree(collection, id, parent, children, clazz);
    }

    /**
     * 集合转树结构
     *
     * @param source    目标集合
     * @param id2       节点编号字段名称
     * @param parent2   父节点编号字段名称
     * @param children2 子节点集合属性名称
     * @param clazz     集合元素类型
     * @return 转换后的树形结构
     */
    public static <T> Collection<T> toTree(@NotNull Collection<T> source, String id2, String parent2, String children2, @NotNull Class<T> clazz) {
        try {
            if (source == null || source.isEmpty()) return null;// 如果目标集合为空,直接返回一个空树

            Collection<T> roots = initContext(clazz, id2, parent2, children2, source);

            // 找出所有的根节点
            for (T c : source) {
                Object parentId = parentField.get(c);
                if (isRootNode(parentId)) {
                    roots.add(c);
                }
            }

            // 从目标集合移除所有根节点
            source.removeAll(roots);

            // 遍历根节点, 依次添加子节点
            for (T root : roots) {
                addChild(root, source, idField, parentField, childrenField);
            }

            closeAccess();

            return roots;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 为目标节点添加孩子节点
     *
     * @param node          目标节点
     * @param collection    目标集合
     * @param idField       ID 字段
     * @param parentField   父节点字段
     * @param childrenField 字节点字段
     */
    private static <T> void addChild(@NotNull T node, @NotNull Collection<T> collection, @NotNull Field idField, @NotNull Field parentField, @NotNull Field childrenField) throws IllegalAccessException {
        Object id = idField.get(node);
        Collection<T> children = (Collection<T>) childrenField.get(node);
        // 如果子节点的集合为 null, 初始化孩子集合
        if (children == null) {
            if (collection.getClass().isAssignableFrom(Set.class)) {
                children = new HashSet<>();
            } else children = new ArrayList<>();
        }

        for (T t : collection) {
            Object o = parentField.get(t);
            if (id.equals(o)) {
                // 将当前节点添加到目标节点的孩子节点
                children.add(t);
                // 重设目标节点的孩子节点集合,这里必须重设,因为如果目标节点的孩子节点是null的话,这样是没有地址的,就会造成数据丢失,所以必须重设,如果目标节点所在类的孩子节点初始化为一个空集合,而不是null,则可以不需要这一步,因为java一切皆指针
                childrenField.set(node, children);
                // 递归添加孩子节点
                addChild(t, collection, idField, parentField, childrenField);
            }
        }
    }

    /**
     * 判断是否是根节点, 判断方式为: 父节点编号为空或为 0, 则认为是根节点. 此处的判断应根据自己的业务数据而定.
     *
     * @param parentId 父节点编号
     * @return 是否是根节点
     */
    private static boolean isRootNode(Object parentId) {
        boolean flag = false;
        if (parentId == null) {
            flag = true;
        } else if (parentId instanceof String && (StringUtils.isEmpty(parentId) || parentId.equals("0"))) {
            flag = true;
        } else if (parentId instanceof Integer && Integer.valueOf(0).equals(parentId)) {
            flag = true;
        } else if (parentId instanceof Long && Long.valueOf(0).equals(parentId)) {
            flag = true;
        }
        return flag;
    }


    private static <T> Collection<T> initContext(Class<T> clazz, String id2, String parent2, String children2, Collection<T> source) throws NoSuchFieldException {

        if (StringUtils.hasText(id2)) id = id2;                     // 如果被依赖字段名称为空则默认为id
        if (StringUtils.hasText(parent2)) parent = parent2;         // 如果依赖字段为空则默认为parent
        if (StringUtils.hasText(children2)) children = children2;   // 如果子节点集合属性名称为空则默认为children

        // 获取 id 字段, 从当前对象或其父类

        try {
            idField = clazz.getDeclaredField(id);
        } catch (NoSuchFieldException e1) {
            idField = clazz.getSuperclass().getDeclaredField(id);
        }

        // 获取 parentId 字段, 从当前对象或其父类

        try {
            parentField = clazz.getDeclaredField(parent);
        } catch (NoSuchFieldException e1) {
            parentField = clazz.getSuperclass().getDeclaredField(parent);
        }

        // 获取 children 字段, 从当前对象或其父类

        try {
            childrenField = clazz.getDeclaredField(children);
        } catch (NoSuchFieldException e1) {
            childrenField = clazz.getSuperclass().getDeclaredField(children);
        }

        // 设置为可访问
        idField.setAccessible(true);
        parentField.setAccessible(true);
        childrenField.setAccessible(true);

        if (source.getClass().isAssignableFrom(Set.class)) {
            return new HashSet<>();
        } else {
            return new ArrayList<>();
        }
    }

    private static void closeAccess() {
        // 关闭可访问
        idField.setAccessible(false);
        parentField.setAccessible(false);
        childrenField.setAccessible(false);
    }

    /**
     * 树转集合结构
     *
     * @param collection 目标集合
     * @param clazz      集合元素类型
     * @return 转换后的集合结构
     */
    public static <T> Collection<T> toCollection(@NotNull Collection<T> collection, @NotNull Class<T> clazz) throws NoSuchFieldException {
        return toCollection(collection, null, null, null, clazz);
    }

    public static <T> Collection<T> toCollection(@NotNull Collection<T> source, String id2, String parent2, String children2, @NotNull Class<T> clazz) throws NoSuchFieldException {
        if (source == null || source.isEmpty()) return null;// 如果目标集合为空,直接返回一个空树


        Collection<T> target = initContext(clazz, id2, parent2, children2, source);

        source.forEach(o -> getNodes(target, o, 0));

        closeAccess();

        return target;
    }

    private static <T> void getNodes(Collection<T> target, T node, Object pid) {
        Collection<T> children = null;
        try {
            children = (Collection<T>) childrenField.get(node);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            childrenField.set(node, null);
            parentField.set(node, pid);
            pid = idField.get(node);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        target.add(node);

        if (children != null && children.size() > 0) {
            Object finalPid = pid;
            children.forEach(o -> getNodes(target, o, finalPid));
        }
    }


    /**
     * 检查变更节点
     * 该方法可能有BUG，谨慎使用
     *
     * @param raw   初始集合
     * @param ripe  变更集合
     * @param clazz 集合元素类型
     *              titleField 对应节点名，如名字不一样需手动修改
     * @return 变更节点
     */
    public static <T> Map<String, Collection<T>> checkChangedNodes(Collection<T> raw, Collection<T> ripe, @NotNull Class<T> clazz) {
        Map<String, Collection<T>> result = new HashMap<>();
        Collection<T> updateNodes = null;
        Collection<T> insertNodes = null;
        Collection<T> removeNodes = null;
        try {
            if (clazz.getClass().isAssignableFrom(Set.class)) {
                updateNodes = new HashSet<>();
                insertNodes = new HashSet<>();
                removeNodes = new HashSet<>();
            } else {
                updateNodes = new ArrayList<>();
                insertNodes = new ArrayList<>();
                removeNodes = new ArrayList<>();
            }
            initContext(clazz, null, null, null, raw);
            titleField = clazz.getDeclaredField(title);
            titleField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (ripe == null || ripe.size() == 0) {
            for (T rr : raw
            ) {
                removeNodes.add(rr);
            }
        } else {
            for (T r : ripe
            ) {
                try {
                    Object rId = idField.get(r);
                    Object rPid = parentField.get(r);
                    Object rTitle = titleField.get(r);
                    boolean hasNode = false;
                    boolean beModified = false;
                    for (T rr : raw
                    ) {
                        Object rrId = idField.get(rr);
                        Object rrPid = parentField.get(rr);
                        Object rrTitle = titleField.get(rr);
                        if (rrId.equals(rId)) {
                            hasNode = true;
                            if (!rrPid.equals(rPid) || !rrTitle.equals(rTitle))
                                beModified = true;
                        }
                    }
                    if (!hasNode) {
                        insertNodes.add(r);
                    } else if (beModified)
                        updateNodes.add(r);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            for (T r : raw
            ) {
                boolean hasRemove = true;
                for (T rr : ripe
                ) {
                    try {
                        Object rId = idField.get(r);
                        Object rrId = idField.get(rr);
                        if (rrId.equals(rId))
                            hasRemove = false;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if (hasRemove)
                    removeNodes.add(r);
            }
        }
        result.put("insertNodes", insertNodes);
        result.put("updateNodes", updateNodes);
        result.put("removeNodes", removeNodes);
        return result;
    }
}
