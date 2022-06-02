<template>
  <div class="main">
    <div style="width: 1000px; margin: 0 auto">
      <client-only>
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
        <div class="html-content" v-html="info.content"></div>
      </client-only>
    </div>
  </div>
</template>

<script>
import "~/assets/css/sign.css";
import "~/assets/css/iconfont.css";

import { getOneDetailByInfoId } from "@/api/info";
import { getListByKey } from "@/api/config";

export default {
  layout: "simple",
  data() {
    return {
      list: null,
      listLoading: true,
      cates: [],
      info: {},
    };
  },
  created() {
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
.html-content > h1,
h2,
h3,
h4 {
  color: #24282b;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: block;
  font-size: 17px;
}

</style>

