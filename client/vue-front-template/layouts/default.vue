<template>
  <div class="in-wrap">
    <header id="header">
      <section class="container">
        <h1 id="logo">
          <a href="#" title="谷粒学院">
            <img src="~/assets/img/logo.png" width="100%" alt="谷粒学院" />
          </a>
        </h1>
        <div class="h-r-nsl">
          <ul class="nav">
            <nuxt-link to="/" tag="li" active-class="current" exact>
              <a>首页</a>
            </nuxt-link>
            <nuxt-link to="/course" tag="li" active-class="current">
              <a>课程</a>
            </nuxt-link>
            <nuxt-link to="/consult" tag="li" active-class="current">
              <a>咨询</a>
            </nuxt-link>
            <nuxt-link to="/info" tag="li" active-class="current">
              <a>文章</a>
            </nuxt-link>
            <!-- <nuxt-link to="/" tag="li">
              <a @click="openTip">公告</a>
            </nuxt-link> -->
          </ul>
          <ul class="h-r-login">
            <li v-show="!loginInfo.nickname" id="no-login">
              |
              <nuxt-link to="/login">
                <em class="icon18 login-icon">&nbsp;</em>
                <span class="vam ml5">登录</span></nuxt-link
              >

              <nuxt-link to="/register">
                <span class="vam ml5">注册</span></nuxt-link
              >
            </li>
            <li v-show="loginInfo.nickname" id="is-login-one" class="mr10">
              <a id="headerMsgCountId" href="#" title="消息">
                <em class="icon18 news-icon">&nbsp;</em>
              </a>
              <q class="red-point" style="display: none">&nbsp;</q>
            </li>
            <li v-show="loginInfo.nickname" id="is-login-two" class="h-r-user">
              <a href="/profile" title>
                <client-only>
                  <img
                    :src="loginInfo.avatar"
                    width="30"
                    height="30"
                    class="vam picImg"
                    alt
                  />
                </client-only>
                <span id="userName">{{ loginInfo.nickname }}</span>
              </a>
              <a>
                <span title="退出" @click="logout()" class="ml5">退出</span>
              </a>
            </li>
          </ul>

          <aside class="h-r-search">
            <label class="h-r-s-box">
              <input
                type="text"
                placeholder="输入你想学的课程"
                v-model="queryTitle"
                value
              />
              <button @click="queryCourse" class="s-btn">
                <em class="icon18">&nbsp;</em>
              </button>
            </label>
          </aside>
        </div>
        <aside class="mw-nav-btn">
          <div class="mw-nav-icon"></div>
        </aside>
        <div class="clear"></div>
      </section>
    </header>

    <nuxt />

    <footer id="footer">
      <section class="container">
        <!-- <div class>
          <h4 class="hLh30">
            <span class="fsize18 f-fM c-999">友情链接</span>
          </h4>
          <ul class="of flink-list">
            <li>
              <a href="http://www.baidu.com/" title="NOV" target="_blank"
                >百度一下你就知道</a
              >
               <a href="http://www.4399.com/" title="NOV" target="_blank"
                >娱乐一下吧</a
              >
            </li>
          </ul>
          <div class="clear"></div>
        </div> -->
        <div class="b-foot">
          <section class="fl col-7">
            <section class="mr20">
              <section class="b-f-link">
                <a href="#" title="关于我们" target="_blank">关于我们</a>|
                <a href="#" title="联系我们" target="_blank">联系我们</a>|
                <a href="#" title="帮助中心" target="_blank">帮助中心</a>|
                <a href="#" title="资源下载" target="_blank">资源下载</a>|
                <span>骚扰热线：13145207788</span>
                <span>Email：info@NOVEDU.com</span>
              </section>
              <section class="b-f-link mt10">
                <span>©2022课程版权均归NOV在线课堂所有</span>
              </section>
            </section>
          </section>
          <aside class="fl col-3 tac mt15">
            <section class="gf-tx">
              <span>
                <img src="~/assets/img/wx-icon.png" alt />
              </span>
            </section>
            <section class="gf-tx">
              <span>
                <img src="~/assets/img/wb-icon.png" alt />
              </span>
            </section>
          </aside>
          <div class="clear"></div>
        </div>
      </section>
    </footer>
  </div>
</template>
<script>
import "~/assets/css/reset.css";
import "~/assets/css/theme.css";
import "~/assets/css/global.css";
import "~/assets/css/web.css";
import "~/assets/css/base.css";
import "~/assets/css/activity_tab.css";
import "~/assets/css/bottom_rec.css";
import "~/assets/css/nice_select.css";
import "~/assets/css/order.css";
import "~/assets/css/swiper-3.3.1.min.css";
import "~/assets/css/pages-weixinpay.css";

import jwtDecode from "jwt-decode";
import { getInfo, removeInfo, removeToken } from "@/utils/auth";
export default {
  data() {
    return {
      loginInfo: {
        id: "",
        username: "",
        nickname: "",
        avatar: "",
      },
      queryTitle: "",
    };
  },
  created() {
    this.fetchData();
  },
  watch: {
    $route(to, from) {
      this.$router.go(0);
    },
  },
  methods: {
    openTip() {
      this.$alert("该功能敬请期待", "nov在线课堂提示", {
        confirmButtonText: "确定",
      });
    },
    queryCourse() {
      this.$router.push({
        path: "/course",
        query: {
          title: this.queryTitle,
        },
      });
    },
    fetchData() {
      let loginInfo = getInfo();
      if (typeof loginInfo == "string" && loginInfo.length > 0) {
        loginInfo = JSON.parse(loginInfo);
      }
      if (loginInfo) {
        this.loginInfo.nickname = loginInfo.nickname;
        this.loginInfo.avatar = loginInfo.avatar;
      }
    },
    logout() {
      this.$confirm("此操作将注销账号, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          removeToken();
          removeInfo();
          this.$router.go(0);
        })
        .catch(() => {});
    },
  },
};
</script>
