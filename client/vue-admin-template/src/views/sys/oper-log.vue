<template>
  <div class="app-container">
    <div class="search_box">
      <el-form :inline="true" ref="queryForm" :model="queryForm" size="medium">
        <el-form-item prop="operName">
          <el-input
            suffix-icon="el-icon-search"
            v-model="queryForm.operName"
            placeholder="操作人员"
            clearable
          ></el-input>
        </el-form-item>

        <el-form-item prop="operIp">
          <el-input
            suffix-icon="el-icon-search"
            v-model="queryForm.operIp"
            placeholder="操作ip"
            clearable
          ></el-input>
        </el-form-item>

        <el-form-item prop="operAddr">
          <el-input
            suffix-icon="el-icon-search"
            v-model="queryForm.operAddr"
            placeholder="操作地址"
            clearable
          ></el-input>
        </el-form-item>

        <el-form-item prop="reqUrl">
          <el-input
            suffix-icon="el-icon-search"
            v-model="queryForm.reqUrl"
            placeholder="请求地址"
            clearable
          ></el-input>
        </el-form-item>

        <el-form-item prop="method">
          <el-select
            style="width: 150px"
            v-model="queryForm.method"
            placeholder="请求方式"
            clearable
          >
            <el-option label="GET" value="GET"> </el-option>
            <el-option label="POST" value="POST"> </el-option>
            <el-option label="PUT" value="PUT"> </el-option>
            <el-option label="DELETE" value="DELETE"> </el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="dateRange">
          <el-date-picker
            style="width: 300px"
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
            @click="exportOperPage"
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
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="请求参数">
                <span>{{ scope.row.reqArgs }}</span>
              </el-form-item>

              <el-form-item label="请求结果">
                <span>{{ scope.row.reqResult }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column label="日志编号" align="left">
          <template slot-scope="scope">
            <span>{{ scope.row.id }}</span>
          </template>
        </el-table-column>

        <el-table-column label="请求方式" align="center" width="100">
          <template slot-scope="scope">
            <el-tag size="medium">
              {{ scope.row.method }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="请求地址" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.reqUrl }}</span>
          </template>
        </el-table-column>

        <el-table-column label="请求类名" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.reqClass }}</span>
          </template>
        </el-table-column>

        <el-table-column label="请求方法" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.reqMethod }}</span>
          </template>
        </el-table-column>

        <el-table-column label="请求耗时" align="center" width="100">
          <template slot-scope="scope">
            <span
              :class="{
                red: scope.row.reqTimeSpend >= 1000,
                green: scope.row.reqTimeSpend < 1000,
              }"
              >{{ scope.row.reqTimeSpend }}ms</span
            >
          </template>
        </el-table-column>

        <el-table-column label="操作人" align="left" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.operName }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作地址" align="left">
          <template slot-scope="scope">
            <span>{{ scope.row.operAddr }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作ip" align="left">
          <template slot-scope="scope">
            <span>{{ scope.row.operIp }}</span>
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          prop="created_at"
          label="操作时间"
          width="200"
        >
          <template slot-scope="scope">
            <i class="el-icon-time" />
            <span>{{ scope.row.reqTime }}</span>
          </template>
        </el-table-column>
      </el-table>

      <div class="block">
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryForm.current"
          :page-sizes="sizes"
          :page-size="queryForm.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="queryForm.total"
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import { getPage, removeById, exportPage } from "@/api/oper";

import { exportExcel } from "@/utils/excel";
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
      queryForm: {
        reqUrl: "",
        operName: "",
        operIp: "",
        operAddr: "",
        method: "",
        current: 1,
        size: 8,
        total: 0,
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
      selectionIds: [],
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      this.listLoading = true;
      this.handleDateRange();
      this.sizes =
        this.queryForm.size > 1
          ? [
              this.queryForm.size / 2,
              this.queryForm.size,
              this.queryForm.size * 2,
            ]
          : [this.queryForm.size, this.queryForm.size * 2];
      getPage(this.queryForm).then((response) => {
        let data = response.data;
        this.queryForm.current = data.current;
        this.queryForm.size = data.size;
        this.queryForm.total = data.total;
        this.list = data.records;
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
    handleCurrentChange(p) {
      this.queryForm.current = p;
      this.fetchData();
    },
    handleSizeChange(s) {
      this.queryForm.size = s;
      this.fetchData();
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
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dateRange = [];
    },
    exportOperPage() {
      exportPage(this.queryForm).then((resp) => {
        exportExcel(resp);
      });
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
          removeById(id).then((resp) => {
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

<style lang="less" scoped>
.el-pagination {
  text-align: center;
}
.red {
  color: rgb(245, 21, 21);
}
.green {
  color: rgb(121, 206, 121);
}
.el-form-item {
  margin-bottom: 0 !important;
}
</style>