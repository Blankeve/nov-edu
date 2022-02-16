<template>
  <div class="app-container">
    <el-form :inline="true" ref="form" :model="form">
      <el-form-item label="用户昵称" prop="title">
        <el-input v-model="form.nickname" placeholder="用户昵称"></el-input>
      </el-form-item>

      <el-form-item label="课程讲师">
        <el-select v-model="form.teacherId" placeholder="请选择讲师">
          <el-option label="所有讲师" key="" value=""> </el-option>
          <el-option
            v-for="(item, index) in teachers"
            :label="item.name"
            :key="item.id"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="所属课程">
        <el-select v-model="form.courseId" placeholder="请选择课程">
          <el-option label="请选择课程" key="" value=""> </el-option>
          <el-option
            v-for="(item, index) in courses"
            :label="item.title"
            :key="item.id"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="创建时间" prop="createTime">
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
        <el-button type="primary" @click="searchForm">查询</el-button>
        <el-button @click="resetForm('form')">重置</el-button>
        <el-button type="success" @click="exportOrderPage">导出当前</el-button>
        <el-button type="success" @click="exportAllOrder">导出所有</el-button>
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
      <el-table-column align="center" label="#" width="50">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="用户昵称" align="center">
        <template slot-scope="scope">
          {{ scope.row.nickname }}
        </template>
      </el-table-column>

      <el-table-column label="用户手机" align="center">
        <template slot-scope="scope">
          {{ scope.row.mobile }}
        </template>
      </el-table-column>

      <el-table-column label="购买课程" width="200px" align="center">
        <template slot-scope="scope">
          {{ scope.row.courseTitle }}
        </template>
      </el-table-column>

      <el-table-column label="课程讲师" align="center">
        <template slot-scope="scope">
          {{ scope.row.teacherName }}
        </template>
      </el-table-column>

      <el-table-column label="课程价格" width="200px" align="center">
        <template slot-scope="scope">
          {{ scope.row.totalFee }}
        </template>
      </el-table-column>

      <el-table-column label="支付状态" width="200px" align="center">
        <template slot-scope="scope">
          {{ scope.row.status === 1 ? "已支付" : "未支付" }}
        </template>
      </el-table-column>

      <el-table-column label="支付方式" width="200px" align="center">
        <template slot-scope="scope" v-if="scope.row.status === 1">
          {{ scope.row.payType === 1 ? "微信支付" : "支付宝" }}
        </template>
      </el-table-column>

      <el-table-column align="center" prop="created_at" label="创建时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>

      <el-table-column fixed="right" align="center" label="操作" width="170">
        <template slot-scope="scope">
          <el-popconfirm
            title="
              确定删除吗？
            "
            @onConfirm="handleDelete(scope.$index, scope.row.id)"
          >
            <el-button slot="reference" type="danger">删除</el-button>
          </el-popconfirm>
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
  </div>
</template>

<script>
import { getList } from "@/api/course";
import { getAll } from "@/api/teacher";
import {
  getOrderPage,
  removeOrderById,
  exportAll,
  exportPage,
} from "@/api/order";
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
      list: null,
      listLoading: true,
      form: {
        courseId: null,
        createTime: "",
        status: 0,
        current: 1,
        size: 8,
        total: 0,
        teacherId: "",
      },
      dateRange: [],
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
      courses: [],
      teachers: [],
      sizes: [],
    };
  },
  created() {
    this.getOptions();
    this.fetchData();
  },
  methods: {
    getOptions() {
      getAll().then((resp) => {
        if (resp.code === 200) {
          this.teachers = resp.data;
        }
      });
      getList().then((resp) => {
        if (resp.code === 200) {
          this.courses = resp.data;
        }
      });
    },
    fetchData() {
      this.listLoading = true;
      this.handleDateRange();
      this.sizes =
        this.form.size > 1
          ? [this.form.size / 2, this.form.size, this.form.size * 2]
          : [this.form.size, this.form.size * 2];
      if (this.subjectId && this.subjectId.length > 0)
        this.form.subjectId = this.subjectId[this.subjectId.length - 1];
      getOrderPage(this.form).then((response) => {
        let data = response.data;
        this.form.current = data.current;
        this.form.size = data.size;
        this.form.total = data.total;
        this.list = data.records;
        this.listLoading = false;
      });
    },
    exportOrderPage() {
      exportPage(this.form).then((resp) => {
        exportExcel(resp);
      });
    },
    exportAllOrder() {
      exportAll(this.form).then((resp) => {
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
    handleDelete(row, id) {
      console.log(id);
      this.$message.error("暂时不支持删除订单!");
    },
    handleEdit(data) {
      this.$router.push({
        path: "/course/edit",
        query: {
          course: data,
        },
      });
    },
    onSubmit() {
      this.fetchData();
    },
    searchForm() {
      this.fetchData();
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.subjectId = null;
      this.form.subjectId = null;
      this.form.teacherId = "";
      this.form.status = "";
      this.dateRange = [];
    },
    watchChapter(data) {
      this.$router.push({
        path: "/chapter/list",
        query: {
          course: data,
        },
      });
    },
    addChapter(data) {
      this.$router.push({
        path: "/chapter/save",
        query: {
          course: data,
        },
      });
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