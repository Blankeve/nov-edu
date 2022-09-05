<template>
  <div
    element-loading-text="玩命加载中"
    v-loading="formLoading"
    class="app-container"
  >
    <h2>{{ this.$route.query.info ? "编辑" : "新增" }}文章</h2>
    <div class="myCourseFrm">
      <el-form
        v-show="active == 0"
        :label-position="labelPosition"
        label-width="80px"
      >
        <el-row>
          <el-col>
            <el-form-item prop="title" label="文章标题">
              <el-input v-model="info.title"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item prop="cate" label="文章分类">
              <el-select
                style="width: 100%"
                v-model="info.cate"
                placeholder="请选择分类"
              >
                <el-option
                  v-for="(item, index) in cates"
                  :label="item.configName"
                  :key="item.id"
                  :value="item.configValue"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col>
            <el-form-item prop="content" label="文章内容">
              <quill-editor
                v-model="info.content"
                ref="VueQuillEditor"
                :options="editorOption"
              ></quill-editor>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <br />
      <el-button
        style="margin-left: 100px"
        icon="el-icon-check"
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
import { editorOptions } from "@/utils/editor-options";

export default {
  data() {
    return {
      formLoading: false,
      active: 0,
      labelPosition: "left",
      key: {
        key: "artcle_cate",
        grade: 2,
      },
      //编辑器相关
      editorOption: editorOptions,

      cates: [],
      info: {},
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
        this.formLoading = true;
        getOneDetailByInfoId(infoId).then((resp) => {
          if (resp.code === 200) {
            this.info = resp.data;
            this.info.cate += "";
            this.formLoading = false;
          }
        });
      }
    },

    submitForm() {
      saveOrUpdate(this.info).then((resp) => {
        if (resp.code === 200) {
          this.$message.success((this.info.id ? "修改" : "新增") + "成功");
        }
      });
    },
    ready() {},
  },
};
</script>



<style lang="scss" scoped>
::v-deep .ql-editor {
  height: 800px;
}

h2 {
  text-align: center;
}
</style>