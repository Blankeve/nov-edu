<template>
  <div class="app-container">
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="玩命加载中"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="#" width="50">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column width="100" label="咨询人" align="center">
        <template slot-scope="scope">
          {{ scope.row.nickname }}
        </template>
      </el-table-column>

      <el-table-column label="咨询内容" align="center">
        <template slot-scope="scope">
          {{ scope.row.content }}
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        prop="created_at"
        label="咨询时间"
        width="200"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>

      <el-table-column width="100" label="回复人" align="center">
        <template slot-scope="scope">
          {{ scope.row.adminName }}
        </template>
      </el-table-column>

      <el-table-column label="回复内容" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.replyContent }}</span>
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        prop="created_at"
        label="回复时间"
        width="200"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.updateTime }}</span>
        </template>
      </el-table-column>

      <el-table-column fixed="right" align="center" label="操作" width="220">
        <template slot-scope="scope">
          <el-button
            type="info"
            size="small"
            @click="handleEdit(scope.row)"
            icon="el-icon-edit"
            >回复</el-button
          >
          <el-button
            type="danger"
            size="small"
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

    <el-dialog
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
        <el-form-item prop="content" label="回复内容">
          <el-input type="textarea" v-model="consult.replyContent"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="noticeFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getPage, update, removeById } from "@/api/consult";
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
      rules: {
        replyContent: [
          { required: true, message: "请输入回复内容", trigger: "blur" },
          {
            min: 1,
            max: 250,
            message: "长度在 1 到 250 个字符",
            trigger: "blur",
          },
        ],
      },
      list: null,
      listLoading: true,
      form: {
        current: 1,
        size: 8,
        total: 0,
      },
      consult: {
        id: null,
        replyContent: "",
      },
      noticeFormTitle: "回复咨询",
      noticeFormVisible: false,
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
      this.$confirm("此操作将永久删除数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          removeById(id).then((resp) => {
            if (resp.code === 200) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
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
    handleEdit(row) {
      this.consult.id = row.id;
      this.consult.replyContent = row.replyContent;
      this.noticeFormVisible = true;
    },
    onSubmit() {
      this.form.sendUser = store.getters.name;
      update(this.consult).then((resp) => {
        if (resp.code === 200) {
          this.$message.success("回复成功");
          this.noticeFormVisible = false;
          this.fetchData();
        }
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