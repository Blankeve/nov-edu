<template>
  <div class="app-container">
    <el-form :inline="true" ref="form" :model="form">
      <el-form-item label="小节名称" prop="title">
        <el-input v-model="form.title" placeholder="小节名称"></el-input>
      </el-form-item>

      <el-form-item label="所属章节">
        <el-select v-model="form.chapterId" placeholder="请选择章节">
          <el-option label="所有章节" key="" value=""> </el-option>
          <el-option
            v-for="(item, index) in chapters"
            :label="item.title"
            :key="item.id"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="第几小节">
        <el-input-number
          v-model="form.sort"
          :min="0"
          :max="10"
          label="描述文字"
        ></el-input-number>
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

      <el-table-column width="320px" label="源视频" align="center">
        <template slot-scope="scope">
          <video width="320" controls>
            <source :src="scope.row.videoSourcePath" type="video/mp4" />
            您的浏览器不支持 HTML5 video 标签。
          </video>
        </template>
      </el-table-column>

      <el-table-column width="100px" label="小节标题" align="center">
        <template slot-scope="scope">
          {{ scope.row.videoTitle }}
        </template>
      </el-table-column>

      <el-table-column label="第几小节" align="center">
        <template slot-scope="scope">
          {{ scope.row.videoSort }}
        </template>
      </el-table-column>

      <el-table-column width="200px" label="视频路径" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.videoSourcePath }}</span>
        </template>
      </el-table-column>

      <el-table-column label="视频时长/秒" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.videoDuration }}</span>
        </template>
      </el-table-column>

      <el-table-column label="视频大小/MB" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.videoSize }}</span>
        </template>
      </el-table-column>

      <el-table-column label="是否试听" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.videoIsFree == 1 ? "是" : "否" }}</span>
        </template>
      </el-table-column>

      <el-table-column label="播放次数" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.videoPlayCount }}</span>
        </template>
      </el-table-column>

      <el-table-column label="视频状态" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.videoStatus }}</span>
        </template>
      </el-table-column>

      <!-- <el-table-column label="课程讲师" align="center">
        <template slot-scope="scope">
          {{ scope.row.teacherName }}
        </template>
      </el-table-column>

      <el-table-column label="所属课程" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.courseTitle }}</span>
        </template>
      </el-table-column> -->

      <el-table-column align="center" prop="created_at" label="创建时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.videoCreateTime }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="created_at" label="更新时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.videoUpdateTime }}</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" align="center" label="操作" width="100">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row.videoId)">编辑</el-button>

          <el-button
            type="danger"
            @click="handleDelete(scope.$index, scope.row.videoId)"
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
import { getPage, removeVideoById } from "@/api/video";
import { getChapterList } from "@/api/chapter";

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
        chapterId: null,
        sort: null,
        createTime: "",
        current: 1,
        size: 8,
        total: 0,
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
      sizes: [],
      chapters: [],
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      this.listLoading = true;
      this.handleDateRange();
      getChapterList().then((resp) => {
        if (resp.code === 200) {
          this.chapters = resp.data;
        }
      });
      let chapterId = this.$route.query.chapter;
      if (chapterId) this.form.chapterId = chapterId;

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
      removeVideoById(id).then((resp) => {
        if (resp.code === 200) {
          this.$message.success("删除成功");
          this.list.splice(row, 1);
        }
      });
    },
    handleEdit(id) {
      this.$router.push({
        path: "/video/edit",
        query: {
          video: id,
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
    searchForm() {
      this.fetchData();
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