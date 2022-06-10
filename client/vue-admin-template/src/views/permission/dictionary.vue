<template>
  <div class="app-container">
    <el-form :inline="true" ref="queryForm" :model="queryForm">
      <el-form-item prop="configName" label="字典名称">
        <el-input
          v-model="queryForm.configName"
          placeholder="字典名称"
        ></el-input>
      </el-form-item>

      <el-form-item prop="configKey" label="字典类型">
        <el-input
          v-model="queryForm.configKey"
          placeholder="字典类型"
        ></el-input>
      </el-form-item>

      <el-form-item prop="status" label="状态">
        <el-select v-model="queryForm.status" placeholder="请选择状态">
          <el-option label="全部" value=""> </el-option>
          <el-option label="启用" value="1"> </el-option>
          <el-option label="停用" value="0"> </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="创建时间" prop="dateRange">
        <el-date-picker
          v-model="dateRange"
          type="datetimerange"
          :picker-options="pickerOptions"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right"
        >
        </el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="fetchData"
          >查询</el-button
        >
        <el-button
          type="danger"
          icon="el-icon-refresh-left"
          @click="resetForm('queryForm')"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-button
      icon="el-icon-plus"
      size="mini"
      type="primary"
      plain
      @click="addConfig()"
      >新增</el-button
    >
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      row-key="id"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column label="字典名称" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row.configName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="字典类型" align="center">
        <template slot-scope="scope">
          <el-tag size="medium">
            {{ scope.row.configKey }}
          </el-tag>
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

      <el-table-column fixed="right" align="center" width="250px" label="操作">
        <template slot-scope="scope">
          <el-button
            round
            @click="handleAddConfig(scope.row)"
            size="mini"
            icon="el-icon-plus"
            type="primary"
            plain
            >新增</el-button
          >
          <el-button
            style="margin-left: 0"
            round
            @click="handleEdit(scope.row)"
            size="mini"
            icon="el-icon-edit"
            type="info"
            plain
            >编辑</el-button
          >
          <el-popconfirm
            :title="
              (scope.row.children ? '该字典包含多个节点,' : '') +
              `确定删除 [${scope.row.configName}] 吗？`
            "
            @onConfirm="handleDelete(scope.row.id)"
          >
            <el-button
              round
              slot="reference"
              size="mini"
              icon="el-icon-delete"
              type="danger"
              plain
              >删除</el-button
            >
          </el-popconfirm>
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
        <el-form-item prop="configName" label="字典名称">
          <el-input v-model="form.configName"></el-input>
        </el-form-item>

        <el-form-item prop="configKey" label="字典类型">
          <el-input :disabled="disabled" v-model="form.configKey"></el-input>
        </el-form-item>

        <el-form-item prop="configValue" label="字典值">
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
import { getList, saveOrUpdate, changeStatus, removeById } from "@/api/config";

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
      disabled: true,
      listLoading: true,
      configFormTitle: "",
      configFormVisible: false,
      queryForm: {
        configName: "",
        configKey: "",
        configValue: "",
      },
      form: {
        configName: "",
        configKey: "",
        configValue: "",
      },
      sizes: [],
      pickerOptions: {
        shortcuts: [
          {
            text: "最近一周",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近三个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
      dateRange: [],
      formRules: {
        configName: [
          { required: true, message: "请输入字典名称", trigger: "blur" },
          {
            min: 1,
            max: 50,
            message: "长度在 1 到 50 个字符",
            trigger: "blur",
          },
        ],
        configKey: [
          { required: true, message: "请输入字典类型", trigger: "blur" },
          {
            min: 1,
            max: 50,
            message: "长度在 1 到 50 个字符",
            trigger: "blur",
          },
        ],
        configValue: [
          { required: true, message: "请输入字典值", trigger: "blur" },
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
      this.handleDateRange();
      getList(this.queryForm).then((response) => {
        let data = response.data;
        this.list = data;
        this.listLoading = false;
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dateRange = [];
    },
    handleDateLength(str) {
      str += "";
      if (str.length < 2) return "0" + str;
      return str;
    },
    handleDateFormat(time) {
      let formatDate =
        time.getFullYear() +
        "-" +
        this.handleDateLength(time.getMonth() + 1) +
        "-" +
        this.handleDateLength(time.getDate()) +
        " " +
        this.handleDateLength(time.getHours()) +
        ":" +
        this.handleDateLength(time.getMinutes()) +
        ":" +
        this.handleDateLength(time.getSeconds());
      return formatDate;
    },
    handleDateRange() {
      if (this.dateRange && this.dateRange.length > 0) {
        this.queryForm.startTime = this.handleDateFormat(
          new Date(this.dateRange[0])
        );
        this.queryForm.endTime = this.handleDateFormat(
          new Date(this.dateRange[1])
        );
      }
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
      this.disabled = false;
      this.form.id = row.id;
      this.form.configName = row.configName;
      this.form.configKey = row.configKey;
      this.form.configValue = row.configValue;
      this.configFormTitle = "修改字典数据";
      this.configFormVisible = true;
    },
    addConfig() {
      this.disabled = false;
      this.form = {
        configName: "",
        configKey: "",
        configValue: "",
      };
      this.form.grade = 1;
      this.configFormTitle = "添加字典数据";
      this.configFormVisible = true;
    },
    handleAddConfig(row) {
      this.addConfig();
      this.disabled = true;
      this.form.parentId = row.id;
      this.form.grade = row.grade + 1;
      this.form.configKey = row.configKey;
    },
    onSubmit() {
      this.configFormVisible = false;
      saveOrUpdate(this.form).then((resp) => {
        if (resp.code === 200) {
          this.$message.success((this.form.id ? "修改" : "添加") + "字典成功");
          this.fetchData();
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dateRange = [];
    },
    getRowClass({ row, column, rowIndex, columnIndex }) {
      return "background:#3f5c6d2c;";
    },
    changeSwitch(row) {
      row.children = undefined;
      changeStatus(row).then((resp) => {
        console.log(typeof row.status);
        if (resp.code === 200) {
          this.$message({
            showClose: true,
            message:
              "字典数据  " +
              "[" +
              row.configName +
              "] " +
              (row.status == "1" ? "已启用" : "已禁用"),
            type: row.status == "1" ? "success" : "warning",
          });

          this.fetchData();
        }
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