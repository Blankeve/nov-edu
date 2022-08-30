<template>
  <div class="app-container">
    <el-input placeholder="输入关键字进行过滤" v-model="filterText"> </el-input>
    <br />
    <br />
    <div id="down-tree" style="width: 50%">
      <el-button type="text" size="mini" @click="() => appendRoot(data)">
        新增一级权限
      </el-button>
      <div class="menu-tree">
        <el-tree
          :data="data"
          node-key="id"
          ref="tree"
          :filter-node-method="filterNode"
          @node-drag-start="handleDragStart"
          @node-drag-enter="handleDragEnter"
          @node-drag-leave="handleDragLeave"
          @node-drag-over="handleDragOver"
          @node-drag-end="handleDragEnd"
          @node-drop="handleDrop"
          :expand-on-click-node="false"
          :default-expanded-keys="defaultShowNodes"
          @node-expand="handleNodeExpand"
          @node-collapse="handleNodeCollapse"
        >
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <i
              :class="data.type == 1 ? 'top-node-icon' : 'leaf-node-icon '"
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
              <el-button
                type="text"
                size="mini"
                icon="el-icon-delete"
                @click="() => remove(node, data)"
              >
              </el-button>
            </span>
          </span>
        </el-tree>
      </div>

      <el-dialog
        :title="menuFormTitle"
        :visible.sync="menuFormVisible"
        :close-on-click-modal="false"
        width="500px"
        center=""
      >
        <el-form :model="form" :rules="formRules" label-width="120">
          <el-form-item v-if="this.form.parentName != '无'" label="父级名称">
            <el-input v-model="form.parentName"></el-input>
          </el-form-item>

          <el-form-item label="权限名称" prop="title">
            <el-input v-model="form.title"></el-input>
          </el-form-item>

          <el-form-item label="权限类型" prop="type">
            <el-radio-group v-model="form.type">
              <el-radio-button label="1">菜单</el-radio-button>
              <el-radio-button label="2">接口</el-radio-button>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="接口路径">
            <el-input v-model="form.value"></el-input>
          </el-form-item>

          <el-form-item label="组件访问名称">
            <el-input v-model="form.name"></el-input>
          </el-form-item>

          <el-form-item label="组件访问路径《权限类型为接口时填写随意》" prop="path">
            <el-input v-model="form.path"></el-input>
          </el-form-item>

          <el-form-item label="组件本地路径《一级菜单填写Layout，权限类型为接口时填写随意》" prop="component">
            <el-input v-model="form.component"></el-input>
          </el-form-item>

          <el-form-item label="图标">
            <el-input v-model="form.icon"></el-input>
          </el-form-item>

          <el-form-item label="权限状态">
            <el-radio-group v-model="form.status">
              <el-radio-button label="1">显示</el-radio-button>
              <el-radio-button label="2">隐藏</el-radio-button>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="权限排序">
            <el-input-number
              v-model="form.sort"
              :min="0"
              :max="1000"
              label="权限排序"
            ></el-input-number>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="menuFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="onSubmit">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
let id = 1000;

import { getTree, saveOrUpdate, removeById } from "@/api/menu";
export default {
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },
  },
  data() {
    return {
      filterText: "",
      defaultShowNodes: [],
      data: [],
      resetData: [],
      baseURL: process.env.VUE_APP_BASE_API,
      form: {
        id: null,
        parentName: "无",
        parentId: 0,
        sort: 0,
        title: "",
        name: "",
        type: 1,
        value: "",
        path: "",
        component: "",
        icon: "",
        status: 1,
      },
      formRules: {
        title: [
          { required: true, message: "请输入权限名称", trigger: "blur" },
          {
            min: 1,
            max: 20,
            message: "长度在 1 到 20 个字符",
            trigger: "blur",
          },
        ],
        type: [{ required: true, message: "请输入权限名称", trigger: "blur" }],
        component: [
          { required: true, message: "请输入组件路径", trigger: "blur" },
          {
            min: 1,
            max: 100,
            message: "长度在 2 到 100 个字符",
            trigger: "blur",
          },
        ],
        path: [
          { required: true, message: "请输入访问路径", trigger: "blur" },
          {
            min: 1,
            max: 100,
            message: "长度在 2 到 100 个字符",
            trigger: "blur",
          },
        ],
      },
      menuFormTitle: "",
      menuFormVisible: false,
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      getTree().then((resp) => {
        if (resp.code === 200) {
          this.data = resp.data;
        }
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.title.indexOf(value) !== -1;
    },
    resetForm() {
      this.form.id = null;
      this.form.title = "";
      this.form.name = "";
      this.form.type = "";
      this.form.value = "";
      this.form.path = "";
      this.form.component = "";
      this.form.icon = "";
      this.form.status = 1;
    },
    onSubmit() {
      saveOrUpdate(this.form).then((resp) => {
        if (resp.code === 200) {
          if (this.form.id) this.$message.success("更新成功");
          else
            this.$message.success(
              "新增成功，请在对应角色分配权限"
            );
          this.fetchData();
          this.menuFormVisible = false;
        }
      });
    },
    append(data) {
      this.resetForm();
      this.form.parentId = data.id;
      this.form.parentName = data.title;
      this.menuFormTitle = "新增权限";
      this.menuFormVisible = true;
    },
    appendRoot(data) {
      this.resetForm();
      this.form.parentId = 0;
      this.form.parentName = "无";
      this.menuFormTitle = "新增权限";
      this.menuFormVisible = true;
    },
    remove(node, data) {
      removeById(data.id).then((resp) => {
        if (resp.code === 200) {
          this.$message.success("删除成功");
          this.fetchData();
        }
      });
    },
    edit(node, data) {
      if (data.id) {
        this.form.id = data.id;
        this.form.parentId = data.parentId;
        this.form.parentName = "无";
        this.form.title = data.title;
        this.form.name = data.name;
        this.form.type = data.type;
        this.form.value = data.value;
        this.form.path = data.path;
        this.form.component = data.component;
        this.form.icon = data.icon;
        this.form.status = data.status;
        this.form.sort = data.sort;
        this.menuFormTitle = "更新权限";
        this.menuFormVisible = true;
      }
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
    // 树节点展开
    handleNodeExpand(data) {
      // 保存当前展开的节点
      let flag = false;
      this.defaultShowNodes.some((item) => {
        if (item === data.id) {
          // 判断当前节点是否存在， 存在不做处理
          flag = true;
          return true;
        }
      });
      if (!flag) {
        // 不存在则存到数组里
        this.defaultShowNodes.push(data.id);
      }
    },
    // 树节点关闭
    handleNodeCollapse(data) {
      this.defaultShowNodes.some((item, i) => {
        if (item === data.id) {
          // 删除关闭节点
          this.defaultShowNodes.length = i;
        }
      });
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
  max-width: 600px;
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

.menu-tree .el-tree-node__expand-icon.is-leaf::before {
  background: none;
  content: "";
  display: block;
  width: 28px;
  height: 28px;
  font-size: 28px;
  background-size: 25px;
}

.menu-tree .top-node-icon {
  background: url("../../icons/png/html.png") no-repeat;
  content: "";
  display: block;
  width: 28px;
  height: 28px;
  font-size: 28px;
  background-size: 25px;
}

.menu-tree .leaf-node-icon {
  background: url("../../icons/png/hand.png") no-repeat;
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

