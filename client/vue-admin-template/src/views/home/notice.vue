<template>
  <div class="app-container">
    <el-row>
      <el-button
        icon="el-icon-plus"
        type="primary"
        size="small"
        @click="addNotice"
        >新增公告</el-button
      >
    </el-row>
    <br />
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

      <el-table-column width="100" label="发布人" align="center">
        <template slot-scope="scope">
          {{ scope.row.sendUser }}
        </template>
      </el-table-column>

      <el-table-column width="200" label="公告标题" align="center">
        <template slot-scope="scope">
          {{ scope.row.title }}
        </template>
      </el-table-column>

      <el-table-column label="公告内容" align="center">
        <template slot-scope="scope">
          <span
            :class="{
              red: scope.row.content.length > 300,
            }"
            >{{
              scope.row.content.length > 300
                ? "当前内容太长，请点击编辑查看"
                : scope.row.content
            }}</span
          >
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

      <el-table-column fixed="right" align="center" label="操作" width="220">
        <template slot-scope="scope">
          <el-button
            type="info"
            @click="handleEdit(scope.row)"
            icon="el-icon-edit"
            size="small"
            >编辑</el-button
          >
          <el-button
            type="danger"
            @click="handleDelete(scope.row.id)"
            icon="el-icon-delete"
            size="small"
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
      width="800px"
      center=""
    >
      <el-form inline :model="form" :rules="rules" label-width="120">
        <el-row>
          <el-col :span="14">
            <el-form-item prop="title" label="公告名称">
              <el-input
                style="width: 300px"
                v-model="form.title"
              ></el-input> </el-form-item
          ></el-col>
          <el-col :span="10">
            <el-form-item label="公告类型">
              <el-select v-model="form.type" placeholder="请选择公告类型">
                <el-option label="公告" value="1"></el-option>
                <el-option label="通知" value="2"></el-option>
              </el-select> </el-form-item
          ></el-col>
        </el-row>

        <el-form-item prop="content" label="公告内容">
          <quill-editor
            v-model="form.content"
            ref="VueQuillEditor"
            :options="editorOption"
          ></quill-editor>
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
import { getPage, removeById, saveOrUpdate } from "@/api/notice";
import { editorOptions } from "@/utils/editor-options";
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
        title: [
          { required: true, message: "请输入公告标题", trigger: "blur" },
          {
            min: 1,
            max: 50,
            message: "长度在 1 到 50 个字符",
            trigger: "blur",
          },
        ],
        content: [
          { required: true, message: "请输入公告内容", trigger: "blur" },
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
        type: "",
        title: "",
        content: "",
        current: 1,
        size: 8,
        total: 0,
      },
      noticeFormTitle: "新增公告",
      noticeFormVisible: false,
      sizes: [],
      //编辑器相关
      editorOption: editorOptions,
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
    addNotice() {
      this.form.id = undefined;
      this.form.title = "";
      this.form.content = "";
      this.form.sendUser = "";
      this.noticeFormTitle = "新增公告";
      this.noticeFormVisible = true;
    },
    handleEdit(row) {
      this.noticeFormTitle = "编辑公告";
      this.form.id = row.id;
      this.form.sendUser = row.sendUser;
      this.form.title = row.title;
      this.form.content = row.content;
      this.noticeFormVisible = true;
    },
    onSubmit() {
      this.form.sendUser = store.getters.name;
      saveOrUpdate(this.form).then((resp) => {
        if (resp.code === 200) {
          this.$message.success((this.form.id ? "更新" : "新增") + "成功");
          this.noticeFormVisible = false;
          this.fetchData();
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.red {
  color: rgb(245, 104, 104);
}
.mid-input {
  width: 80px;
}
.el-pagination {
  text-align: center;
}

::v-deep .ql-editor {
  height: 500px;
}
.el-input {
  width: 100%;
}
</style>