<template>
  <div class="app-container">
    <!-- <el-button icon="el-icon-plus" type="primary" @click="addNotice"
      >添加公告</el-button
    > -->
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
      :row-style="{ height: 80 + 'px' }"
    >
      <el-table-column align="center" label="#" width="50">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column width="100" label="用户名" align="center">
        <template slot-scope="scope">
          {{ scope.row.username }}
        </template>
      </el-table-column>

      <el-table-column label="登录ip" align="center">
        <template slot-scope="scope">
          {{ scope.row.loginIp }}
        </template>
      </el-table-column>

      <el-table-column label="登录地址" align="center">
        <template slot-scope="scope">
          {{ scope.row.loginAddress }}
        </template>
      </el-table-column>

      <el-table-column label="登录设备" align="center">
        <template slot-scope="scope">
          {{ scope.row.loginDevice }}
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        prop="created_at"
        label="登录时间"
        width="200"
      >
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
import { getLoginHistoryPage } from "@/api/user";
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
      getLoginHistoryPage(this.form).then((response) => {
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
    // handleDelete(id) {
    //   removeById(id).then((response) => {
    //     this.$message.success("删除成功");
    //     this.fetchData();
    //   });
    // },
    // addNotice() {
    //   this.form.title = "";
    //   this.form.content = "";
    //   this.form.sendUser = "";
    //   this.noticeFormTitle = "添加公告";
    //   this.noticeFormVisible = true;
    // },
    // handleEdit(row) {
    //   this.noticeFormTitle = "编辑公告";
    //   this.form.id = row.id;
    //   this.form.sendUser = row.sendUser;
    //   this.form.title = row.title;
    //   this.form.content = row.content;
    //   this.noticeFormVisible = true;
    // },
    // onSubmit() {
    //   this.form.sendUser = store.getters.name;
    //   saveOrUpdate(this.form).then((resp) => {
    //     if (resp.code === 200) {
    //       this.$message.success("添加成功");
    //       this.noticeFormVisible = false;
    //       this.fetchData();
    //     }
    //   });
    // },
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
</style>