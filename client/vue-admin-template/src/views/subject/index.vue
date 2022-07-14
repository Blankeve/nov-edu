<template>
  <div class="app-container">
    <el-input placeholder="输入关键字进行过滤" v-model="filterText"> </el-input>
    <br />
    <br />
    <div id="down-tree" style="width: 50%">
      <el-button type="text" size="mini" @click="() => append()">
        添加一级分类
      </el-button>
      <el-tree
        :data="data"
        node-key="id"
        ref="tree1"
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
          <i
            :class="data.parentId == 0 ? 'top-node-icon' : 'leaf-node-icon '"
          ></i>
          <span
            >{{ data.title }}&nbsp;{{
              data.children ? `(${data.children.length})` : ""
            }}</span
          >
          <span style="margin-left: 100px">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-circle-plus-outline"
              @click="() => append(data)"
            >
            </el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              circle
              @click="() => edit(node, data)"
            >
            </el-button>
            &nbsp;
            <el-popconfirm
              title="删除该分类同时会清空分类下的所有课程信息,确认删除吗?"
              @onConfirm="remove(node, data)"
            >
              <el-button
                slot="reference"
                type="text"
                size="mini"
                icon="el-icon-delete"
              >
              </el-button>
            </el-popconfirm>
          </span>
        </span>
      </el-tree>
    </div>
    <br />
  </div>
</template>

<script>
let id = 1000;

import { getList, saveOrUpdate, removeSubjectById } from "@/api/subject";
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
      form: {
        parentId: null,
        title: "",
      },
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      getList().then((resp) => {
        if (resp.code === 200) {
          id = resp.data.lastId;
          if (resp.data.subjects) {
            this.data = resp.data.subjects;
            this.resetData = JSON.parse(JSON.stringify(resp.data.subjects));
          }
        }
      });
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
      this.$prompt("添加的节点名称", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputValidator: function (val) {
          if (val) {
            return true;
          } else {
            return "输入内容不能为空";
          }
        },
      })
        .then(({ value }) => {
          if (!data) this.form.parentId = 0;
          else this.form.parentId = data.id;
          this.form.title = value;
          saveOrUpdate(this.form).then((resp) => {
            if (resp.code === 200)
              this.$message.success((this.form.id ? "更新" : "添加") + "成功");
            this.fetchData();
          });
        })
        .catch(() => {
          return;
        });
    },
    remove(node, data) {
      removeSubjectById(data.id).then((resp) => {
        if (resp.code === 200) {
          this.$message.success("删除成功");
          this.fetchData();
        }
      });
    },
    edit(node, data) {
      const parent = node.parent;
      const children = parent.data.children || parent.data;
      const index = children.findIndex((d) => d.id === data.id);
      this.$prompt("请编辑节点名称", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputValidator: function (val) {
          console.log(val);
          if (val) {
            return true;
          } else {
            return "输入内容不能为空";
          }
        },
      })
        .then(({ value }) => {
          children[index].title = value;
        })
        .catch(() => {
          return;
        });
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
  font-size: 14px;
  padding-right: 8px;
}

#down-tree {
  flex: 1;
  max-width: 500px;
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
  background: url("../../icons/png/expand.png") no-repeat;
  content: "";
  display: block;
  width: 28px;
  height: 28px;
  font-size: 28px;
  background-size: 25px;
}

.el-tree-node__expand-icon.is-leaf::before {
  background: none;
  content: "";
  display: block;
  width: 28px;
  height: 28px;
  font-size: 28px;
  background-size: 25px;
}

.top-node-icon {
  background: url("../../icons/png/folder.png") no-repeat;
  content: "";
  display: block;
  width: 28px;
  height: 28px;
  font-size: 28px;
  background-size: 25px;
}

.leaf-node-icon {
  background: url("../../icons/png/book.png") no-repeat;
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

