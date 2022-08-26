<template>
  <div class="app-container">
    <el-form :inline="true" ref="form" :model="form">
      <el-form-item>
        <el-button icon="el-icon-plus" type="primary" size="small" @click="addRole">添加角色</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <!--展开列-->
      <el-table-column type="expand"></el-table-column>

      <el-table-column align="center" label="#" width="50">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="角色名称" align="center">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>

      <el-table-column width="100"  label="角色编码" align="center">
        <template slot-scope="scope">
          {{ scope.row.code }}
        </template>
      </el-table-column>

      <el-table-column label="描述" align="center">
        <template slot-scope="scope">
          {{ scope.row.remark }}
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        prop="created_at"
        width="200"
        label="创建时间"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>

      <el-table-column fixed="right" align="center" width="400" label="操作">
        <template slot-scope="scope">
          <el-button
            type="warning"
            icon="el-icon-setting"
            @click="handleSelectMenu(scope.row.id)"
            >分配权限</el-button
          >

          <el-button @click="handleEdit(scope.row)" icon="el-icon-edit"
            >编辑</el-button
          >

          <el-button
            type="danger"
            @click="handleDelete(scope.row.id)"
            icon="el-icon-delete"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <div class="block">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="form.current"
        :page-sizes="sizes"
        :page-size="form.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="form.total"
      >
      </el-pagination>
    </div>

    <el-dialog
      :title="roleFormTitle"
      :visible.sync="roleFormVisible"
      :close-on-click-modal="false"
      width="500px"
      center=""
    >
      <el-form :model="form" :rules="formRules" label-width="120">
        <el-form-item prop="name" label="角色名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>

        <el-form-item prop="code" label="角色编码">
          <el-input v-model="form.code"></el-input>
        </el-form-item>

        <el-form-item prop="remark" label="角色描述">
          <el-input v-model="form.remark"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="roleFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!--分配菜单-->
    <el-dialog title="分配权限" :visible.sync="dialogVisibleMenu" width="30%">
      <el-tree
        :data="menuTreeList"
        show-checkbox
        default-expand-all
        node-key="id"
        ref="tree"
        highlight-current
        :props="defaultProps"
      >
      </el-tree>
      <div style="margin-top: 20px" align="center">
        <el-button type="primary" @click="handleSaveMenu()">保存</el-button>
        <el-button @click="handleClearMenu()">清空</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  saveOrUpdate,
  getPage,
  removeById,
  exportPage,
} from "@/api/role";
import { exportExcel } from "@/utils/excel";
import { getTree, queryMenuByRoleId, saveRoleSelMenu } from "@/api/menu";
export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: "success",
        draft: "gray",
        deleted: "danger",
      };
      return statusMap[status];
    },
  },
  data() {
    return {
      list: null,
      listLoading: true,
      //菜单相关
      dialogVisibleMenu: false,
      menuTreeList: [],
      defaultProps: {
        children: "children",
        label: "title",
      },
      roleId: null,
      form: {
        name: "",
        code: null,
        remark: "",
        current: 1,
        size: 8,
        total: 0,
      },
         formRules: {
        name: [
          { required: true, message: "请输入角色名称", trigger: "blur" },
          { min: 1, max: 20, message: "长度在 1 到 20 个字符", trigger: "blur" },
        ],
        code: [
          { required: true, message: "请输入角色编码", trigger: "blur" },
          { min: 1, max: 10, message: "长度在 1 到 10 个字符", trigger: "blur" },
        ],
        remark: [
          { required: true, message: "请输入角色描述", trigger: "blur" },
          { min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur" },
        ],
      },
      roleFormTitle: "",
      roleFormVisible: false,
      dateRange: [],
      sizes: [],
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      this.listLoading = true;
      this.sizes =
        this.form.size > 1
          ? [this.form.size / 2, this.form.size, this.form.size * 2]
          : [this.form.size, this.form.size * 2];
      getPage(this.form).then((response) => {
        let data = response.data;
        this.form.current = data.current;
        this.form.size = data.size;
        this.form.total = data.total;
        this.list = data.records;
        this.listLoading = false;
      });
    },
    handleClearMenu() {
      this.$nextTick(() => {
        this.$refs.tree.setCheckedKeys([]);
      });
    },
    //新增分配菜单
    handleSaveMenu() {
      let checkedMenuIds = this.$refs.tree
        .getCheckedKeys()
        .concat(this.$refs.tree.getHalfCheckedKeys());
      this.$confirm("确认分配所选菜单?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        let obj = { id: this.roleId, checkMenu: checkedMenuIds };
        saveRoleSelMenu(obj).then((resp) => {
          if (resp.code === 200) {
            this.$message.success("分配成功");
            location.reload();
          }
        });
      });
    },
    handleSelectMenu(roleId) {
      this.dialogVisibleMenu = true;
      this.roleId = roleId;
      getTree().then((resp) => {
        if (resp.code === 200) {
          this.menuTreeList = resp.data;
          //回显菜单数据
          queryMenuByRoleId(roleId).then((result) => {
            let roleMenu = result.data;
            let checkedMenuIds = [];
            for (let i = 0; i < roleMenu.length; i++) {
              if (roleMenu != null && roleMenu.length > 0) {
                checkedMenuIds.push(roleMenu[i].id);
              }
            }
            const arr = [];
            for (let i = 0; i < checkedMenuIds.length; i++) {
              if (
                !this.$refs.tree.getNode(checkedMenuIds[i]).childNodes ||
                !this.$refs.tree.getNode(checkedMenuIds[i]).childNodes.length
              ) {
                arr.push(checkedMenuIds[i]);
              }
            }
            this.$refs.tree.setCheckedKeys(arr);
          });
        }
      });
    },
    addRole() {
      this.form.id = null;
      this.form.name = "";
      this.form.code = null;
      this.form.remark = "";
      this.roleFormTitle = "添加角色";
      this.roleFormVisible = true;
    },
    exportTeacherPage() {
      exportPage(this.form).then((resp) => {
        exportExcel(resp);
      });
    },
    handleCurrentChange(p) {
      this.form.current = p;
      this.fetchData();
    },
    handleSizeChange(s) {
      this.form.size = s;
      this.fetchData();
    },
    handleDelete(id) {
      removeById(id).then((response) => {
        this.fetchData();
      });
    },
    handleEdit(row) {
      this.form.id = row.id;
      this.form.name = row.name;
      this.form.code = row.code;
      this.form.remark = row.remark;
      this.roleFormTitle = "修改角色";
      this.roleFormVisible = true;
    },
    onSubmit() {
      saveOrUpdate(this.form).then((resp) => {
        if (resp.code === 200) {
          this.roleFormVisible = false;
          this.$message.success((this.form.id ? "修改" : "添加") + "角色成功");
          this.fetchData();
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dateRange = [];
    },
  },
};
</script>

<style>
.mid-input {
  width: 80px;
}
.el-pagination {
  text-align: center;
}
</style>