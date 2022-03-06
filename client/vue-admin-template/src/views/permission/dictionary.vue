<template>
  <div class="table-wrapper">
    <el-button icon="el-icon-plus" type="primary" @click="addConfig()"
      >添加配置</el-button
    >
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      fit
      stripe
      highlight-current-row
      :row-style="getRowClass"
      :header-row-style="getRowClass"
      :header-cell-style="getRowClass"
    >
      <el-table-column align="center" label="#" width="50">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>

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

      <el-table-column fixed="right" align="center" label="操作" width="250">
        <template slot-scope="scope">
          <el-button
            type="info"
            @click="handleEdit(scope.row)"
            icon="el-icon-edit"
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
          <el-input :autosize="{ minRows: 2, maxRows: 4}" type="textarea" v-model="form.configValue"></el-input>
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
      list: null,
      listLoading: true,
      form: { configName: "", configKey: "", configValue: "" },
      configFormTitle: "",
      configFormVisible: false,
      formRules: {
        configName: [
          { required: true, message: "请输入配置名称", trigger: "blur" },
          { min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur" },
        ],
        configKey: [
          { required: true, message: "请输入配置键", trigger: "blur" },
          { min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur" },
        ],
        configValue: [
          { required: true, message: "请输入配置值", trigger: "blur" },
          { min: 1, max: 500, message: "长度在 1 到 500 个字符", trigger: "blur" },
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
      getList().then((response) => {
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
      this.form = {};
      this.configFormTitle = "添加配置";
      this.configFormVisible = true;
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
  },
};
</script>

<style lang="less" >
.mid-input {
  width: 80px;
}
.el-pagination {
  text-align: center;
}
.table-wrapper {
  background: url("../../icons/png/bg.png") no-repeat;
  width: 100%;
  height: 100%;
  background-size: 100%;
}
.table-wrapper /deep/ .el-table--fit {
  padding: 20px;
}
.table-wrapper /deep/ .el-table,
.el-table__expanded-cell {
  background-color: transparent;
}

.table-wrapper /deep/ .el-table tr {
  background-color: transparent !important;
}
.table-wrapper /deep/ .el-table--enable-row-transition .el-table__body td,
.el-table .cell {
  background-color: transparent;
}
</style>