<template>
  <div class="app-container">
    <el-input placeholder="输入课程分类进行过滤" v-model="filterText">
    </el-input>
    <br />
    <br />
    <div class="down-tree" style="width: 100%">
      <el-button type="text" size="mini" @click="() => appendRoot(data)">
        添加课程
      </el-button>
      <el-button type="text" size="mini" @click="() => (expandAll = true)">
        展开所有
      </el-button>
      <el-button type="text" size="mini" @click="() => (expandAll = false)">
        收缩所有
      </el-button>
      <el-tree
        :data="data"
        show-checkbox
        node-key="id"
        ref="tree"
        default-expand-all
        :filter-node-method="filterNode"
        @node-drag-start="handleDragStart"
        @node-drag-enter="handleDragEnter"
        @node-drag-leave="handleDragLeave"
        @node-drag-over="handleDragOver"
        @node-drag-end="handleDragEnd"
        @node-drop="handleDrop"
        :expand-on-click-node="false"
      >
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span>{{ data.title }}</span>
          <span>
            <el-button type="text" size="mini" @click="() => append(data)">
              {{
                node.data.price
                  ? ""
                  : node.data.courseId
                  ? "添加章节"
                  : node.data.chapterId
                  ? "添加小节"
                  : ""
              }}
            </el-button>
            <el-button type="text" size="mini" @click="() => edit(node, data)">
              编辑{{
                node.data.price
                  ? "课程"
                  : node.data.courseId
                  ? "章节"
                  : node.data.chapterId
                  ? "小节"
                  : ""
              }}
            </el-button>
            <el-button
              type="text"
              size="mini"
              @click="() => remove(node, data)"
            >
              删除
            </el-button>
          </span>
        </span>
      </el-tree>
    </div>
    <br />
    <el-button type="primary" @click="onSubmit">保存</el-button>
    <el-button @click="initTree">重置</el-button>
  </div>
</template>

<script>
let id = 1000;

import { getTree, update } from "@/api/course";
export default {
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },
  },
  data() {
    return {
      filterText: "",
      data: [],
      resetData: [],
      expandAll: false,
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      getTree().then((resp) => {
        if (resp.code === 200) {
          // id = resp.data.lastId;
          if (resp.data) {
            this.data = resp.data;
            this.resetData = JSON.parse(JSON.stringify(resp.data));
          }
        }
      });
    },
    initTree() {
      this.data = JSON.parse(JSON.stringify(this.resetData));
    },
    onSubmit() {
      update({ eduSubjects: this.data }).then((resp) => {
        this.$message({
          message: "保存成功",
          type: resp.code === 200 ? "success" : "error",
        });
        this.fetchData();
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.title.indexOf(value) !== -1;
    },
    append(data) {
      if (data.courseId) {
        this.$router.push({
          path: "/course/chapter",
        });
      }
      else if(data.chapterId){
        this.$router.push({
          path: "/course/video",
        });
      }
    },
    edit(node, data) {
        if (data.courseId) {
        this.$router.push({
          path: "/course/chapter",
          query: {
            chapter: data,
          },
        });
      }
      else if(data.chapterId){
        this.$router.push({
          path: "/course/video",
          query: {
            video: data,
          },
        });
      }
    },
    appendRoot(data) {
      this.$router.push({
        path: "/course/save",
      });
    },
    remove(node, data) {
      const parent = node.parent;
      const children = parent.data.children || parent.data;
      const index = children.findIndex((d) => d.id === data.id);
      children.splice(index, 1);
    },

    renderContent(h, { node, data, store }) {
      return (
        <span class="custom-tree-node">
          <span>{node.title}</span>
          <span>
            <el-button
              size="mini"
              type="text"
              on-click={() => this.append(data)}
            >
              Append
            </el-button>
            <el-button
              size="mini"
              type="text"
              on-click={() => this.remove(node, data)}
            >
              Delete
            </el-button>
          </span>
        </span>
      );
    },

    handleDragStart(node, ev) {
      console.log("drag start", node);
    },
    handleDragEnter(draggingNode, dropNode, ev) {
      console.log("tree drag enter: ", dropNode.title);
    },
    handleDragLeave(draggingNode, dropNode, ev) {
      console.log("tree drag leave: ", dropNode.title);
    },
    handleDragOver(draggingNode, dropNode, ev) {
      console.log("tree drag over: ", dropNode.title);
    },
    handleDragEnd(draggingNode, dropNode, dropType, ev) {
      console.log("tree drag end: ", dropNode && dropNode.title, dropType);
    },
    handleDrop(draggingNode, dropNode, dropType, ev) {
      console.log("tree drop: ", dropNode.title, dropType);
    },
    allowDrop(draggingNode, dropNode, type) {
      if (!dropNode.data.title) {
        return type !== "inner";
      } else {
        return true;
      }
    },
    allowDrag(draggingNode) {
      return draggingNode.data.title != undefined;
    },
  },
};
</script>

<style>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.down-tree {
  flex: 1;
  max-width: 1200px;
  height: 678px;
  background: rgba(245, 248, 250, 1);
  border-radius: 3px;
  border: 1px solid rgba(211, 219, 222, 1);
  margin-left: 12px;
  padding: 14px;
}

.el-tree-node__label {
  font-size: 12px;
}

.el-tree .el-tree-node__expand-icon.expanded {
  -webkit-transform: rotate(0deg);
  transform: rotate(0deg);
}
.el-tree .el-icon-caret-right:before {
  background: url("../../icons/png/folder.png") no-repeat;
  content: "";
  display: block;
  width: 28px;
  height: 28px;
  font-size: 28px;
  background-size: 25px;
}

.el-tree-node__expand-icon.is-leaf::before {
  background: url("../../icons/png/file.png") no-repeat;
  content: "";
  display: block;
  width: 28px;
  height: 28px;
  font-size: 28px;
  background-size: 25px;
}

.el-tree-node__content {
  background: #f5f8fa;
  height: 36px;
}

.el-tree-node.is-current > .el-tree-node__content {
  background-color: #fde9be !important;
  color: #333333;
}
</style>

