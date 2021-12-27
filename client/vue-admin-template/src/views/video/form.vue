<template>
  <div class="app-container">
    <h2>{{this.video.title != ""?"编辑":"添加"}}小节</h2>
    <div class="myFrm">
      <el-form :label-position="labelPosition" label-width="80px">
        <el-form-item label="小节名称">
          <el-input v-model="video.title"></el-input>
        </el-form-item>

        <el-form-item label="所属讲师">
          <el-select
            v-model="teacher"
            placeholder="请选择讲师"
            @change="teacherChange"
          >
            <el-option
              v-for="(item, index) in teachers"
              :label="item.name"
              :key="item.id"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="所属课程">
          <el-select
            v-model="courseId"
            placeholder="请选择课程"
            @change="courseChange"
          >
            <el-option
              v-for="(item, index) in course"
              :label="item.title"
              :key="item.id"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="所属章节">
          <el-select v-model="video.chapterId" placeholder="请选择章节">
            <el-option
              v-for="(item, index) in chapters"
              :label="item.title"
              :key="item.id"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="当前小节">
          <el-input-number
            v-model="video.sort"
            :min="1"
            :max="100"
            label="总课时"
          ></el-input-number>
        </el-form-item>
      </el-form>
      <br />
      <el-button type="primary" @click="submitForm">添加小节</el-button>
    </div>
  </div>
</template>
<script>
import { getListByTeacherId } from "@/api/course";
import { getChaptersByCourseId } from "@/api/chapter";
import { getAll } from "@/api/teacher";
import { save } from "@/api/video";
export default {
  data() {
    return {
      labelPosition: "left",
      editorOption: {
        /* quill options */
      },
      video: {
        title: "",
        chapterId: null,
        sort: 1,
      },
      teachers: [],
      chapters: [],
      teacher: null,
      course: [],
      courseId: null,
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      let params = this.$route.query;

      if (params.video) {
        this.video = params.video;
        return;
      }

      getAll().then((resp) => {
        if (resp.code === 200) {
          this.teachers = resp.data;
        }
      });
    },
    teacherChange() {
      getListByTeacherId(this.teacher).then((resp) => {
        if (resp.code === 200) {
          this.course = resp.data;
        }
      });
    },
    courseChange() {
      getChaptersByCourseId(this.courseId).then((resp) => {
        if (resp.code === 200) {
          this.chapters = resp.data;
        }
      });
    },

    submitForm() {
      save(this.video).then((resp) => {
        if (resp.code === 200) {
          this.$message({
            type: "success",
            message: "添加成功!",
          });
        }
      });
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