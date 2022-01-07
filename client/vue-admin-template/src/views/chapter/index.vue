<template>
  <div class="app-container">
    <el-form :inline="true" ref="form" :model="form">
      <el-form-item label="章节名称" prop="title">
        <el-input v-model="form.title" placeholder="章节名称"></el-input>
      </el-form-item>

      <el-form-item label="所属课程">
        <el-select v-model="form.courseId" placeholder="请选择课程">
          <el-option label="所有课程" key="" value=""> </el-option>
          <el-option
            v-for="(item, index) in courses"
            :label="item.title"
            :key="item.id"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="第几章节">
        <el-input-number
          v-model="form.sort"
          :min="0"
          :max="10"
          label="描述文字"
        ></el-input-number>
      </el-form-item>

      <el-form-item label="添加时间" prop="createTime">
        <el-date-picker
          v-model="form.createTime"
          type="datetime"
          placeholder="课程添加时间"
          value-format="yyyy-MM-dd HH:mm:ss"
        >
        </el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="searchForm">查询</el-button>
        <el-button @click="resetForm('form')">重置</el-button>
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

      <el-table-column label="所属课程" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.courseTitle }}</span>
        </template>
      </el-table-column>

      <el-table-column label="课程讲师" align="center">
        <template slot-scope="scope">
          {{ scope.row.teacherName }}
        </template>
      </el-table-column>

      <el-table-column label="章节标题" align="center">
        <template slot-scope="scope">
          {{ scope.row.chapterTitle }}
        </template>
      </el-table-column>

      <el-table-column label="第几章节" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.chapterSort }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="视频数量" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.videoQty }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="created_at" label="创建时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.chapterCreateTime }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="created_at" label="更新时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.chapterUpdateTime }}</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" align="center" label="操作" width="170">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row.chapterId)">编辑</el-button>
          <el-popconfirm
            :title="
              (scope.row.videoQty > 0 ? '该章节下视频不为空,' : '') +
              '确定删除吗？'
            "
            @onConfirm="handleDelete(scope.$index, scope.row.chapterId)"
          >
            <el-button slot="reference" type="danger">删除</el-button>
          </el-popconfirm>

          <el-button type="text" @click="addVideo(scope.row.chapterId)"
            >添加小节</el-button
          >
          <el-button
            v-if="scope.row.videoQty > 0"
            type="text"
            @click="watchVideo(scope.row.chapterId)"
            >查看小节</el-button
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
import { getPage, removeChapterById } from "@/api/chapter";
import { getList } from "@/api/course";

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
        sort: null,
        courseId: null,
        createTime: "",
        current: 1,
        size: 8,
        total: 0,
      },
      sizes: [],
      courses: [],
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      getList().then((resp) => {
        if (resp.code === 200) {
          this.courses = resp.data;
        }
      });
      let courseId = this.$route.query.course;
      if (courseId) this.form.courseId = courseId;
      this.listLoading = true;
      this.sizes =
        this.form.size > 1
          ? [this.form.size / 2, this.form.size, this.form.size * 2]
          : [this.form.size, this.form.size * 2];
      getPage(this.form).then((response) => {
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
    handleDelete(row, id) {
      removeChapterById(id).then((resp) => {
        if (resp.code === 200) {
          this.$message.success("删除成功");
          this.list.splice(row, 1);
        }
      });
    },
    handleEdit(id) {
      this.$router.push({
        path: "/chapter/edit",
        query: {
          chapter: id,
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
    },
    watchVideo(data) {
      this.$router.push({
        path: "/video/list",
        query: {
          chapter: data,
        },
      });
    },
    addVideo(data) {
      this.$router.push({
        path: "/video/save",
        query: {
          chapter: data,
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