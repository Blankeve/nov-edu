<template>
  <div class="main">
    <div class="title">
      <nuxt-link to="/logIn">登录</nuxt-link>
      <span>·</span>
      <nuxt-link to="/register">注册</nuxt-link>
    </div>

    <div class="sign-up-container">
      <el-form ref="userForm" :model="form">
        <el-form-item
          class="input-prepend restyle"
          prop="nickname"
          :rules="[
            { required: false, message: '请输入你的昵称', trigger: 'blur' },
          ]"
        >
          <div>
            <el-input
              type="text"
              placeholder="你的昵称"
              v-model="form.nickname"
            />
            <i class="iconfont icon-user" />
          </div>
        </el-form-item>

        <el-form-item
          class="input-prepend restyle no-radius"
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
              v-model="form.username"
            />
            <i class="iconfont icon-user" />
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
              placeholder="设置密码"
              v-model="form.password"
            />
            <i class="iconfont icon-password" />
          </div>
        </el-form-item>

        <el-form-item
          class="input-prepend"
          prop="mobile"
          :rules="[
            { required: false, message: '请输入手机号码', trigger: 'blur' },
            { validator: checkPhone, trigger: 'blur' },
          ]"
        >
          <div>
            <el-input
              type="phone"
              placeholder="设置手机号"
              v-model="form.mobile"
            />
            <i class="iconfont icon-phone" />
          </div>
        </el-form-item>

        <div class="btn">
          <input
            type="button"
            class="sign-up-button"
            value="注册"
            @click="submitRegister()"
          />
        </div>
        <p class="sign-up-msg">
          点击 “注册” 即表示您同意并愿意遵守简书
          <br />
          <a target="_blank" href="http://www.jianshu.com/p/c44d171298ce"
            >用户协议</a
          >
          和
          <a target="_blank" href="http://www.jianshu.com/p/2ov8x3">隐私政策</a>
          。
        </p>
      </el-form>
      <!-- 更多注册方式 -->
      <div class="more-sign">
        <h6>社交帐号直接注册</h6>
        <ul>
          <li>
            <a
              id="weixin"
              class="weixin"
              target="_blank"
              href="http://huaan.free.idcfengye.com/api/ucenter/wx/login"
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

import { registerMember } from "@/api/register";

export default {
  layout: "sign",
  data() {
    return {
      form: {
        //封装注册输入数据
        mobile: "",
        code: "", //验证码
        nickname: "",
        username: "",
        password: "",
      },
      sending: true, //是否发送验证码
      second: 60, //倒计时间
      codeTest: "获取验证码",
      userValidated: false,
      pwdValidated: false,
      phoneValidated: false,
    };
  },
  methods: {
    //注册提交的方法
    submitRegister() {
      if (this.userValidated && this.pwdValidated && this.phoneValidated)
        registerMember(this.form).then((resp) => {
          if (resp.code === 200) {
            //提示注册成功
            this.$message({
              type: "success",
              message: "注册成功",
            });
            //跳转登录页面
            this.$router.push({
              path: "/login",
              query: {
                form: this.form,
              },
            });
          }
        });
    },
    timeDown() {
      let result = setInterval(() => {
        --this.second;
        this.codeTest = this.second;
        if (this.second < 1) {
          clearInterval(result);
          this.sending = true;
          //this.disabled = false;
          this.second = 60;
          this.codeTest = "获取验证码";
        }
      }, 1000);
    },

    checkPhone(rule, value, callback) {
      console.log(value);
      //debugger
      if (!/^1[3|4|5|7|8]\d{9}$/.test(value)) {
        this.phoneValidated = false;
        return callback(new Error("手机号码格式不正确"));
      }
      this.phoneValidated = true;
      return callback();
    },

    checkUsername(rule, value, callback) {
      //debugger
      if (!/^\w{6,18}$/.test(value)) {
        this.userValidated = false;
        return callback(new Error("用户名格式不正确"));
      }
      this.userValidated = true;
      return callback();
    },

    checkPassword(rule, value, callback) {
      //debugger
      if (!/^\w{6,18}$/.test(value)) {
        this.pwdValidated = false;
        return callback(new Error("密码格式不正确"));
      }
      this.pwdValidated = true;
      return callback();
    },
  },
};
</script>

