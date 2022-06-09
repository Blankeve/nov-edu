<template>
  <div class="table-wrapper">
    <el-button icon="el-icon-plus" type="primary" @click="addConfig()"
      >添加字典</el-button
    >
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      row-key="id"
      :row-style="getRowClass"
      :header-row-style="getRowClass"
      :header-cell-style="getRowClass"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column label="名称" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.configName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="key" align="center">
        <template slot-scope="scope">
          {{ scope.row.configKey }}
        </template>
      </el-table-column>

      <el-table-column label="value" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.configValue }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="created_at" label="创建时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="created_at" label="更新时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.updateTime }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" width="100px" label="是否启用">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-color="#1890ff"
            active-value="1"
            inactive-color="#DCDFE6"
            inactive-value="0"
            @change="changeSwitch(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>

      <el-table-column fixed="right" align="center" width="300px" label="操作">
        <template slot-scope="scope">
          <el-button
            type="info"
            @click="handleEdit(scope.row)"
            size="small"
            icon="el-icon-edit"
            >编辑</el-button
          >
          <el-button
            type="primary"
            @click="handleAddConfig(scope.row)"
            size="small"
            icon="el-icon-plus"
            >新增</el-button
          >
          <el-button
            type="danger"
            @click="handleDelete(scope.row.id)"
            size="small"
            icon="el-icon-delete"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      :title="configFormTitle"
      :visible.sync="configFormVisible"
      :close-on-click-modal="false"
      width="500px"
      center=""
    >
      <el-form :rules="formRules" :model="form" label-width="120">
        <el-form-item prop="configName" label="配置名称">
          <el-input v-model="form.configName"></el-input>
        </el-form-item>

        <el-form-item prop="configKey" label="配置键">
          <el-input v-model="form.configKey"></el-input>
        </el-form-item>

        <el-form-item prop="configValue" label="配置值">
          <el-input
            :autosize="{ minRows: 2, maxRows: 4 }"
            type="textarea"
            v-model="form.configValue"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="configFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getList, saveOrUpdate, removeById } from "@/api/config";

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
      list: [],
      listLoading: true,
      form: { configName: "", configKey: "", configValue: "" },
      configFormTitle: "",
      configFormVisible: false,
      form: {
        configName: "",
        configKey: "",
        configValue: "",
      },
      sizes: [],
      formRules: {
        configName: [
          { required: true, message: "请输入配置名称", trigger: "blur" },
          {
            min: 1,
            max: 50,
            message: "长度在 1 到 50 个字符",
            trigger: "blur",
          },
        ],
        configKey: [
          { required: true, message: "请输入配置键", trigger: "blur" },
          {
            min: 1,
            max: 50,
            message: "长度在 1 到 50 个字符",
            trigger: "blur",
          },
        ],
        configValue: [
          { required: true, message: "请输入配置值", trigger: "blur" },
          {
            min: 1,
            max: 500,
            message: "长度在 1 到 500 个字符",
            trigger: "blur",
          },
        ],
      },
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
      getList(this.form).then((response) => {
        let data = response.data;
        this.list = data;
        this.listLoading = false;
      });
    },
    handleDelete(id) {
      removeById(id).then((resp) => {
        if (resp.code === 200) {
          this.$message.success("删除成功");
          this.fetchData();
        }
      });
    },
    handleEdit(row) {
      this.form.id = row.id;
      this.form.configName = row.configName;
      this.form.configKey = row.configKey;
      this.form.configValue = row.configValue;
      this.configFormTitle = "修改配置";
      this.configFormVisible = true;
    },
    addConfig() {
      this.form = {
        configName: "",
        configKey: "",
        configValue: "",
      };
      this.form.grade = 1;
      this.configFormTitle = "添加字典";
      this.configFormVisible = true;
    },
    handleAddConfig(row) {
      this.addConfig();
      this.form.parentId = row.id;
      this.form.grade = row.grade + 1;
      this.form.configKey = row.configKey;
    },
    onSubmit() {
      this.configFormVisible = false;
      saveOrUpdate(this.form).then((resp) => {
        if (resp.code === 200) {
          this.$message.success((this.form.id ? "修改" : "添加") + "配置成功");
          this.fetchData();
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    getRowClass({ row, column, rowIndex, columnIndex }) {
      return "background:#3f5c6d2c;";
    },
    changeSwitch(row) {
      row.children = undefined;
      saveOrUpdate(row).then((resp) => {
        console.log(typeof row.status);
        if (resp.code === 200) {
          console.log(row.status);
          this.$message.success(
            "字典: " +
              "'" +
              row.configName +
              "' " +
              (row.status == "1" ? "已启用" : "已禁用")
          );
        }
        this.fetchData();
      });
    },
  },
};
</script>

<style lang="less" >
.mid-input {
  width: 80px;
}
</style>