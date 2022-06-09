<template>
  <div class="app-container">
    <el-form :inline="true" ref="form" :model="form">
      <el-form-item label="资讯标题" prop="title">
        <el-input v-model="form.title" placeholder="资讯标题"></el-input>
      </el-form-item>

      <el-form-item prop="cate" label="资讯分类">
        <el-select v-model="form.cate" placeholder="请选择分类">
          <el-option label="全部" :key="0" :value="null"> </el-option>
          <el-option
            v-for="(item, index) in cates"
            :label="item.configName"
            :key="item.id"
            :value="item.configValue"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="作者" prop="title">
        <el-input v-model="form.createrNickname" placeholder="作者"></el-input>
      </el-form-item>

      <el-form-item label="发布时间" prop="createTime">
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

      <el-table-column width="200" label="资讯分类" align="center">
        <template slot-scope="scope">
          {{ scope.row.catename }}
        </template>
      </el-table-column>

      <el-table-column label="资讯标题" align="center">
        <template slot-scope="scope">
          {{ scope.row.title }}
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        prop="created_at"
        label="发布日期"
        width="200"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        prop="created_at"
        label="更新日期"
        width="200"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.updateTime }}</span>
        </template>
      </el-table-column>

      <el-table-column width="100" label="点击量" align="center">
        <template slot-scope="scope">
          {{ scope.row.clickCount }}
        </template>
      </el-table-column>

      <el-table-column width="100" label="作者" align="center">
        <template slot-scope="scope">
          {{ scope.row.createrNickname }}
        </template>
      </el-table-column>

      <el-table-column width="100" label="更新人" align="center">
        <template slot-scope="scope">
          {{ scope.row.updaterNickname }}
        </template>
      </el-table-column>

      <el-table-column fixed="right" align="center" label="操作" width="220">
        <template slot-scope="scope">
          <el-button
            type="info"
            @click="handleEdit(scope.row.id)"
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
import { getListByKey } from "@/api/config";
import { getPage, removeById } from "@/api/info";
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
        current: 1,
        size: 8,
        total: 0,
      },
      cates: [],
      sizes: [],
      key: {
        key: "info_cate",
        grade: 2
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
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      this.handleDateRange();
      getListByKey(this.key).then((resp) => (this.cates = resp.data));
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
      removeById(id).then((resp) => {
        if (resp.code === 200) {
          this.$message.success("删除成功");
          this.fetchData();
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.form.createrNickname = "";
      this.dateRange = [];
    },
    handleEdit(data) {
      this.$router.push({
        path: "/home/info/edit",
        query: {
          info: data,
        },
      });
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