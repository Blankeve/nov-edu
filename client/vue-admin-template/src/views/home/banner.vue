<template>
  <div class="app-container">
    <el-form :inline="true" ref="form" :model="form">
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="small"
          @click="addConfig()"
          >新增轮播图</el-button
        >
      </el-form-item>
    </el-form>

    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="玩命加载中"
      fit
      stripe
      highlight-current-row
    >
      <el-table-column align="center" label="#" width="50">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="图片标题" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.title }}</span>
        </template>
      </el-table-column>

      <el-table-column label="页面地址" align="center">
        <template slot-scope="scope">
          {{ scope.row.linkUrl }}
        </template>
      </el-table-column>

      <el-table-column label="图片" width="120" align="center">
        <template slot-scope="scope">
          <div class="demo-image__preview">
            <el-image
              :src="scope.row.imageUrl"
              alt="图片获取失败"
              title="点击查看大图"
              width="100px"
              :preview-src-list="[scope.row.imageUrl]"
            />
          </div>
        </template>
      </el-table-column>

      <el-table-column label="显示级别" align="center">
        <template slot-scope="scope">
          {{ scope.row.sort }}
        </template>
      </el-table-column>

      <el-table-column align="center" prop="created_at" label="创建时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="created_at" label="更新时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.updateTime }}</span>
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

    <el-dialog
      :title="configFormTitle"
      :visible.sync="configFormVisible"
      width="500px"
      center=""
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form :model="form" label-width="120">
        <el-form-item label="图片标题">
          <el-input v-model="form.title"></el-input>
        </el-form-item>

        <el-form-item label="页面地址">
          <el-input v-model="form.linkUrl"></el-input>
        </el-form-item>

        <el-form-item label="显示排序">
          <el-input-number
            v-model="form.sort"
            :min="0"
            :max="1000"
            label="描述文字"
          ></el-input-number>
        </el-form-item>

        <el-form-item label="轮播图片">
          <el-upload
            class="avatar-uploader"
            name="img"
            :action="baseURL + '/upload/img'"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img
              width="100%"
              v-if="form.imageUrl"
              :src="form.imageUrl"
              class="avatar"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="configFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getList, saveOrUpdate, removeById } from "@/api/banner";

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
        linkUrl: "/",
        title: "",
        sort: 0,
      },
      baseURL: process.env.VUE_APP_BASE_API,
      configFormTitle: "",
      configFormVisible: false,
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      this.listLoading = true;
      getList().then((response) => {
        let data = response.data;
        this.list = data;
        this.listLoading = false;
      });
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
    handleEdit(row) {
      this.form.id = row.id;
      this.form.title = row.title;
      this.form.linkUrl = row.linkUrl;
      this.form.imageUrl = row.imageUrl;
      this.form.sort = row.sort;
      this.configFormTitle = "修改图片";
      this.configFormVisible = true;
    },
    addConfig() {
      this.form = {};
      this.configFormTitle = "新增图片";
      this.configFormVisible = true;
    },
    onSubmit() {
      this.configFormVisible = false;
      saveOrUpdate(this.form).then((resp) => {
        if (resp.code === 200) {
          this.$message.success((this.form.id ? "修改" : "新增") + "图片成功");
          this.fetchData();
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    getRowClass({ row, column, rowIndex, columnIndex }) {
      return "background:#3f5c6d2c;";
    },
    handleAvatarSuccess(res, file) {
      this.$forceUpdate();
      this.form.imageUrl = res.data.path;
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isPNG = file.type === "image/png";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!(isJPG || isPNG)) {
        this.$message.error("上传头像图片只能是 JPG，PNG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return (isJPG || isPNG) && isLt2M;
    },
  },
};
</script>

<style  scoped>
.el-pagination {
  text-align: center;
}
</style>