package com.novedu.nov.common.object;
import java.util.List;

/**
 * @author ：juam
 * @date ：2021/12/17 14:29
 * @description： 符合该规范的树形数据都可以遍历出来
 * @modified By：
 * @version:
 */
public abstract class Tree {

    /**
     * @description： 当前节点id
     */
    private Integer id;

    /**
     * @description： 当前节点描述
     */
    private String title;

    /**
     * @description： 父节点id
     */
    private Integer parentId;

    /**
     * @description： 当前节点下的所有子节点
     */

    private List<Tree> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<Tree> getChildren() {
        return children;
    }

    public void setChildren(List<Tree> children) {
        this.children = children;
    }

}
