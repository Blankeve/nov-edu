<template>
  <div class="app-container">
    <el-form :inline="true" ref="form" :model="form">
      <el-form-item prop="name">
        <el-input v-model="form.nickname" placeholder="用户昵称"></el-input>
      </el-form-item>

      <el-form-item prop="name">
        <el-input v-model="form.courseTitle" placeholder="课程名称"></el-input>
      </el-form-item>

      <el-form-item prop="name">
        <el-input v-model="form.videoTitle" placeholder="视频名称"></el-input>
      </el-form-item>

      <el-form-item label="观看日期" prop="createTime">
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
        <el-button type="primary" icon="el-icon-search" @click="onSubmit"
          >查询</el-button
        >
        <el-button
          type="danger"
          icon="el-icon-refresh-left"
          @click="resetForm('form')"
          >重置</el-button
        >
        <!-- <el-button
          type="success"
          icon="el-icon-download"
          @click="exportTeacherPage"
          >导出当前</el-button
        > -->
        <!-- <el-button type="success" icon="el-icon-download" @click="exportAllTeacher">导出所有</el-button> -->
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
import { getStudyRecordPage } from "@/api/video";
import store from "@/store";
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
        title: "",
        content: "",
        sendUser: "",
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
