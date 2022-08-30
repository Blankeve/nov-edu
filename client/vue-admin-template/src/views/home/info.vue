<template>
  <div class="app-container">
    <div class="search_box">
      <el-form :inline="true" ref="form" :model="form" size="medium">
        <el-form-item prop="title">
          <el-input
            suffix-icon="el-icon-search"
            v-model="form.title"
            placeholder="文章标题"
            clearable
          ></el-input>
        </el-form-item>

        <el-form-item prop="cate">
          <el-select v-model="form.cate" placeholder="文章分类" clearable>
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

        <el-form-item prop="title">
          <el-input
            v-model="form.createrNickname"
            placeholder="作者"
            clearable
          ></el-input>
        </el-form-item>

        <el-form-item prop="createTime">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            :picker-options="pickerOptions"
            range-separator="至"
            start-placeholder="发布日期"
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
    </div>

    <div class="main_content" style="border-top: 2px solid #f0f0f0">
      <div class="btn-layout">
        <div>
          <el-button
            size="mini"
            plain
            type="primary"
            @click="handleAdd()"
            icon="el-icon-folder-add"
            >新增</el-button
          >
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
            @click="exportInfoPage"
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

        <el-table-column width="200" label="文章分类" align="center">
          <template slot-scope="scope">
            {{ scope.row.catename }}
          </template>
        </el-table-column>

        <el-table-column label="文章标题" align="center">
          <template slot-scope="scope">
            {{ scope.row.title }}
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
        <el-table-column
          align="center"
          prop="created_at"
          label="发布时间"
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
          label="更新时间"
          width="200"
        >
          <template slot-scope="scope">
            <i class="el-icon-time" />
            <span>{{ scope.row.updateTime }}</span>
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
import { getListByKey } from "@/api/config";
import { getPage, removeById, exportPage } from "@/api/info";
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
        current: 1,
        size: 8,
        total: 0,
      },
      cates: [],
      sizes: [],
      key: {
        key: "artcle_cate",
        grade: 2,
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
      selectionIds: [],
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
    exportInfoPage() {
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
              id.push(this.selectionIds[i]["id"]);
          }
          removeById(id).then((resp) => {
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
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.form.createrNickname = "";
      this.dateRange = [];
    },
    handleAdd() {
      this.$router.push({
        path: "/home/infoform",
      });
    },
    handleEdit() {
      let id = this.selectionIds[0]["id"];
      this.$router.push({
        path: "/home/info/edit",
        query: {
          info: id,
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
    searchForm() {
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