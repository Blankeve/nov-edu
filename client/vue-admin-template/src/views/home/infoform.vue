<template>
  <div class="app-container">
    <h2>{{ this.$route.query.info ? "编辑" : "添加" }}资讯</h2>
    <div class="myCourseFrm">
      <el-form
        v-show="active == 0"
        :model="courseVO"
        :rules="formRules"
        :label-position="labelPosition"
        label-width="80px"
      >
        <el-form-item prop="title" label="资讯标题">
          <el-input v-model="info.title"></el-input>
        </el-form-item>

        <el-form-item prop="cate" label="资讯分类">
          <el-select v-model="info.cate" placeholder="请选择分类">
            <el-option
              v-for="(item, index) in cates"
              :label="item.configName"
              :key="item.id"
              :value="item.configValue"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="content" label="资讯内容">
          <quill-editor
            v-model="info.content"
            ref="VueQuillEditor"
          ></quill-editor>
        </el-form-item>
      </el-form>

      <br />
      <el-button
        style="margin-left: 100px"
        icon="el-icon-plus"
        type="primary"
        @click="submitForm"
        >提交</el-button
      >
    </div>
  </div>
</template>
<script>
import { getListByKey } from "@/api/config";
import { saveOrUpdate, getOneDetailByInfoId } from "@/api/info";

export default {
  data() {
    return {
      active: 0,
      labelPosition: "left",
      key: {
        key: "info_cate",
      },
      editorOption: {
        /* quill options */
      },
      cates: [],
      info: {},
      formRules: {
        courseTitle: [
          { required: true, message: "请输入课程标题", trigger: "blur" },
          {
            min: 1,
            max: 50,
            message: "长度在 1 到 50 个字符",
            trigger: "blur",
          },
        ],
        teacherId: [
          { required: true, message: "请选择课程讲师", trigger: "blur" },
          {
            min: 1,
            max: 20,
            message: "长度在 1 到 20 个字符",
            trigger: "blur",
          },
        ],
        subjectId: [
          { required: true, message: "请选择课程分类", trigger: "blur" },
          {
            min: 1,
            max: 20,
            message: "长度在 1 到 10 个字符",
            trigger: "blur",
          },
        ],
        courseCover: [
          { required: true, message: "请上传课程封面", trigger: "blur" },
          {
            min: 1,
            max: 50,
            message: "长度在 1 到 50 个字符",
            trigger: "blur",
          },
        ],
        introDescription: [
          { required: true, message: "请输入课程简介", trigger: "blur" },
          {
            min: 1,
            max: 500,
            message: "长度在 1 到 500 个字符",
            trigger: "blur",
          },
        ],
      },
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      let infoId = this.$route.query.info;
      getListByKey(this.key).then((resp) => (this.cates = resp.data));
      if (infoId) {
        getOneDetailByInfoId(infoId).then((resp) => {
          if (resp.code === 200) {
            this.info = resp.data;
          }
        });
      }
    },

    submitForm() {
      saveOrUpdate(this.info).then((resp) => {
        if (resp.code === 200) {
          this.$message.success((this.info.id ? "修改" : "添加") + "成功");
        }
      });
    },
  },
};
</script>

<style>
.myCourseFrm {
  width: 1200px;
  margin: 0 auto;
}

.ql-editor {
  height: 800px;
}

h2 {
  text-align: center;
}
</style>