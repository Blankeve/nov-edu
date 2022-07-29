<template>
  <client-only>
    <div id="aCoursesList">
      <!-- 网校课程 开始 -->
      <div>
        <div
          style="
            margin: 0px;
            padding: 0px;
            width: 50px;
            position: fixed;
            top: 50%;
            margin-top: -190px;
            z-index: 1100;
          "
        >
          <a
            target="_blank"
            href="http://wpa.qq.com/msgrd?v=3&uin=95307355&site=qq&menu=yes"
            ><img
              border="0"
              src="http://wpa.qq.com/pa?p=2:95307355:53"
              alt="点击这里给我发消息"
              title="点击这里给我发消息"
          /></a>
        </div>
        <section class="container">
          <!-- 幻灯片 开始 -->
          <div v-if="banners.length" v-swiper:mySwiper="swiperOption">
            <div class="swiper-wrapper">
              <div
                v-for="banner in banners"
                :key="banner.id"
                class="swiper-slide"
                style="background: #040b1b"
              >
                <a target="" :href="banner.linkUrl">
                  <img
                    width="100%"
                    :src="banner.imageUrl"
                    :alt="banner.title"
                  />
                </a>
              </div>
            </div>
            <div class="swiper-pagination swiper-pagination-white"></div>
            <div
              class="swiper-button-prev swiper-button-white"
              slot="button-prev"
            ></div>
            <div
              class="swiper-button-next swiper-button-white"
              slot="button-next"
            ></div>
          </div>
          <!-- 幻灯片 结束 -->

          <header class="comm-title">
            <h2 class="tac">
              <span class="c-333">热门课程</span>
            </h2>
          </header>
          <div>
            <article class="comm-course-list">
              <ul class="of" id="bna">
                <li v-for="course in courses" :key="course.id">
                  <div class="cc-l-wrap">
                    <section class="course-img">
                      <img
                        :src="course.cover"
                        class="img-responsive"
                        :alt="course.title"
                      />
                      <div class="cc-mask">
                        <nuxt-link :to="'/course/' + course.id">
                          <a title="开始学习" class="comm-btn c-btn-1"
                            >开始学习</a
                          >
                        </nuxt-link>
                      </div>
                    </section>
                    <h3 class="hLh30 txtOf mt10">
                      <nuxt-link :to="'/course/' + course.id">
                        <a
                          :title="course.title"
                          class="course-title fsize16 c-333"
                          >{{ course.title }}</a
                        >
                      </nuxt-link>
                    </h3>
                    <section class="mt10 hLh20 of">
                      <span
                        class="fr jgTag"
                        :class="{
                          'bg-green': course.price == 0,
                          'bg-red': course.price > 0,
                        }"
                      >
                        <i class="c-fff fsize18 f-fA">{{
                          course.price > 0 ? "¥" + course.price : "免费"
                        }}</i>
                      </span>
                      <span class="fl jgAttr c-ccc f-fA">
                        <i class="c-999 f-fA">{{ course.viewCount }}次播放</i>
                        |
                        <i class="c-999 f-fA"
                          >{{ course.commentCount }}人评论</i
                        >
                      </span>
                    </section>
                  </div>
                </li>
              </ul>
              <div class="clear"></div>
            </article>
          </div>

          <header class="comm-title">
            <h2 class="tac">
              <span class="c-333">最多人学习课程</span>
            </h2>
          </header>
          <div>
            <article class="comm-course-list">
              <ul class="of" id="bna">
                <li v-for="course in courses2" :key="course.id">
                  <div class="cc-l-wrap">
                    <section class="course-img">
                      <img
                        :src="course.cover"
                        class="img-responsive"
                        :alt="course.title"
                      />
                      <div class="cc-mask">
                        <nuxt-link :to="'/course/' + course.id">
                          <a title="开始学习" class="comm-btn c-btn-1"
                            >开始学习</a
                          >
                        </nuxt-link>
                      </div>
                    </section>
                    <h3 class="hLh30 txtOf mt10">
                      <nuxt-link :to="'/course/' + course.id">
                        <a
                          :title="course.title"
                          class="course-title fsize16 c-333"
                          >{{ course.title }}</a
                        >
                      </nuxt-link>
                    </h3>
                    <section class="mt10 hLh20 of">
                      <span
                        class="fr jgTag"
                        :class="{
                          'bg-green': course.price == 0,
                          'bg-red': course.price > 0,
                        }"
                      >
                        <i class="c-fff fsize18 f-fA">{{
                          course.price > 0 ? "¥" + course.price : "免费"
                        }}</i>
                      </span>
                      <span class="fl jgAttr c-ccc f-fA">
                        <i class="c-999 f-fA">{{ course.buyCount }}人学习</i>
                        |
                        <i class="c-999 f-fA"
                          >{{ course.commentCount }}人评论</i
                        >
                      </span>
                    </section>
                  </div>
                </li>
              </ul>
              <div class="clear"></div>
            </article>
          </div>

          <!-- <header class="comm-title">
            <h2 class="tac">
              <span class="c-333">最多人购买课程</span>
            </h2>
          </header>
          <div>
            <article class="comm-course-list">
              <ul class="of" id="bna">
                <li v-for="course in courses3" :key="course.id">
                  <div class="cc-l-wrap">
                    <section class="course-img">
                      <img
                        :src="course.cover"
                        class="img-responsive"
                        :alt="course.title"
                      />
                      <div class="cc-mask">
                        <nuxt-link :to="'/course/' + course.id">
                          <a title="开始学习" class="comm-btn c-btn-1"
                            >开始学习</a
                          >
                        </nuxt-link>
                      </div>
                    </section>
                    <h3 class="hLh30 txtOf mt10">
                      <nuxt-link :to="'/course/' + course.id">
                        <a
                          :title="course.title"
                          class="course-title fsize16 c-333"
                          >{{ course.title }}</a
                        >
                      </nuxt-link>
                    </h3>
                    <section class="mt10 hLh20 of">
                      <span
                        class="fr jgTag"
                        :class="{
                          'bg-green': course.price == 0,
                          'bg-red': course.price > 0,
                        }"
                      >
                        <i class="c-fff fsize18 f-fA">{{
                          course.price > 0 ? "¥" + course.price : "免费"
                        }}</i>
                      </span>
                      <span class="fl jgAttr c-ccc f-fA">
                        <i class="c-999 f-fA">{{ course.buyCount }}人购买</i>
                        |
                        <i class="c-999 f-fA"
                          >{{ course.commentCount }}人评论</i
                        >
                      </span>
                    </section>
                  </div>
                </li>
              </ul>
              <div class="clear"></div>
            </article>
            <section class="tac pt20">
              <nuxt-link to="/course">
                <a title="全部课程" class="comm-btn c-btn-2">全部课程</a>
              </nuxt-link>
            </section>
          </div> -->
        </section>
      </div>
      <!-- /网校课程 结束 -->
      <!-- 网校名师 开始 -->
      <div>
        <section class="container">
          <header class="comm-title">
            <h2 class="tac">
              <span class="c-333">名师大咖</span>
            </h2>
          </header>
          <div>
            <article class="i-teacher-list">
              <ul class="of">
                <li v-for="teacher in teachers" :key="teacher.id">
                  <section class="i-teach-wrap">
                    <div class="i-teach-pic">
                      <nuxt-link :to="'/teacher/' + teacher.id">
                        <img
                          width="250px"
                          height="150px"
                          :alt="teacher.name"
                          :src="teacher.avatar"
                        />
                      </nuxt-link>
                    </div>
                    <div class="mt10 hLh30 txtOf tac">
                      <nuxt-link :to="'/teacher/' + teacher.id">
                        <a :title="teacher.name" class="fsize18 c-666">{{
                          teacher.name
                        }}</a>
                      </nuxt-link>
                    </div>
                    <div class="hLh30 txtOf tac">
                      <span class="fsize14 c-999">{{ teacher.career }}</span>
                    </div>
                    <div class="mt15 i-q-txt">
                      <p class="c-999 f-fA">
                        {{ teacher.intro }}
                      </p>
                    </div>
                  </section>
                </li>
              </ul>
              <div class="clear"></div>
            </article>
            <section class="tac pt20">
              <nuxt-link to="/teacher">
                <a title="全部讲师" class="comm-btn c-btn-2">全部讲师</a>
              </nuxt-link>
            </section>
          </div>
        </section>
      </div>
      <!-- /网校名师 结束 -->
      <el-dialog
        :title="notice.title"
        :visible.sync="noticeFormVisible"
        :close-on-click-modal="false"
        width="500px"
        center=""
      >
        <div class="html-content" v-html="notice.content"></div>
        <p>发布于{{ this.notice.createTime }}</p>
        <div slot="footer" class="dialog-footer">
          <el-button @click="noticeFormVisible = false">我知道了</el-button>
        </div>
      </el-dialog>
    </div>
  </client-only>
</template>

<script>
import { getBannerList } from "@/api/banner";
import { getClientCourseList } from "@/api/course";
import { getClientTeacherList } from "@/api/teacher";
import { receiveNotice } from "@/api/notice";
import { getFormatTime } from "@/utils/datetime-format";
import { mapGetters } from "vuex";

export default {
  data() {
    return {
      swiperOption: {
        //配置分页
        pagination: {
          el: ".swiper-pagination", //分页的dom节点
        },
        //配置导航
        navigation: {
          nextEl: ".swiper-button-next", //下一页dom节点
          prevEl: ".swiper-button-prev", //前一页dom节点
        },
        observer: true, //修改swiper自己或子元素时，自动初始化swiper
        observeParents: true, //修改swiper的父元素时，自动初始化swiper
        loop: true,
        autoplay: {
          delay: 2000,
          disableOnInteraction: false,
        },
      },
      banners: [],
      courses: [],
      courses2: [],
      // courses3: [],
      teachers: [],
      notice: { id: "", title: "", content: "", sendUser: "" },
      noticeFormVisible: false,
    };
  },
  created() {
    this.fetchData();
  },
  computed: {
    ...mapGetters(["uid"]),
  },
  mounted() {
    let adId = "n_";
    receiveNotice().then((resp) => {
      if (resp.code === 200) {
        this.notice = resp.data;
        this.notice.createTime = getFormatTime(this.notice.createTime);
        let adValue = localStorage.getItem(adId);
        if (!adValue || adValue != this.notice.id) {
          this.noticeFormVisible = true;
          localStorage.setItem(adId, this.notice.id);
        }
      }
    });
  },
  methods: {
    fetchData() {
      getBannerList().then((resp) => {
        if (resp.code === 200) {
          this.banners = resp.data;
        }
      });

      getClientCourseList().then((resp) => {
        if (resp.code === 200) {
          this.courses = resp.data.c1;
          this.courses2 = resp.data.c2;
          // this.courses3 = resp.data.c3;
        }
      });

      getClientTeacherList().then((resp) => {
        if (resp.code === 200) {
          this.teachers = resp.data;
        }
      });
    },
  },
};
</script>
