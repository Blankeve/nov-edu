<template>
  <div class="app-container">
    <el-form :inline="true" ref="queryForm" :model="queryForm">


          <el-form-item prop="operName" label="操作人员">
            <el-input
              v-model="queryForm.operName"
              placeholder="操作人员"
            ></el-input>
          </el-form-item>



          <el-form-item prop="operIp" label="操作ip">
            <el-input
              v-model="queryForm.operIp"
              placeholder="操作ip"
            ></el-input>
          </el-form-item>
   

       
          <el-form-item prop="operAddr" label="操作地址">
            <el-input
              v-model="queryForm.operAddr"
              placeholder="操作地址"
            ></el-input>
          </el-form-item>
  

    
          <el-form-item prop="reqUrl" label="请求地址">
            <el-input
              v-model="queryForm.reqUrl"
              placeholder="请求地址"
            ></el-input>
          </el-form-item>
       

       
          <el-form-item prop="method" label="请求方式">
            <el-select v-model="queryForm.method" placeholder="请选择请求方式">
              <el-option label="全部" value=""> </el-option>
              <el-option label="GET" value="GET"> </el-option>
              <el-option label="POST" value="POST"> </el-option>
              <el-option label="PUT" value="PUT"> </el-option>
              <el-option label="DELETE" value="DELETE"> </el-option>
            </el-select>
          </el-form-item>
   
 

      <el-form-item label="请求时间" prop="dateRange">
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

    <el-table :data="list" v-loading="listLoading" style="width: 100%">
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

      <el-table-column label="请求方式" align="center">
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

      <el-table-column label="请求耗时" align="center">
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

      <el-table-column label="操作人" align="left">
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

      <el-table-column align="center" prop="created_at" label="操作时间">
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
</template>

<script>
import { getPage } from "@/api/oper";

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
  },
};
</script>

<style lang="less" >
.mid-input {
  width: 80px;
}
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
.red {
  color: rgb(245, 21, 21);
}
.green {
  color: rgb(121, 206, 121);
}
</style>