<template>
  <div class="app-container">
    <h2>
      {{
        this.$route.query.chapter && !this.$route.query.course
          ? "编辑"
          : "添加"
      }}章节
    </h2>
    <div class="myFrm">
      <el-form :label-position="labelPosition" label-width="80px">
        <el-form-item label="章节名称">
          <el-input v-model="chapter.title"></el-input>
        </el-form-item>

        <el-form-item
          v-show="!this.$route.query.chapter && !this.$route.query.course"
          label="所属讲师"
        >
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

        <el-form-item
          v-show="!this.$route.query.chapter && !this.$route.query.course"
          label="所属课程"
        >
          <el-select v-model="chapter.courseId" placeholder="请选择课程">
            <el-option
              v-for="(item, index) in course"
              :label="item.title"
              :key="item.id"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="当前章节">
          <el-input-number
            v-model="chapter.sort"
            :min="1"
            :max="100"
            label="总课时"
          ></el-input-number>
        </el-form-item>
      </el-form>
      <br />
      <el-button type="primary" @click="submitForm"
        >{{
          this.$route.query.chapter && !this.$route.query.course
            ? "编辑"
            : "添加"
        }}章节</el-button
      >
    </div>
  </div>
</template>
<script>
import { getList, getListByTeacherId, getOneByCourseId } from "@/api/course";
import { getAll } from "@/api/teacher";
import { save, updateById, getOneByChapterId } from "@/api/chapter";
export default {
  data() {
    return {
      labelPosition: "left",
      editorOption: {
        /* quill options */
      },
      chapter: {
        title: "",
        courseId: "",
        sort: null,
      },
      teachers: [],
      teacher: null,
      course: [],
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      let chapterId = this.$route.query.chapter;
      if (chapterId) {
        getOneByChapterId(chapterId).then((resp) => {
          if (resp.code === 200) {
            this.chapter = resp.data;
          }
        });
        return;
      }
      this.chapter.courseId = this.$route.query.course;
      getList().then((resp) => {
        if (resp.code === 200) {
          this.course = resp.data;
        }
      });
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
    handleChange(value) {
      this.chapter.courseId = value;
    },

    submitForm() {
      save(this.chapter).then((resp) => {
        if (resp.code === 200) {
          this.$confirm(
            (this.$route.query.chapter && !this.$route.query.course
              ? "修改"
              : "添加") + "章节成功, 是否添加小节?",
            "提示",
            {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning",
            }
          )
            .then(() => {
              this.$router.push({
                path: "/video/save",
                query: {
                  chapter: resp.data,
                },
              });
            })
            .catch(() => {});
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