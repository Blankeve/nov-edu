<template>
  <div class="main">
    <div style="width: 1000px; margin: 0 auto">
      <client-only>
        <div>
          <span
            ><a @click="clickItem()" :class="addColor()" href="#" class="cate"
              >全部文章</a
            ></span
          >
          <el-divider direction="vertical"></el-divider>
          <span v-for="(item, index) in cates" :key="item.id">
            <span
              ><a
                @click="clickItem(item.configValue)"
                :class="addColor(item.configValue)"
                href="#"
                class="cate"
                >{{ item.configName }}</a
              ></span
            >
            <el-divider direction="vertical"></el-divider>
          </span>
        </div>
        <br />
        <div v-loading="listLoading">
          <span v-for="(item, index) in list" :key="item.id">
            <el-badge
              v-if="item.clickCount"
              :value="item.clickCount+'次阅读'"
              class="item"
            ></el-badge>
            <el-badge
              v-if="!item.clickCount"
              value="new"
              class="item"
            ></el-badge>
            <span
              ><p class="text1">{{ item.catename }}</p>
              <nuxt-link :to="'/info/' + item.id">{{ item.title }}</nuxt-link>
            </span>
            <span class="author">
              {{ item.createTime }}&nbsp; {{ item.createrNickname }}</span
            >
            <el-divider></el-divider>
          </span>
        </div>
      </client-only>

      <!-- 公共分页 开始 -->
      <div class="block">
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="form.current"
          :page-sizes="sizes"
          :page-size="form.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="form.total"
        >
        </el-pagination>
      </div>
      <!-- 公共分页 结束 -->
    </div>
  </div>
</template>

<script>
import "~/assets/css/sign.css";
import "~/assets/css/iconfont.css";

import { getPage } from "@/api/info";
import { getListByKey } from "@/api/config";
import { getFormatTime } from "@/utils/datetime-format";

export default {
  layout: "simple",
  data() {
    return {
      list: null,
      listLoading: true,
      cates: [],
      sizes: [],
      form: {
        cate: null,
        current: 1,
        size: 10,
        total: 0,
        pages: 1,
      },
      acolor: true, //是否展示颜色
      currentIndex: null,
      key: {
        key: "artcle_cate",
        grade: 2,
      },
    };
  },
  mounted() {
    let pageInfo = sessionStorage.getItem("infoPageForm");
    if (pageInfo) {
      this.form = JSON.parse(pageInfo);
    }
    this.fetchData();
  },
  methods: {
    fetchData() {
      getListByKey(this.key).then((resp) => (this.cates = resp.data));
      this.listLoading = true;
      this.sizes =
        this.form.size > 1
          ? [this.form.size / 2, this.form.size, this.form.size * 2]
          : [this.form.size, this.form.size * 2];
      getPage(this.form).then((response) => {
        let data = response.data;
        this.form.current = data.current;
        this.form.size = data.size;
        this.form.total = data.total;
        this.list = data.records;
        for (let i = 0; i < this.list.length; i++) {
          this.list[i]["createTime"] = getFormatTime(
            this.list[i]["createTime"]
          );
        }
        this.listLoading = false;
        sessionStorage.setItem("infoPageForm", JSON.stringify(this.form));
      });
    },
    handleCurrentChange(p) {
      this.form.current = p;
      this.fetchData();
    },
    handleSizeChange(s) {
      this.form.size = s;
      this.fetchData();
    },
    clickItem: function (index) {
      //行点击事件
      this.currentIndex = index;
      this.form.cate = index;
      this.fetchData();
    },
    addColor: function (index) {
      //颜色改变事件
      if (this.currentIndex == index) {
        return { active: this.acolor };
      }
    },
  },
};
</script>

<style scoped>
.text1 {
  color: #0e5ef3;
  float: left;
  margin-right: 2.64rem;
  min-width: 0.6rem;
}
span {
  font-size: 16px;
}
.cate {
  font-size: 18px;
}
.author {
  font-weight: 600;
  margin-top: 30px;
  font-size: 11px;
  float: right;
}
.cate:hover {
  color: #f93333;
  text-decoration: none;
  cursor: pointer;
}
.active {
  color: #f93333;
}
</style>

