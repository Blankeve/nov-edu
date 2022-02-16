<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- /课程详情 开始 -->
    <section class="container">
      <section class="path-wrap txtOf hLh30">
        <nuxt-link to="/">
          <a title class="c-999 fsize14">首页</a>
        </nuxt-link>
        \
        <nuxt-link to="/course">
          <a title class="c-999 fsize14">课程列表</a>
        </nuxt-link>
        \
        <span class="c-333 fsize14">{{ course.courseTitle }}</span>
      </section>
      <div>
        <article class="c-v-pic-wrap" style="height: 357px">
          <section class="p-h-video-box" id="videoPlay">
            <img
              width="100%"
              :src="course.courseCover"
              :alt="course.courseCover"
              class="dis c-v-pic"
            />
          </section>
        </article>
        <aside class="c-attr-wrap">
          <section class="ml20 mr15">
            <h2 class="hLh30 txtOf mt15">
              <span class="c-fff fsize24">{{ course.courseTitle }}</span>
            </h2>
            <section class="c-attr-jg">
              <span class="c-fff">价格：</span>
              <b class="c-yellow" style="font-size: 24px"
                >￥{{ course.coursePrice }}</b
              >
            </section>
            <section class="c-attr-mt c-attr-undis">
              <span class="c-fff fsize14"
                >主讲： {{ course.teacherName }}&nbsp;&nbsp;&nbsp;</span
              >
            </section>
            <section class="c-attr-mt of">
              <span class="ml10 vam">
                <em class="icon18 scIcon"></em>
                <span class="c-fff vam" title="收藏"
                  >{{ course.courseApplyCount }}人正在学习该门课程</span
                >
              </span>
            </section>
            <section class="c-attr-mt">
              <a
                v-if="course.coursePrice == 0"
                href="javascript:void(0)"
                @click="viewOrBuy"
                title="立即观看"
                class="comm-btn c-btn-3"
                >{{ hasBuy ? "已报名" : "立即报名" }}</a
              >
              <a
                v-if="course.coursePrice > 0"
                href="javascript:void(0)"
                @click="viewOrBuy"
                title="立即观看"
                class="comm-btn c-btn-3"
                >{{ hasBuy ? "已购买" : "立即购买" }}</a
              >
            </section>
          </section>
        </aside>
        <aside class="thr-attr-box">
          <ol class="thr-attr-ol clearfix">
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">购买数</span>
                <br />
                <h6 class="c-fff f-fM mt10">{{ course.courseBuyCount }}</h6>
              </aside>
            </li>
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">课时数</span>
                <br />
                <h6 class="c-fff f-fM mt10">{{ course.courseLessonNum }}</h6>
              </aside>
            </li>
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">浏览数</span>
                <br />
                <h6 class="c-fff f-fM mt10">{{ course.courseViewCount }}</h6>
              </aside>
            </li>
          </ol>
        </aside>
        <div class="clear"></div>
      </div>
      <!-- /课程封面介绍 -->
      <div class="mt20 c-infor-box">
        <article class="fl col-7">
          <section class="mr30">
            <div class="i-box">
              <div>
                <section id="c-i-tabTitle" class="c-infor-tabTitle c-tab-title">
                  <a
                    name="c-i"
                    href="javascript:void(0)"
                    @click="courseDetailClick"
                    :class="{
                      current: clickState == 1,
                    }"
                    title="课程详情"
                    >课程详情</a
                  >
                  <a
                    name="c-i"
                    href="javascript:void(0)"
                    title="课程评论"
                    @click="commentClick"
                    :class="{
                      current: clickState == 2,
                    }"
                    >课程评论</a
                  >
                </section>
              </div>
              <article v-show="clickState == 1" class="ml10 mr10">
                <div>
                  <h6 class="c-g-content c-infor-title">
                    <span>课程大纲</span>
                  </h6>
                  <section class="mt20">
                    <div class="lh-menu-wrap">
                      <menu id="lh-menu" class="lh-menu mt10 mr10">
                        <ul>
                          <!-- 文件目录 -->
                          <li
                            class="lh-menu-stair"
                            v-for="chapter in tree.children"
                            :key="chapter.id"
                          >
                            <a
                              href="javascript: void(0)"
                              :title="chapter.title"
                              class="current-1"
                            >
                              <em class="lh-menu-i-1 icon18 mr10"></em>第{{
                                chapter.sort
                              }}章 {{ chapter.title }}
                            </a>
                            <ol class="lh-menu-ol" style="display: block">
                              <li
                                class="lh-menu-second ml30"
                                v-for="video in chapter.children"
                                :key="video.id"
                              >
                                <a
                                  href="javascript: void(0)"
                                  @click="openVideo(video.isFree, video.id)"
                                  title
                                >
                                  <span class="fr">
                                    <i
                                      class="vam mr10"
                                      :class="{
                                        'pay-icon': video.isFree == 0,
                                        'free-icon': video.isFree == 1,
                                      }"
                                      >{{
                                        video.isFree == 1
                                          ? "免费"
                                          : hasBuy
                                          ? "已付费"
                                          : "付费"
                                      }}</i
                                    >
                                  </span>
                                  <em class="lh-menu-i-2 icon16 mr5">&nbsp;</em
                                  >第{{ video.sort }}节 {{ video.title }}
                                </a>
                              </li>
                            </ol>
                          </li>
                        </ul>
                      </menu>
                    </div>
                  </section>
                </div>
                <!-- /课程大纲 -->
                <div>
                  <h6 class="c-i-content c-infor-title mt50">
                    <span>课程介绍</span>
                  </h6>
                  <div class="course-txt-body-wrap">
                    <section class="course-txt-body">
                      <p v-html="course.introDescription"></p>
                    </section>
                  </div>
                </div>
                <!-- /课程介绍 -->
              </article>
              <article v-show="clickState == 2" class="ml10 mr10">
                <div>
                  <div class="mt5">
                    <el-form
                      :inline="true"
                      :model="comment"
                      class="demo-form-inline"
                    >
                      <el-form-item label="我要评价">
                        <el-rate
                          class="mt5"
                          v-model="comment.mark"
                          :colors="colors"
                        >
                        </el-rate>
                      </el-form-item>
                      <el-form-item>
                        <el-input
                          placeholder="请输入内容"
                          prefix-icon="el-icon-chat-dot-square"
                          v-model="comment.content"
                        >
                        </el-input>
                      </el-form-item>

                      <el-form-item>
                        <el-button type="primary" @click="submitComment"
                          >评价</el-button
                        >
                      </el-form-item>
                    </el-form>
                  </div>

                  <h6 class="c-g-content c-infor-title mt2">
                    <span>所有评论</span>
                  </h6>
                  <!-- /无数据提示 开始-->
                  <section
                    v-if="clickState == 2 && comments.length == 0"
                    class="no-data-wrap"
                  >
                    <em class="icon30 no-data-ico">&nbsp;</em>
                    <span class="c-666 fsize14 ml10 vam"
                      >没有相关评论，抢占沙发吧...</span
                    >
                  </section>
                  <!-- /无数据提示 结束-->
                  <section class="stud-act-list">
                    <div class="comment-list grid-row-2">
                      <div
                        class="comment-item"
                        v-for="comment in comments"
                        :key="comment.id"
                      >
                        <div class="item-left">
                          <img
                            class="user-avatar"
                            :src="comment.avatar"
                            width="40"
                            height="40"
                          />
                          <p class="user-name">{{ comment.nickname }}</p>
                        </div>
                        <div class="item-right">
                          <div class="star-list">
                            <i
                              v-for="count in comment.mark"
                              :key="count"
                              class="iconfont icon--Star"
                            ></i>
                          </div>
                          <div class="comment-bd">
                            {{ comment.content }}
                          </div>
                          <div class="comment-ft">
                            <!-- <span class="comment-where"
                              >已上课34小时52分钟时评价</span
                            > -->
                            <span class="comment-time">{{
                              comment.createTime
                            }}</span>
                            <span
                              @click="handleReport(comment.id)"
                              class="comment-report"
                              data-id="144115261255602258"
                              >举报</span
                            >
                          </div>
                          <div class="comment-reply"></div>
                        </div>
                      </div>
                    </div>

                    <div>
                      <div class="paging">
                        <!-- undisable这个class是否存在，取决于数据属性hasPrevious -->
                        <a
                          v-if="form.pages > 1 && form.current != 1"
                          @click="firstPage"
                          title="首页"
                          >首</a
                        >
                        <a
                          v-if="form.pages > 1 && form.current > 1"
                          title="前一页"
                          @click="prevPage"
                          >&lt;</a
                        >
                        <a
                          v-if="form.pages > 1 && form.current < form.pages"
                          title="后一页"
                          @click="nextPage"
                          >&gt;</a
                        >
                        <a
                          v-if="form.pages > 1 && form.current != form.pages"
                          title="末页"
                          @click="lastPage"
                          >末</a
                        >
                        <div class="clear"></div>
                      </div>
                    </div>
                  </section>
                </div>
              </article>
            </div>
          </section>
        </article>
        <aside class="fl col-3">
          <div class="i-box">
            <div>
              <section class="c-infor-tabTitle c-tab-title">
                <a title href="javascript:void(0)">主讲讲师</a>
              </section>
              <section class="stud-act-list">
                <ul style="height: 80px">
                  <li>
                    <div class="u-face">
                      <nuxt-link :to="'/teacher/' + course.teacherId">
                        <img
                          :alt="course.teacherName"
                          :src="course.teacherAvatar"
                        />
                      </nuxt-link>
                    </div>
                    <section class="hLh30 txtOf">
                      <a class="c-333 fsize16 fl" href="#">{{
                        course.teacherName
                      }}</a>
                    </section>
                    <section class="hLh20 txtOf">
                      <span class="c-999">{{ course.teacherCareer }}</span>
                    </section>
                  </li>
                </ul>
              </section>
            </div>
          </div>
        </aside>
        <div class="clear"></div>
      </div>
    </section>
    <!-- /课程详情 结束 -->
  </div>
</template>

<script>
import { getTree, getOneDetailByCourseId, applyCourse } from "@/api/course";
import { save, getCommentPage, reportComment } from "@/api/comment";
import {
  createOrder,
  getOrderById,
  getOrderByUidAndCourseId,
} from "@/api/order";
import { getToken, removeToken } from "@/utils/auth";
import jwtDecode from "jwt-decode";

export default {
  data() {
    return {
      token: null,
      hasBuy: false,
      course: {
        courseTitle: "",
      },
      tree: {},
      clickState: 1,
      form: {
        current: 1,
        size: 8,
        total: 0,
        pages: 1,
      },
      comment: {
        content: "",
        mark: 5,
      },
      comments: [],
      colors: ["#99A9BF", "#F7BA2A", "#FF9900"], // 等同于 { 2: '#99A9BF', 4: { value: '#F7BA2A', excluded: true }, 5: '#FF9900' }
    };
  },
  created() {
    this.token = getToken();
    this.fetchData();
  },
  methods: {
    fetchData() {
      let id = this.$route.params.id;
      getOneDetailByCourseId(id).then((resp) => {
        if (resp.code === 200) {
          this.course = resp.data;
        }
      });
      getOrderByUidAndCourseId(id).then((resp) => {
        if (resp.code === 200 && resp.data && resp.data.hasBuy) {
          this.hasBuy = true;
        }
      });
      getTree({ id: id }).then((resp) => {
        if (resp.code === 200) {
          this.tree = resp.data.records[0];
        }
      });
    },
    commentClick() {
      this.clickState = 2;
      this.form.courseId = this.course.courseId;
      getCommentPage(this.form).then((resp) => {
        if (resp.code === 200) {
          this.comments = resp.data.records;
          this.form.pages = resp.data.pages;
        }
      });
    },
    courseDetailClick() {
      this.clickState = 1;
    },
    submitComment() {
      if (!this.token) {
        this.$notify({
          title: "NOV课堂提示",
          message: "登录后才可以评论该课程!",
          type: "warning",
        });
        //跳转登录页面
        this.$router.push({
          path: "/login",
        });
        return;
      }
      this.comment.courseId = this.course.courseId;
      this.comment.teacherId = this.course.teacherId;
      this.comment.teacherId = this.course.teacherId;
      save(this.comment).then((resp) => {
        if (resp.code === 200) {
          this.$message({
            type: "success",
            message: "评论成功",
          });
          this.commentClick();
        }
      });
    },
    nextPage() {
      this.form.current++;
      this.commentClick();
    },
    prevPage() {
      this.form.current--;
      this.commentClick();
    },
    firstPage() {
      this.form.current = 1;
      this.commentClick();
    },
    lastPage() {
      this.form.current = this.form.pages;
      this.commentClick();
    },
    checkLogin() {
      if (!this.token) {
        this.$notify({
          title: "NOV课堂提示",
          message: "请先登录!",
          type: "warning",
        });
        //跳转登录页面
        this.$router.push({
          path: "/login",
        });
        return false;
      }
      return true;
    },
    viewOrBuy() {
      if (!this.checkLogin()) return;
      const loginInfo = jwtDecode(this.token);
      let uid = loginInfo.uid;
      let data = {
        courseId: this.course.courseId,
        uid: uid,
        teacherId: this.course.teacherId,
      };
      if (this.course.coursePrice == 0) {
        applyCourse(data).then((resp) => {
          if (resp.code === 200) {
            this.$message({
              type: "success",
              message: "报名成功",
            });
            this.hasBuy = true;
            this.course.courseApplyCount++;
          }
        });
      }
      if (this.course.coursePrice > 0 && !this.hasBuy) {
        this.$confirm("您还未拥有该课程, 是否立即购买?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            const loading = this.$loading({
              lock: true,
              text: "正在跳转订单页",
              spinner: "el-icon-loading",
              background: "rgba(0, 0, 0, 0.7)",
            });
            createOrder(data).then((resp) => {
              if (resp.code === 200) {
                let orderId = resp.data.order;
                loading.close();
                //跳转订单页面
                this.$router.push({
                  path: "/order/" + orderId,
                });
              }
            });
          })
          .catch(() => {});
      }
    },
    openVideo(isFree, id) {
      if (
        (this.course.coursePrice > 0 && isFree == 1) ||
         this.hasBuy
      ) {
        if (!this.checkLogin()) return;
        this.$router.push({
          path: "/video/" + id,
        });
      } else {
        this.$message({
          type: "error",
          message:
            "请先" + (this.course.coursePrice > 0 ? "购买" : "报名") + "该课程",
        });
      }
    },
    handleReport(id) {
      if (!this.token) {
        this.$notify({
          title: "NOV课堂提示",
          message: "请先登录!",
          type: "warning",
        });
        //跳转登录页面
        this.$router.push({
          path: "/login",
        });
        return;
      }
      reportComment(id).then((resp) => {
        if (resp.code === 200) {
          this.$message({
            type: "success",
            message: "举报留言成功",
          });
        }
      });
    },
  },
};
</script>
<style >
.comment-list .comment-item {
  position: relative;
  padding: 20px 20px 20px 80px;
  border-top: 1px solid #e5e5e5;
}

.comment-list .comment-item:first-child {
  border-top: 0;
}

.comment-list .comment-item .item-good {
  position: absolute;
  top: 0;
  right: 0;
  width: 25px;
  height: 25px;
  line-height: 25px;
}

.comment-list .comment-item .item-good:before {
  content: "";
  width: 0;
  height: 0;
  border-top: 25px solid #23b8ff;
  border-right: 25px solid #23b8ff;
  border-left: 25px solid transparent;
  border-bottom: 25px solid transparent;
  position: absolute;
  top: 0;
  left: -25px;
}

.comment-list .comment-item .item-good span {
  position: relative;
  font-size: 16px;
  color: #fff;
  top: 1px;
}

.comment-list .comment-item .item-left {
  position: absolute;
  width: 80px;
  text-align: center;
  top: 20px;
  left: 0;
}

.comment-list .comment-item .user-avatar {
  border-radius: 50%;
}

.comment-list .comment-item .item-right {
  position: relative;
}

.comment-list .comment-item .star-list {
  margin-top: -4px;
}

.comment-list .comment-item .comment-bd {
  margin-bottom: 10px;
}

.comment-list .comment-ft + .comment-reply {
  margin-top: 20px;
  margin-bottom: -20px;
}

.comment-ft {
  color: #999;
}

.comment-ft .comment-where {
  margin-right: 10px;
}

.comment-ft .comment-report {
  position: absolute;
  right: 10px;
  cursor: pointer;
}

.comment-ft .comment-report:hover {
  color: #23b8ff;
}

.comment-ft.comment-ft--first {
  margin-bottom: 20px;
}
</style>