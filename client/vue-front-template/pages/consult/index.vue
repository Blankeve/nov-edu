<template>
  <div class="main">
    <div class="title" style="width: 600px; margin: 0 auto">
      <client-only>
        <div class="mt5">
          <el-form :inline="true" :model="consult" class="demo-form-inline">
            <el-form-item label="我要咨询"> </el-form-item>
            <el-form-item>
              <el-input
                style="width: 430px"
                placeholder="请输入咨询内容"
                prefix-icon="el-icon-chat-dot-square"
                type="textarea"
                v-model="consult.content"
              >
              </el-input>
            </el-form-item>

            <el-form-item>
              <el-button size="small" type="primary" @click="submitConsult"
                >提交</el-button
              >
            </el-form-item>
          </el-form>
        </div>
      </client-only>

      <div class="block">
        <el-timeline>
          <el-timeline-item
            v-for="consult in consults"
            :key="consult.id"
            :timestamp="consult.createTime"
            placement="top"
          >
            <el-card>
              <p>{{ consult.nickname }} ：</p>
              <p class="c">&nbsp;&nbsp;{{ consult.content }}</p>
              <div v-if="consult.adminId != null">
                <br />
                <h5 class="a-re">
                  {{ consult.adminName }}于 {{ consult.updateTime }} 回复 :
                </h5>
                <p class="c">&nbsp;&nbsp;{{ consult.replyContent }}</p>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
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
    submitConsult() {
      save(this.consult).then((resp) => {
        if (resp.code === 200)
          this.$message({
            type: "success",
            message: "提交成功",
          });
        this.fetchData();
      });
    },
  },
};
</script>

<style>
.c {
  font-size: 10px;
}
.a-re {
  color: tomato;
}
</style>

