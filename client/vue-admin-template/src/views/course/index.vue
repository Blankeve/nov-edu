<template>
  <div class="app-container">
    <el-form :inline="true" ref="form" :model="form">
      <el-form-item label="课程名称" prop="title">
        <el-input v-model="form.title" placeholder="课程名称"></el-input>
      </el-form-item>

      <el-form-item label="课程分类">
        <el-cascader
          v-model="subjectId"
          :options="subjects"
          :props="{ expandTrigger: 'hover', label: 'title', value: 'id' }"
        ></el-cascader>
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

      <el-form-item label="课程状态">
        <el-select v-model="form.status" placeholder="请选择">
          <el-option label="已上架" key="1" value="1"> </el-option>
          <el-option label="已下架" key="0" value="0"> </el-option>
          <el-option label="全部" key="" value=""> </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="添加时间" prop="createTime">
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
        <el-button type="primary" icon="el-icon-search" @click="searchForm"
          >查询</el-button
        >
        <el-button
          type="danger"
          icon="el-icon-refresh-left"
          @click="resetForm('form')"
          >重置</el-button
        >
        <el-button
          type="success"
          icon="el-icon-download"
          @click="exportCoursePage"
          >导出当前</el-button
        >
        <el-button
          type="success"
          icon="el-icon-download"
          @click="exportAllCourse"
          >导出所有</el-button
        >
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

      <el-table-column label="课程类别" align="center">
        <template slot-scope="scope">
          {{ scope.row.subjectTitle }}
        </template>
      </el-table-column>

      <el-table-column label="课程标题" width="200" align="center">
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
          {{ scope.row.coursePrice }}
        </template>
      </el-table-column>

      <el-table-column label="课程课时" align="center">
        <template slot-scope="scope">
          {{ scope.row.courseLessonNum }}
        </template>
      </el-table-column>

      <el-table-column label="课程封面" width="120" align="center">
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

      <el-table-column label="学习人数" align="center">
        <template slot-scope="scope">
          {{ scope.row.courseBuyCount }}
        </template>
      </el-table-column>

      <el-table-column label="播放数量" align="center">
        <template slot-scope="scope">
          {{ scope.row.courseViewCount }}
        </template>
      </el-table-column>

      <el-table-column label="评论数量" align="center">
        <template slot-scope="scope">
          {{ scope.row.courseCommentCount }}
        </template>
      </el-table-column>

      <el-table-column align="center" prop="created_at" label="创建时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.courseCreateTime }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="created_at" label="更新时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.courseUpdateTime }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="章节数量" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.chapterQty }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="视频数量" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.videoQty }}</span>
        </template>
      </el-table-column>

      <el-table-column label="课程发布" width="150" align="center">
        <template slot-scope="scope">
          <el-button
            size="small"
            :type="scope.row.courseStatus == 1 ? 'danger' : 'primary'"
            :icon="
              'el-icon-circle-' +
              (scope.row.courseStatus == 1 ? 'close' : 'check')
            "
            @click="handleRelease(scope.row.courseId, scope.row.courseStatus)"
          >
            {{
              scope.row.courseStatus == 1 ? "下架课程" : "上架课程"
            }}</el-button
          >
        </template>
      </el-table-column>

      <el-table-column fixed="right" align="center" label="操作" width="250">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row.courseId)" icon="el-icon-edit">编辑</el-button>
          <el-popconfirm
            :title="
              (scope.row.chapterQty > 0 ? '该课程下章节不为空,' : '') +
              '确定删除吗？'
            "
            @onConfirm="handleDelete(scope.$index, scope.row.courseId)"
          >
            <el-button slot="reference" type="danger" icon="el-icon-delete">删除</el-button>
          </el-popconfirm>

          <el-button type="text" @click="addChapter(scope.row.courseId)"
            >添加章节</el-button
          >
          <el-button
            v-if="scope.row.chapterQty > 0"
            type="text"
            @click="watchChapter(scope.row.courseId)"
            >查看章节</el-button
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
import {
  getPage,
  removeById,
  exportAll,
  exportPage,
  release,
} from "@/api/course";
import { getAll } from "@/api/teacher";
import { getList } from "@/api/subject";
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
        title: "",
        subjectId: null,
        courseId: null,
        createTime: "",
        status: "",
        current: 1,
        size: 8,
        total: 0,
        teacherId: "",
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
      subjectId: [],
      teachers: [],
      subjects: [],
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
          this.subjects = resp.data.subjects;
        }
      });
    },
    exportCoursePage() {
      exportPage(this.form).then((resp) => {
        exportExcel(resp);
      });
    },
    exportAllCourse() {
      exportAll().then((resp) => {
        exportExcel(resp);
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
      getPage(this.form).then((response) => {
        let data = response.data;
        this.form.current = data.current;
        this.form.size = data.size;
        this.form.total = data.total;
        this.list = data.records;
        this.listLoading = false;
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
      removeById(id).then((resp) => {
        if (resp.code === 200) {
          this.$message.success("删除成功");
          this.list.splice(row, 1);
        }
      });
    },
    handleEdit(data) {
      this.$router.push({
        path: "/course/edit",
        query: {
          course: data,
        },
      });
    },
    handleRelease(id, status) {
      status = status == 1 ? 0 : 1;
      release({ id: id, status: status }).then((resp) => {
        if (resp.code === 200) {
          this.$message.success((status == 1 ? "上架" : "下架") + "成功");
          this.fetchData();
        }
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