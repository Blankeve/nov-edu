<template>
  <div class="app-container">
    <div class="search_box">
      <el-form :inline="true" ref="form" :model="form" size="medium">
        <el-form-item prop="title">
          <el-input
            suffix-icon="el-icon-search"
            v-model="form.nickname"
            placeholder="用户昵称"
            clearable
          ></el-input>
        </el-form-item>

        <el-form-item v-if="code !== 5">
          <el-select v-model="form.teacherId" placeholder="课程讲师" clearable>
            <el-option
              v-for="(item, index) in teachers"
              :label="item.name"
              :key="item.id"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-select v-model="form.courseId" placeholder="课程名称" clearable>
            <el-option
              v-for="(item, index) in courses"
              :label="item.title"
              :key="item.id"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="createTime">
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
          <el-button
            size="small"
            type="primary"
            icon="el-icon-search"
            @click="searchForm"
            >查询</el-button
          >
          <el-button
            size="small"
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
            @click="exportOrderPage"
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

        <el-table-column label="用户昵称" align="center">
          <template slot-scope="scope">
            {{ scope.row.nickname }}
          </template>
        </el-table-column>

        <el-table-column v-if="code !== 5" label="用户名" align="center">
          <template slot-scope="scope">
            {{ scope.row.username }}
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

        <el-table-column label="课程价格" align="center">
          <template slot-scope="scope">
            {{ scope.row.totalFee }}
          </template>
        </el-table-column>

        <el-table-column align="center" prop="created_at" label="创建时间">
          <template slot-scope="scope">
            <i class="el-icon-time" />
            <span>{{ scope.row.createTime }}</span>
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
  </div>
</template>

<script>
import { getList } from "@/api/course";
import { getAll } from "@/api/teacher";
import { getOrderPage, exportPage } from "@/api/order";
import { exportExcel } from "@/utils/excel";
import { mapGetters } from "vuex";

export default {
  computed: {
    ...mapGetters(["sidebar", "avatar", "name", "role", "code"]),
  },
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
      selectionIds: [],
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
      this.$message.error("暂时不支持删除订单!");
    },
    handleSelectionChange(val) {
      this.selectionIds = val;
    },
    handleRowClick(row) {
      if (!row.disabled) {
        this.$refs.table.toggleRowSelection(row);
      }
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

<style scoped>
.mid-input {
  width: 80px;
}
.el-pagination {
  text-align: center;
}
.el-form-item {
  margin-bottom: 0 !important;
}
</style>