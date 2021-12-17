<template>
  <div class="app-container">
    <el-input placeholder="输入课程分类进行过滤" v-model="filterText">
    </el-input>
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
      draggable
      :allow-drop="allowDrop"
      :allow-drag="allowDrag"
      :expand-on-click-node="false"
    >
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ data.title }}</span>
       <span>
              <el-button type="text" size="mini" @click="() => append(data)">
                添加
              </el-button>
              <el-button
                type="text"
                size="mini"
                @click="() => edit(node, data)"
              >
                编辑
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
    <br>  <br>  <br>
    <el-button type="primary" @click="onSubmit">保存</el-button>
        <el-button @click="resetForm('form')">重置</el-button>
  </div>
</template>

<script>
let id = 1000;

import { getList } from "@/api/subject";
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
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      getList().then((resp) => {
        this.data = resp.data;
        console.log(this.data);
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.title.indexOf(value) !== -1;
    },
    append(data) {
      const newChild = { id: id++, title: "未命名", children: [] };
      if (!data.children) {
        this.$set(data, "children", []);
      }
      data.children.push(newChild);
    },

    remove(node, data) {
      const parent = node.parent;
      const children = parent.data.children || parent.data;
      const index = children.findIndex((d) => d.id === data.id);
      children.splice(index, 1);
    },
  edit(node, data) {
      const parent = node.parent;
      const children = parent.data.children || parent.data;
      const index = children.findIndex((d) => d.id === data.id);
      this.$prompt("请编辑节点名称", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputValidator:function(val){
          console.log(val);
          if(val){
            return true;
          }else{
            return '输入内容不能为空';
          }
        }
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
        console.log('drag start', node);
      },
      handleDragEnter(draggingNode, dropNode, ev) {
        console.log('tree drag enter: ', dropNode.label);
      },
      handleDragLeave(draggingNode, dropNode, ev) {
        console.log('tree drag leave: ', dropNode.label);
      },
      handleDragOver(draggingNode, dropNode, ev) {
        console.log('tree drag over: ', dropNode.label);
      },
      handleDragEnd(draggingNode, dropNode, dropType, ev) {
        console.log('tree drag end: ', dropNode && dropNode.label, dropType);
      },
      handleDrop(draggingNode, dropNode, dropType, ev) {
        console.log('tree drop: ', dropNode.label, dropType);
      },
      allowDrop(draggingNode, dropNode, type) {
        if (!dropNode.data.title) {
          return type !== 'inner';
        } else {
          return true;
        }
      },
      allowDrag(draggingNode) {
        return draggingNode.data.title != undefined;
      }
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
</style>

