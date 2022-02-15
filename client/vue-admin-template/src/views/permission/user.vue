<template>
  <div class="app-container">
    <el-form :inline="true" ref="form" :model="form">
      <el-form-item prop="name">
        <el-input
          class="mid-input"
          v-model="form.name"
          placeholder="姓名"
        ></el-input>
      </el-form-item>

      <el-form-item prop="level">
        <el-select v-model="form.level" placeholder="讲师等级">
          <el-option label="高级讲师" value="1"></el-option>
          <el-option label="首席讲师" value="2"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="入驻日期" prop="createTime">
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
        <el-button type="primary" @click="onSubmit">查询</el-button>
        <el-button @click="resetForm('form')">重置</el-button>
        <el-button type="success" @click="exportTeacherPage"
          >导出当前</el-button
        >
        <el-button type="success" @click="exportAllTeacher">导出所有</el-button>
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

      <el-table-column width="100" label="name" align="center">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>

      <el-table-column label="avatar" width="120" align="center">
        <template slot-scope="scope">
          <div class="demo-image__preview">
            <el-image
              :src="scope.row.avatar"
              alt="图片获取失败"
              title="点击查看大图"
              width="100px"
              :preview-src-list="[scope.row.avatar]"
            />
          </div>
        </template>
      </el-table-column>

      <el-table-column label="career" align="center">
        <template slot-scope="scope">
          {{ scope.row.career }}
        </template>
      </el-table-column>

      <el-table-column label="intro" width="500" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.intro }}</span>
        </template>
      </el-table-column>

      <el-table-column width="100" label="level" align="center">
        <template slot-scope="scope">
          {{ scope.row.level == 1 ? "高级讲师" : "首席讲师" }}
        </template>
      </el-table-column>

      <el-table-column width="50" label="显示级别" align="center">
        <template slot-scope="scope">
          {{ scope.row.sort }}
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        prop="created_at"
        label="joinDate"
        width="100"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>

      <el-table-column fixed="right" align="center" label="操作" width="200">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row.id)">编辑</el-button>

          <el-button type="danger" @click="handleDelete(scope.row.id)"
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
  </div>
</template>

<script>
import { getList, removeById, exportAll, exportPage } from "@/api/teacher";
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
        name: "",
        level: "",
        current: 1,
        size: 8,
        total: 0,
        startTime: null,
        endTime: null,
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
      sizes: [],
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
        this.form.size > 1
          ? [this.form.size / 2, this.form.size, this.form.size * 2]
          : [this.form.size, this.form.size * 2];
      getList(this.form).then((response) => {
        let data = response.data;
        this.form.current = data.current;
        this.form.size = data.size;
        this.form.total = data.total;
        this.list = data.records;
        this.listLoading = false;
      });
    },
    exportTeacherPage() {
      exportPage(this.form).then((resp) => {
        exportExcel(resp);
      });
    },
    exportAllTeacher() {
      exportAll().then((resp) => {
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
      removeById(id).then((response) => {
        this.fetchData();
      });
    },
    handleEdit(id) {
      this.$router.push({
        path: "/teacher/edit",
        query: {
          id: id,
        },
      });
    },
    onSubmit() {
      this.fetchData();
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