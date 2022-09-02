<template>
  <div class="app-container">
    <div class="search_box">
      <el-form :inline="true" ref="form" :model="form" size="medium">
        <el-form-item prop="username">
          <el-input
            suffix-icon="el-icon-search"
            v-model="form.username"
            placeholder="用户名"
            clearable
          ></el-input>
        </el-form-item>

        <el-form-item prop="loginIp">
          <el-input
            suffix-icon="el-icon-search"
            v-model="form.loginIp"
            placeholder="登录ip"
            clearable
          ></el-input>
        </el-form-item>

        <el-form-item prop="loginAddress">
          <el-input
            suffix-icon="el-icon-search"
            v-model="form.loginAddress"
            placeholder="登录地址"
            clearable
          ></el-input>
        </el-form-item>

        <el-form-item prop="loginDevice">
          <el-input
            suffix-icon="el-icon-search"
            v-model="form.loginDevice"
            placeholder="登录设备"
            clearable
          ></el-input>
        </el-form-item>

        <el-form-item prop="createTime">
          <el-date-picker
            style="width: 300px"
            v-model="dateRange"
            type="datetimerange"
            :picker-options="pickerOptions"
            range-separator="至"
            start-placeholder="起始日期"
            end-placeholder="结束日期"
            align="right"
          >
          </el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="searchForm"
            >查询</el-button
          >
          <el-button
            type="danger"
            icon="el-icon-refresh-left"
            @click="resetForm('form')"
            >重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>

    <div class="main_content" style="border-top: 2px solid #f0f0f0">
      <div class="btn-layout">
        <div>
          <el-button
            icon="el-icon-delete"
            size="mini"
            type="danger"
            :disabled="selectionIds.length == 0"
            plain
            @click="handleDelete()"
            >删除</el-button
          >
        </div>
        <div>
          <el-button
            size="mini"
            type="success"
            plain
            icon="el-icon-download"
            @click="exportInfoPage"
            >导出</el-button
          >
        </div>
      </div>

      <el-table
        ref="table"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
        v-loading="listLoading"
        :data="list"
        element-loading-text="玩命加载中"
        border
        fit
        highlight-current-row
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column align="center" label="#" width="50">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>

        <el-table-column width="100" label="用户名" align="center">
          <template slot-scope="scope">
            {{ scope.row.username }}
          </template>
        </el-table-column>

        <el-table-column label="登录ip" align="center">
          <template slot-scope="scope">
            {{ scope.row.loginIp }}
          </template>
        </el-table-column>

        <el-table-column label="登录地址" align="center">
          <template slot-scope="scope">
            {{ scope.row.loginAddress }}
          </template>
        </el-table-column>

        <el-table-column label="登录设备" align="center">
          <template slot-scope="scope">
            {{ scope.row.loginDevice }}
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          prop="created_at"
          label="登录时间"
          width="200"
        >
          <template slot-scope="scope">
            <i class="el-icon-time" />
            <span>{{ scope.row.createTime }}</span>
          </template>
        </el-table-column>

        <!-- <el-table-column fixed="right" align="center" label="操作" width="220">
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
      </el-table-column> -->
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
    </div>
    <!-- <el-dialog
      :title="noticeFormTitle"
      :visible.sync="noticeFormVisible"
      :close-on-click-modal="false"
      width="500px"
      center=""
    >
      <el-form
        :model="form"
        :rules="rules"
        class="demo-ruleForm"
        label-width="120"
      >
        <el-form-item prop="title" label="公告名称">
          <el-input v-model="form.title"></el-input>
        </el-form-item>

        <el-form-item prop="content" label="公告内容">
          <el-input type="textarea" v-model="form.content"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="noticeFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit">确 定</el-button>
      </div>
    </el-dialog> -->
  </div>
</template>

<script>
import {
  getLoginHistoryPage,
  removeLoginHistoryById,
  exportLoginHistoryPage,
} from "@/api/user";
import { exportExcel } from "@/utils/excel";

export default {
  data() {
    return {
      list: null,
      listLoading: true,
      form: {
        username: "",
        loginIp: "",
        loginAddress: "",
        loginDevice: "",
        current: 1,
        size: 10,
        total: 0,
      },
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
      sizes: [],
      selectionIds: [],
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
      getLoginHistoryPage(this.form).then((response) => {
        let data = response.data;
        this.form.current = data.current;
        this.form.size = data.size;
        this.form.total = data.total;
        this.list = data.records;
        this.listLoading = false;
      });
    },
    exportInfoPage() {
      exportLoginHistoryPage(this.form).then((resp) => {
        exportExcel(resp);
      });
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
        this.form.startTime = this.handleDateFormat(
          new Date(this.dateRange[0])
        );
        this.form.endTime = this.handleDateFormat(new Date(this.dateRange[1]));
      }
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
      this.$confirm("此操作将永久删除数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          if (this.selectionIds && this.selectionIds.length > 0) {
            id = [];
            for (let i = 0; i < this.selectionIds.length; i++)
              id.push(this.selectionIds[i]["id"]);
          }
          removeLoginHistoryById(id).then((resp) => {
            if (resp.code === 200) {
              this.$message.success("删除成功");
              this.fetchData();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dateRange = [];
    },
    handleSelectionChange(val) {
      this.selectionIds = val;
    },
    handleRowClick(row) {
      if (!row.disabled) {
        this.$refs.table.toggleRowSelection(row);
      }
    },
    searchForm() {
      this.form.current = 1;
      this.fetchData();
    },
  },
};
</script>

<style scoped>
.el-pagination {
  text-align: center;
}
.el-form-item {
  margin-bottom: 0 !important;
}
</style>