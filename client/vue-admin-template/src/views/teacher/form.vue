<template>
  <div class="app-container">
    <el-form label-width="80px">
      <el-form-item label="讲师名称">
        <el-col :span="4">
          <el-input v-model="teacher.name"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="讲师排序">
        <el-input-number
          v-model="teacher.sort"
          :min="0"
          :max="10"
          label="描述文字"
        ></el-input-number>
      </el-form-item>
      <el-form-item label="讲师头衔">
        <el-select v-model="teacher.level" placeholder="请选择">
          <el-option label="高级讲师" value="1"></el-option>
          <el-option label="首席讲师" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="讲师资历">
        <el-col :span="12">
          <el-input v-model="teacher.career"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="讲师简介">
        <el-col :span="12">
          <el-input
            type="textarea"
            v-model="teacher.intro"
            :rows="10"
          ></el-input>
        </el-col>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveOrEdit">保存</el-button>
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
      }
      else{
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
          }
        });
      }
    },
  },
};
</script>