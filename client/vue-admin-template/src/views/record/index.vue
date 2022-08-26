<template>
  <div class="app-container">
    <div class="search_box">
      <el-form :inline="true" ref="form" :model="form" size="medium">
        <el-form-item prop="name">
          <el-input
            suffix-icon="el-icon-search"
            v-model="form.nickname"
            placeholder="用户昵称"
            clearable=""
          ></el-input>
        </el-form-item>

        <el-form-item prop="name">
          <el-input
            suffix-icon="el-icon-search"
            v-model="form.courseTitle"
            placeholder="课程名称"
            clearable=""
          ></el-input>
        </el-form-item>

        <el-form-item prop="name">
          <el-input
            suffix-icon="el-icon-search"
            v-model="form.videoTitle"
            placeholder="视频名称"
            clearable=""
          ></el-input>
        </el-form-item>

        <el-form-item label="观看日期" prop="createTime">
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
        <div></div>
        <div>
          <el-button
            size="mini"
            type="success"
            plain
            icon="el-icon-download"
            @click="exportRecordPage"
            >导出</el-button
          >
        </div>
      </div>
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

        <el-table-column label="用户昵称" width="200px" align="center">
          <template slot-scope="scope">
            {{ scope.row.nickname }}
          </template>
        </el-table-column>

        <el-table-column label="课程名称" width="200px" align="center">
          <template slot-scope="scope">
            {{ scope.row.courseTitle }}
          </template>
        </el-table-column>
        <el-table-column label="课程封面" width="200px" align="center">
          <template slot-scope="scope">
            <div class="demo-image__preview">
              <el-image
                :src="scope.row.courseCover"
                alt="图片获取失败"
                title="点击查看大图"
                width="100px"
                :preview-src-list="[scope.row.courseCover]"
              />
            </div>
          </template>
        </el-table-column>

        <el-table-column label="章节/小节" width="100px" align="center">
          <template slot-scope="scope">
            {{ scope.row.chapterSort }}/{{ scope.row.videoSort }}
          </template>
        </el-table-column>

        <el-table-column label="视频名称" width="200px" align="center">
          <template slot-scope="scope">
            {{ scope.row.videoTitle }}
          </template>
        </el-table-column>

        <el-table-column align="center" prop="created_at" label="观看日期">
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
import { getStudyRecordPage, exportPage } from "@/api/record";
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
        nickname: "",
        courseTitle: "",
        videoTitle: "",
        current: 1,
        size: 12,
        total: 0,
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
      getStudyRecordPage(this.form).then((response) => {
        let data = response.data;
        this.form.current = data.current;
        this.form.size = data.size;
        this.form.total = data.total;
        this.list = data.records;
        this.listLoading = false;
      });
    },
    exportRecordPage() {
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
    searchForm() {
      this.fetchData();
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.form.nickname = "";
      this.form.courseTitle = "";
      this.form.videoTitle = "";
      this.dateRange = [];
    },
    onSubmit() {
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