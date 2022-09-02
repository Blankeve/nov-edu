<template>
  <div class="app-container">
    <div class="search_box">
      <el-form :inline="true" ref="form" :model="form" size="medium">
        <el-form-item prop="title">
          <el-input
            suffix-icon="el-icon-search"
            v-model="form.title"
            placeholder="小节名称"
            clearable=""
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-select
            v-model="form.chapterId"
            placeholder="所属章节"
            clearable=""
          >
            <el-option
              v-for="(item, index) in chapters"
              :label="item.title"
              :key="item.id"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-input-number
            v-model="form.sort"
            :min="0"
            :max="10"
            placeholder="第几小节"
          ></el-input-number>
        </el-form-item>

        <el-form-item label="新增时间" prop="createTime">
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
            size="mini"
            :disabled="selectionIds.length != 1"
            plain
            type="warning"
            @click="handleEdit()"
            icon="el-icon-edit"
            >编辑</el-button
          >
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
            @click="exportVideoPage"
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
        <el-table-column type="expand" label="观看">
          <template slot-scope="scope">
            <video width="320" controls>
              <source :src="scope.row.videoSourcePath" type="video/mp4" />
              您的浏览器不支持 HTML5 video 标签。
            </video>
          </template>
        </el-table-column>

        <el-table-column width="100px" label="小节标题" align="center">
          <template slot-scope="scope">
            <el-button
              type="text"
              @click.stop="handleEdit(scope.row.videoId)"
              >{{ scope.row.videoTitle }}</el-button
            >
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
            <el-tag :type="scope.row.videoStatus == 1 ? 'success' : 'danger'">{{
              scope.row.videoStatus == 1 ? "正常" : "异常"
            }}</el-tag>
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

        <el-table-column
          width="200"
          align="center"
          prop="created_at"
          label="创建时间"
        >
          <template slot-scope="scope">
            <i class="el-icon-time" />
            <span>{{ scope.row.videoCreateTime }}</span>
          </template>
        </el-table-column>

        <el-table-column
          width="200"
          align="center"
          prop="created_at"
          label="更新时间"
        >
          <template slot-scope="scope">
            <i class="el-icon-time" />
            <span>{{ scope.row.videoUpdateTime }}</span>
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
import { getPage, removeVideoById, exportPage } from "@/api/video";
import { getChapterList } from "@/api/chapter";
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
        chapterId: null,
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
      selectionIds: [],
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
    exportVideoPage() {
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
      this.$confirm("此操作将永久删除数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          if (this.selectionIds && this.selectionIds.length > 0) {
            id = [];
            for (let i = 0; i < this.selectionIds.length; i++)
              id.push(this.selectionIds[i]["videoId"]);
          }
          removeVideoById(id).then((resp) => {
            if (resp.code === 200) {
              this.$message.success("删除成功");
              this.fetchData();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    handleEdit(id) {
      if (!id) id = this.selectionIds[0]["videoId"];
      this.$router.push({
        path: "/video/edit",
        query: {
          video: id,
        },
      });
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
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dateRange = [];
      this.form.chapterId = null;
      this.form.sort = null;
    },
    searchForm() {
      this.form.current = 1;
      this.fetchData();
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