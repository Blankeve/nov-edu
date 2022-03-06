<template>
  <div class="app-container">
    <el-form :model="teacher" :rules="formRules" label-width="80px">
      <el-form-item prop="name" label="讲师名称">
        <el-col :span="4">
          <el-input v-model="teacher.name"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item prop="sort" label="讲师排序">
        <el-input-number
          v-model="teacher.sort"
          :min="0"
          :max="10"
          label="描述文字"
        ></el-input-number>
      </el-form-item>
      <el-form-item prop="level" label="讲师头衔">
        <el-select v-model="teacher.level" placeholder="请选择">
          <el-option label="高级讲师" value="1"></el-option>
          <el-option label="首席讲师" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="career" label="讲师资历">
        <el-col :span="12">
          <el-input v-model="teacher.career"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item prop="intro" label="讲师简介">
        <el-col :span="12">
          <el-input
            type="textarea"
            v-model="teacher.intro"
            :rows="10"
          ></el-input>
        </el-col>
      </el-form-item>
      <el-form-item prop="avatar" label="讲师头像">
        <el-upload
          class="avatar-uploader"
          name="img"
          :action="baseURL + '/upload/img'"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
        >
          <img v-if="teacher.avatar" :src="teacher.avatar" class="avatar" />
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-check" type="primary" @click="saveOrEdit"
          >保存</el-button
        >
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { save, getById, updateById } from "@/api/teacher";

export default {
  data() {
    return {
      teacher: {
        id: "",
        name: "",
        sort: 0,
        level: "",
        career: "",
        intro: "",
        avatar: "",
      },
      baseURL: process.env.VUE_APP_BASE_API,
      formRules: {
        name: [
          { required: true, message: "请输入讲师名称", trigger: "blur" },
          {
            min: 1,
            max: 20,
            message: "长度在 1 到 20 个字符",
            trigger: "blur",
          },
        ],
        sort: [
          { required: true, message: "请设置讲师排序", trigger: "blur" },
          {
            type: "number",
            message: "长度在 1 到 3 个字符",
            trigger: "blur",
          },
        ],
        level: [
          { required: true, message: "请选择讲师头衔", trigger: "blur" },
          {
            message: "长度在 1 到 3 个字符",
            trigger: "blur",
          },
        ],
        career: [
          { required: true, message: "请输入讲师资历", trigger: "blur" },
          {
            message: "长度在 1 到 50 个字符",
            trigger: "blur",
          },
        ],
        intro: [
          { required: true, message: "请输入讲师简介", trigger: "blur" },
          {
            message: "长度在 1 到 500 个字符",
            trigger: "blur",
          },
        ],
        avatar: [
          { required: true, message: "请上传讲师头像", trigger: "blur" },
          {
            message: "长度在 1 到 310个字符",
            trigger: "blur",
          },
        ],
      },
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    saveOrEdit() {
      if (this.teacher.id) {
        updateById(this.teacher).then((resp) => {
          if (resp.code == 200) {
            this.$router.push({ path: "/teacher/list" });
          }
        });
      } else {
        save(this.teacher).then((resp) => {
          if (resp.code == 200) {
            this.$router.push({ path: "/teacher/list" });
          }
        });
      }
    },
    fetchData() {
      let params = this.$route.query;
      if (params && params.id) {
        getById(params.id).then((resp) => {
          if (resp.code == 200) {
            this.teacher = resp.data;
            this.teacher.level += "";
          }
        });
      }
    },
    handleAvatarSuccess(res, file) {
      this.teacher.avatar = res.data.path;
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

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>