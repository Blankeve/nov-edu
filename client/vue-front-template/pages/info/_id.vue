<template>
  <div class="main">
    <div style="width: 1000px; margin: 0 auto">
      <el-page-header style="float: left" @back="goBack" content="">
      </el-page-header>
      <div>
        <p class="text2">
          {{ info.title }}
          <span class="author">
            点击量: {{ info.clickCount }} &nbsp;&nbsp;&nbsp;
            {{ info.createTime }} 作者: {{ info.createrNickname }}</span
          >
        </p>
      </div>
      <el-divider></el-divider>
      <!-- <quill-editor
          disabled
          ref="editor"
          v-model="info.content"
          :options="editorOption"
        /> -->
      <client-only>
        <div class="ql-container ql-snow">
          <div class="html-content ql-editor" v-html="info.content"></div>
        </div>

        <el-backtop :bottom="100">
          <div
            style="
               {
                height: 100%;
                width: 100%;
                background-color: #f2f5f6;
                box-shadow: 0 0 6px rgba(0, 0, 0, 0.12);
                text-align: center;
                line-height: 40px;
                color: #1989fa;
              }
            "
          >
            回到顶部
          </div></el-backtop
        >
      </client-only>
    </div>
  </div>
</template>

<script>
import "~/assets/css/sign.css";
import "~/assets/css/iconfont.css";
if (process.client) {
  var editorOptions = require("@/utils/editor-options");
}
import { getOneDetailByInfoId } from "@/api/info";

export default {
  layout: "simple",
  data() {
    return {
      list: null,
      listLoading: true,
      cates: [],
      info: {},
      editorOption: editorOptions,
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      let id = this.$route.params.id;
      if (id) {
        getOneDetailByInfoId(id).then((resp) => {
          if (resp.code === 200) {
            this.info = resp.data.data;
          }
        });
      }
    },
    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>

<style >
.text2 {
  color: #24282b;
  font-size: 20px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: block;
  text-align: center;
}
.author {
  font-size: 10px;
  font-weight: 600;
  float: right;
}
</style>

