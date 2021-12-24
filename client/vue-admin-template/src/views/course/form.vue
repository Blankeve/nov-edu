<template>
  <div class="app-container">
    <h2>添加课程</h2>
    <div class="myFrm">
      <el-form
        v-show="active == 0"
        :label-position="labelPosition"
        label-width="80px"
      >
        <el-form-item label="课程标题">
          <el-input v-model="course.title"></el-input>
        </el-form-item>

        <el-form-item label="课程分类">
          <el-cascader
            v-model="course.subjectId"
            :options="subjects"
            :props="{ expandTrigger: 'hover', label: 'title', value: 'id' }"
            @change="handleChange"
          ></el-cascader>
        </el-form-item>

        <el-form-item label="课程讲师">
          <el-select v-model="course.teacherId" placeholder="请选择讲师">
            <el-option
              v-for="(item, index) in teacher"
              :label="item.name"
              :key="item.id"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="总课时">
          <el-input-number
            v-model="course.lessonNum"
            :min="0"
            :max="100"
            label="总课时"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="课程简介">
          <quill-editor
            v-model="course.description"
            ref="VueQuillEditor"
          ></quill-editor>
        </el-form-item>
        <el-form-item label="课程封面">
          <el-upload
            class="avatar-uploader"
            name="img"
            :action="baseURL + '/upload/img'"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="course.cover" :src="course.cover" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="课程价格">
          <el-input-number
            v-model="course.price"
            :min="0"
            :max="100"
            label="课程价格"
          ></el-input-number>
        </el-form-item>
      </el-form>
      <el-form
        v-show="active == 1"
        :label-position="labelPosition"
        label-width="80px"
      >
        <el-form-item label="课程标题">
          <el-input v-model="course.title"></el-input>
        </el-form-item>
      </el-form>
      <br />
      <el-button type="primary" @click="submitForm">添加课程</el-button>
    </div>
  </div>
</template>
<script>
import { getAll } from "@/api/teacher";
import { getList } from "@/api/subject";
import { save } from "@/api/course";

export default {
  data() {
    return {
      active: 0,
      labelPosition: "left",
      editorOption: {
        /* quill options */
      },
      course: {
        title: "",
        lessonNum: "",
        price: 0,
        teacherId: null,
        subjectId: [],
        cover: "",
        description: "",
      },
      teacher: [],
      subjects: [],
      baseURL: process.env.VUE_APP_BASE_API,
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      getAll().then((resp) => {
        if (resp.code === 200) {
          this.teacher = resp.data;
        }
      });
      getList().then((resp) => {
        if (resp.code === 200) {
          this.subjects = resp.data.subjects;
        }
      });
    },
    handleChange(value) {
      this.course.subjectId = value;
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
    handleAvatarSuccess(res, file) {
      this.course.cover = res.data.path;
    },
    submitForm() {
      if (this.active === 10) {
        save(this.course).then((resp) => {
          if (resp.code === 200) {
            this.$confirm("添加课程成功, 是否添加章节?", "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning",
            })
              .then(() => {
                this.$router.push({
                  path: "/course/chapter",
                });
              })
              .catch(() => {});
          }
        });
      }
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

.myFrm {
  width: 600px;
  margin: 0 auto;
}

.ql-editor {
  height: 300px;
}

h2 {
  text-align: center;
}
</style>