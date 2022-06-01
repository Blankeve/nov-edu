<template>
  <div class="main">
    <div class="title" style="width: 1000px; margin: 0 auto">
      <client-only>
        <div>
          <span><a href="#" class="cate">行业动态</a></span>
          <el-divider direction="vertical"></el-divider>
          <span><a  class="cate">最新政策</a></span>
          <el-divider direction="vertical"></el-divider>
        </div>
        <br />
        <div>
          <span
            ><p class="text1">行业动态</p>
            <a href=""
              >青春是一个短暂的美梦, 当你醒来时, 它早已消失无踪</a
            ></span
          >
          <el-divider></el-divider>
        </div>
      </client-only>

      <!-- 公共分页 开始 -->
      <div>
        <div class="paging">
          <!-- undisable这个class是否存在，取决于数据属性hasPrevious -->
          <a
            v-show="form.pages > 2 && form.current != 1"
            @click="firstPage"
            title="首页"
            >首</a
          >
          <a
            v-show="form.pages > 1 && form.current > 1"
            title="前一页"
            @click="prevPage"
            >&lt;</a
          >
          <a
            v-show="form.pages > 1 && form.current < form.pages"
            title="后一页"
            @click="nextPage"
            >&gt;</a
          >
          <a
            v-show="form.pages > 2 && form.current != form.pages"
            title="末页"
            @click="lastPage"
            >末</a
          >
          <div class="clear"></div>
        </div>
      </div>
      <!-- 公共分页 结束 -->
    </div>
  </div>
</template>

<script>
import "~/assets/css/sign.css";
import "~/assets/css/iconfont.css";

import { save, getPage } from "@/api/consult";

export default {
  layout: "simple",
  data() {
    return {
      consults: [],
      form: {
        current: 1,
        size: 8,
        total: 0,
        pages: 1,
      },
      consult: {
        content: "",
      },
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      getPage(this.form).then((resp) => {
        if (resp.code === 200) {
          this.consults = resp.data.records;
          this.form.pages = resp.data.pages;
        }
      });
    },
    nextPage() {
      this.form.current++;
      this.fetchData();
    },
    prevPage() {
      this.form.current--;
      this.fetchData();
    },
    firstPage() {
      this.form.current = 1;
      this.fetchData();
    },
    lastPage() {
      this.form.current = this.form.pages;
      this.fetchData();
    },
    addClass(){
       
    }
  },
};
</script>

<style>
.text1 {
  color: #0e5ef3;
  float: left;
  margin-right: 2.64rem;
  min-width: 0.6rem;
}
span {
  font-size: 19px;
}
.cate{
    font-size: 22px;
}

.cate:hover{
  text-decoration: none;
  cursor: pointer;
}
.cate:active{
    color: #f93333;
}

</style>

