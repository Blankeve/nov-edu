<template>
  <div class="app-container">
    <el-steps :active="active" finish-status="success" align-center>
      <el-step
        :title="active > 0 ? '已完成' : '步骤1'"
        icon="el-icon-edit"
        description="编辑课程信息"
      ></el-step>
      <el-step
        :title="active > 1 ? '已完成' : '步骤2'"
        description="创建课程大纲"
      ></el-step>
      <el-step
        :title="active > 2 ? '已完成' : '步骤3'"
        description="最终发布"
      ></el-step>
    </el-steps>
    <div class="myFrm">

        <el-form v-show="active == 0" :label-position="labelPosition" label-width="80px">
          <el-form-item label="课程标题">
            <el-input v-model="course.title"></el-input>
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
            <el-input
              v-model="course.description"
            ></el-input>
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
 <el-form v-show="active == 1" :label-position="labelPosition" label-width="80px">
          <el-form-item label="课程标题">
            <el-input v-model="course.title"></el-input>
          </el-form-item>
      <el-form-item label="课程讲师">
    <el-select  placeholder="请选择讲师">
      <el-option v-for="(item,index) in teacher" :label="item.name" :key="item.id" :value="item.id"></el-option>
    </el-select>
  </el-form-item>
  <el-form-item label="课程分类">
        <el-cascader
    v-model="course.subjectId"
    :options="subjects"
    :props="{ expandTrigger: 'hover' ,label: 'title',value: 'id'}"
    @change="handleChange"></el-cascader>
  </el-form-item>

          </el-form-item>
          <el-form-item label="课程简介">
            <el-input
            type="textarea"
              v-model="course.description"
                :rows="10"
            ></el-input>
          </el-form-item>
        </el-form>
      <br>
      <el-button type="primary" @click="previousStep" v-show="active > 0"
        >上一步</el-button
      >
      <el-button type="primary" @click="nextStep" v-show="active < 2"
        >保存并下一步</el-button
      >
    </div>
  </div>
</template>
<script>
import { getAll } from "@/api/teacher";
import { getList } from "@/api/subject";

export default {
  data() {
    return {
      active: 0,
      labelPosition: "top",
      course: {
        title: "",
        lessonNum: "",
        description: "",
        price: 0,
        subjectId: "",
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
      getList().then(resp=>{
              if (resp.code === 200) {
          this.subjects = resp.data.subjects;
        }
      })
    },
    nextStep() {
      this.active = this.active < 3 ? this.active + 1 : this.active;
    },
    previousStep() {
      this.active = this.active > 0 ? this.active - 1 : this.active;
    },
    handleChange(value) {
      console.log(value);
    },
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
  width: 500px;
  margin: 0 auto;
}

.el-form-item {
  margin-bottom: 0px;
}
</style>