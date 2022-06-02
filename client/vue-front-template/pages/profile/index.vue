<template>
  <div class="main">
    <div class="title" style="width: 1000px; margin: 0 auto">
      <el-tabs tab-position="left" style="height: 700px">
        <el-tab-pane label="我的资料">
          <el-descriptions class="margin-top" :column="1" border>
            <template slot="extra">
              <el-button
                icon="el-icon-check"
                type="primary"
                @click="updateProfile"
                >更新资料</el-button
              >
              <el-button icon="el-icon-close" type="danger" @click="logout"
                >退出登录</el-button
              >
            </template>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-user"></i>
                头像
              </template>

              <el-upload
                class="avatar-uploader"
                name="img"
                :action="'http://159.75.234.20:8000/upload/img'"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
              >
                <img
                  width="100"
                  v-if="user.avatar"
                  :src="user.avatar"
                  class="avatar"
                />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-user"></i>
                昵称
              </template>
              <el-input v-model="user.nickname" autocomplete="off"></el-input>
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-user"></i>
                用户名
              </template>
              {{ user.username }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-mobile-phone"></i>
                手机号
              </template>
              <el-input v-model="user.mobile" autocomplete="off"></el-input>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-location-outline"></i>
                居住地
              </template>
              {{ user.lastLoginIp }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-tickets"></i>
                账户角色
              </template>
              <el-tag size="small">{{ user.roleName }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-office-building"></i>
                加入时间
              </template>
              {{ user.createTime }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-office-building"></i>
                更新时间
              </template>
              {{ user.updateTime }}
            </el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
        <el-tab-pane label="我的订单">
          <el-table
            v-loading="listLoading"
            :data="list"
            element-loading-text="Loading"
            fit
            highlight-current-row
          >
            <el-table-column align="center" label="#" width="50">
              <template slot-scope="scope">
                {{ scope.$index + 1 }}
              </template>
            </el-table-column>

            <el-table-column label="购买课程" width="200px" align="center">
              <template slot-scope="scope">
                {{ scope.row.courseTitle }}
              </template>
            </el-table-column>
            <el-table-column label="课程封面" width="200px" align="center">
              <template slot-scope="scope">
                <div class="demo-image__preview">
                  <el-image
                    :src="scope.row.courseCover"
                    alt="图片获取失败"
                    title="点击查看大图"
                    width="100px"
                    :preview-src-list="[scope.row.courseCover]"
                  />
                </div>
              </template>
            </el-table-column>
            <el-table-column label="课程讲师" align="center">
              <template slot-scope="scope">
                {{ scope.row.teacherName }}
              </template>
            </el-table-column>

            <el-table-column label="课程价格" width="80px" align="center">
              <template slot-scope="scope">
                {{ scope.row.totalFee }}
              </template>
            </el-table-column>

            <el-table-column label="支付方式" width="100px" align="center">
              <template slot-scope="scope" v-if="scope.row.status === 1">
                {{ scope.row.payType === 1 ? "微信支付" : "支付宝" }}
              </template>
            </el-table-column>

            <el-table-column align="center" prop="created_at" label="支付时间">
              <template slot-scope="scope">
                <i class="el-icon-time" />
                <span>{{ scope.row.createTime }}</span>
              </template>
            </el-table-column>

            <el-table-column
              fixed="right"
              align="center"
              label="操作"
              width="170"
            >
              <template slot-scope="scope">
                <el-button
                  @click="studyCourse(scope.row.courseId)"
                  icon="el-icon-right"
                  slot="reference"
                  type="primary"
                  >立即学习</el-button
                >
              </template>
            </el-table-column>
          </el-table>
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
        </el-tab-pane>
        <el-tab-pane label="历史观看"></el-tab-pane>
        <el-tab-pane label="修改密码">
          <el-form
            :model="ruleForm"
            status-icon
            :rules="rules"
            ref="ruleForm"
            label-width="100px"
            class="demo-ruleForm"
          >
            <el-form-item label="旧密码" prop="oldpass">
              <el-input
                type="password"
                v-model="ruleForm.oldpass"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newpass">
              <el-input
                type="password"
                v-model="ruleForm.newpass"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm')"
                >提交</el-button
              >
            </el-form-item>
          </el-form></el-tab-pane
        >
      </el-tabs>
    </div>
  </div>
</template>

<script>
import "~/assets/css/sign.css";
import "~/assets/css/iconfont.css";

import { getById, updatePwdById, updateById } from "@/api/user";
import { getToken, removeToken, removeInfo } from "@/utils/auth";
import jwtDecode from "jwt-decode";
import { getOrderPage } from "@/api/order";
export default {
  layout: "simple",
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (!/^\w{6,18}$/.test(value)) {
          return callback(new Error("密码格式不正确"));
        }
        callback();
      }
    };
    return {
      list: null,
      listLoading: true,
      sizes: [],
      form: {
        current: 1,
        size: 8,
        total: 0,
        pages: 1,
      },
      user: {
        username: "",
        mobile: "",
        lastLoginIp: undefined,
      },
      token: null,
      ruleForm: {
        id: undefined,
        oldpass: "",
        newpass: "",
      },
      rules: {
        oldpass: [{ validator: validatePass, trigger: "blur" }],
        newpass: [{ validator: validatePass, trigger: "blur" }],
      },
      profile: {
        avatar: "",
      },
      loginInfo: undefined,
    };
  },
  created() {
    this.token = getToken();
    if (!this.token) this.$router.push("/login");
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      if (!this.loginInfo) this.loginInfo = jwtDecode(this.token);

      let uid = this.loginInfo.uid;
      getById(uid).then((resp) => {
        if (resp.code === 200) {
          this.user = resp.data;
        }
      });
      this.listLoading = true;
      this.sizes =
        this.form.size > 1
          ? [this.form.size / 2, this.form.size, this.form.size * 2]
          : [this.form.size, this.form.size * 2];
      getOrderPage(this.form).then((response) => {
        let data = response.data;
        this.form.current = data.current;
        this.form.size = data.size;
        this.form.total = data.total;
        this.list = data.records;
        this.listLoading = false;
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
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        console.log(valid);
        if (valid) {
          this.ruleForm.id = this.user.id;
          updatePwdById(this.ruleForm).then((resp) => {
            if (resp.code === 200) {
              this.$message({
                type: "success",
                message: "修改成功",
              });
            }
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    handleAvatarSuccess(res, file) {
      this.user.avatar = res.data.path;
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isPNG = file.type === "image/png";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!(isJPG || isPNG)) {
        this.$message.error("上传头像图片只能是 JPG，PNG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return (isJPG || isPNG) && isLt2M;
    },
    studyCourse(id) {
      this.$router.push({
        path: "/course/" + id,
      });
    },
    updateProfile() {
      updateById(this.user).then((resp) => {
        if (resp.code === 200) {
          this.$message({
            type: "success",
            message: "更新成功",
          });
          this.fetchData();
        }
      });
    },
    historyWatch(){
      this.$alert("该功能敬请期待", "nov在线课堂提示", {
        confirmButtonText: "确定",
      });
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
          this.$router.push("/");
        })
        .catch(() => {});
    },
  },
};
</script>

<style scoped>
.c {
  font-size: 10px;
}
.a-re {
  color: tomato;
}
</style>

