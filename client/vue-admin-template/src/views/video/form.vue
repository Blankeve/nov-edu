<template>
  <div class="app-container">
    <h2>
      {{
        this.$route.query.video && !this.$route.query.chapter ? "编辑" : "新增"
      }}小节
    </h2>
    <div class="myFrm">
      <el-form
        :model="video"
        :rules="videoFormRules"
        :label-position="labelPosition"
        label-width="80px"
      >
        <el-form-item prop="title" label="小节名称">
          <el-input v-model="video.title"></el-input>
        </el-form-item>

        <el-form-item
          v-show="!this.$route.query.video && !this.$route.query.chapter"
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
          v-show="!this.$route.query.video && !this.$route.query.chapter"
          label="所属课程"
        >
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

        <el-form-item
          v-show="!this.$route.query.video && !this.$route.query.chapter"
          label="所属章节"
        >
          <el-select v-model="video.chapterId" placeholder="请选择章节">
            <el-option
              v-for="(item, index) in chapters"
              :label="item.title"
              :key="item.id"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="sort" label="当前小节">
          <el-input-number
            v-model="video.sort"
            :min="1"
            :max="100"
            label="总课时"
          ></el-input-number>
        </el-form-item>

        <el-form-item prop="isFree" label="是否收费">
          <el-radio-group v-model="video.isFree">
            <el-radio-button label="1">免费</el-radio-button>
            <el-radio-button label="0">付费</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <el-form-item v-if="video.videoSourcePath" label="视频路径">
          {{ video.videoSourcePath }}
          <video width="320" controls>
            <source :src="video.videoSourcePath" type="video/mp4" />
            您的浏览器不支持 HTML5 video 标签。
          </video>
        </el-form-item>

        <el-form-item label="上传视频">
          <el-upload
            class="upload-demo"
            :action="baseURL + '/upload/video'"
            :on-preview="handlePreview"
            :on-success="handleVideoSuccess"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            :before-upload="beforeUpload"
            name="video"
            multiple
            :limit="1"
          >
            <el-button size="small" type="primary"
              >{{ video.videoSourcePath ? "重新" : "点击" }}上传</el-button
            >
            <div slot="tip" class="el-upload__tip">
              只能上传mp4/avi文件，且不超过300MB
            </div>
          </el-upload>
          <span v-show="video.videoOriginalName != ''">{{
            percentageFlag
          }}</span>
        </el-form-item>
      </el-form>
      <br />
      <el-button icon="el-icon-check" type="primary" @click="submitForm"
        >提交</el-button
      >
    </div>
  </div>
</template>
<script>
import { getListByTeacherId } from "@/api/course";
import { getChaptersByCourseId } from "@/api/chapter";
import { getAll } from "@/api/teacher";
import { saveVideo, getOneByVideoId } from "@/api/video";
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
        isFree: 1,
        sort: null,
        videoSourcePath: "",
        duration: null,
        videoOriginalName: "",
        size: null,
      },
      videoFormRules: {
        title: [
          { required: true, message: "请输入小节名称", trigger: "blur" },
          { min: 1, max: 50, message: "长度在 1 到 5 个字符", trigger: "blur" },
        ],
        sort: [
          { required: true, message: "请选择当前小节", trigger: "blur" },
          {
            type: "number",
            message: "长度在 1 到 3 个字符",
            trigger: "blur",
          },
        ],
        isFree: [
          { required: true, message: "请设置小节是否收费", trigger: "blur" },
          {
            type: "number",
            message: "长度在 1 到 3 个字符",
            trigger: "blur",
          },
        ],
      },
      teachers: [],
      chapters: [],
      teacher: null,
      course: [],
      courseId: null,
      percentageFlag: "",
      baseURL: process.env.VUE_APP_BASE_API,
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      let videoId = this.$route.query.video;
      if (videoId) {
        getOneByVideoId(videoId).then((resp) => {
          if (resp.code === 200) {
            this.video = resp.data;
          }
        });
        return;
      }
      this.video.chapterId = this.$route.query.chapter;

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
      saveVideo(this.video).then((resp) => {
        if (resp.code === 200) {
          this.$message({
            type: "success",
            message:
              (this.$route.query.video && !this.$route.query.chapter
                ? "修改"
                : "新增") + "成功!",
          });
        }
      });
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    beforeUpload() {
      this.percentageFlag = "努力上传中..";
    },
    handleVideoSuccess(res, file) {
      this.video.videoSourcePath = res.data.path;
      this.video.videoOriginalName = res.data.videoOriginalName;
      this.video.duration = res.data.duration;
      this.video.size = res.data.size;
      this.percentageFlag = "";
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