<template>
  <div class="main">
    <div class="title">
      <nuxt-link to="/logIn">登录</nuxt-link>
      <span>·</span>
      <nuxt-link to="/register">注册</nuxt-link>
    </div>

    <div class="sign-up-container">
      <el-form ref="userForm" :model="user">
        <el-form-item
          class="input-prepend restyle"
          prop="username"
          :rules="[
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { validator: checkUsername, trigger: 'blur' },
          ]"
        >
          <div>
            <el-input
              type="text"
              placeholder="用户名"
              v-model="user.username"
            />
            <i class="iconfont icon-phone" />
          </div>
        </el-form-item>

        <el-form-item
          class="input-prepend"
          prop="password"
          :rules="[
            { required: true, message: '请输入密码', trigger: 'blur' },
            { validator: checkPassword, trigger: 'blur' },
          ]"
        >
          <div>
            <el-input
              type="password"
              placeholder="密码"
              v-model="user.password"
            />
            <i class="iconfont icon-password" />
          </div>
        </el-form-item>

        <div class="btn">
          <input
            type="button"
            class="sign-in-button"
            value="登录"
            @click="submitLogin()"
          />
        </div>

        <nuxt-link to="/">
          <div class="btn">
            <input type="button" class="back-home-button" value="返回主页" />
          </div>
        </nuxt-link>
      </el-form>

      <div class="more-sign">
        <h6>社交帐号登录</h6>
        <ul>
          <li>
            <a
              id="weixin"
              class="weixin"
              target="_blank"
              href="http://qy.free.idcfengye.com/api/ucenter/weixinLogin/login"
              ><i class="iconfont icon-weixin"
            /></a>
          </li>
          <li>
            <a id="qq" class="qq" target="_blank" href="#"
              ><i class="iconfont icon-qq"
            /></a>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import "~/assets/css/sign.css";
import "~/assets/css/iconfont.css";
import cookie from "js-cookie";
import { loginMember } from "@/api/login";
import { setToken } from "@/utils/auth";
export default {
  layout: "sign",

  data() {
    return {
      user: {
        username: "",
        password: "",
      },
      loginInfo: {},
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      let form = this.$route.query.form;
      if (form) {
        this.user.username = form.username;
        this.user.password = form.password;
      }
    },
    checkPhone(rule, value, callback) {
      //debugger
      if (!/^1[34578]\d{9}$/.test(value)) {
        return callback(new Error("手机号码格式不正确"));
      }
      return callback();
    },
    checkUsername(rule, value, callback) {
      //debugger
      if (!/^\w{6,18}$/.test(value)) {
        return callback(new Error("用户名格式不正确"));
      }
      return callback();
    },

    checkPassword(rule, value, callback) {
      //debugger
      if (!/^\w{6,18}$/.test(value)) {
        return callback(new Error("密码格式不正确"));
      }
      return callback();
    },

    submitLogin() {
      loginMember(this.user).then((resp) => {
        if (resp.code === 200) {
          //提示登录成功
          this.$message({
            type: "success",
            message: "登录成功",
          });
          let token = resp.data.access_token;
          setToken(token);
          let loginInfo = resp.data.loginInfo;
          //跳转登录页面
          this.$router.push({
            path: "/",
            query: {
              loginInfo: loginInfo,
            },
          });
        }
      });
    },
  },
};
</script>
<style>
.el-form-item__error {
  z-index: 9999999;
}
</style>
