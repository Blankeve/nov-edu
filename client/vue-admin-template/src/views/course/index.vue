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

      <el-form-item prop="createTime">
        <el-date-picker
          v-model="form.createTime"
          type="datetime"
          placeholder="入驻时间"
          value-format="yyyy-MM-dd HH:mm:ss"
        >
        </el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
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

      <el-table-column label="课程标题" align="center">
        <template slot-scope="scope">
          {{ scope.row.courseTitle }}
        </template>
      </el-table-column>

  <el-table-column label="课程讲师" align="center">
        <template slot-scope="scope">
          {{ scope.row.teacherName }}
        </template>
      </el-table-column>

  <el-table-column label="课程分类" align="center">
        <template slot-scope="scope">
          {{ scope.row.subjectTitle }}
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

       <el-table-column label="销售数量" align="center">
        <template slot-scope="scope">
          {{ scope.row.courseBuyCount }}
        </template>
      </el-table-column>

       <el-table-column label="浏览数量" align="center">
        <template slot-scope="scope">
          {{ scope.row.courseViewCount }}
        </template>
      </el-table-column>

       <el-table-column label="level"  align="center">
        <template slot-scope="scope">
          {{ scope.row.courseStatus == 1 ? "已上架" : "未上架" }}
        </template>
      </el-table-column>

       <el-table-column label="课程简介" align="center">
        <template slot-scope="scope">
          {{ scope.row.courseId}}
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        prop="created_at"
        label="创建时间"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.courseCreateTime }}</span>
        </template>
      </el-table-column>

   <el-table-column
        align="center"
        prop="created_at"
        label="更新时间"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.courseUpdateTime }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="200">
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
import { getPage, removeById } from "@/api/course";

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
        createTime: "",
        current: 1,
        size: 8,
        total: 0,
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