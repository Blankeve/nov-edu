<template>
  <div>
    <!-- 幻灯片 开始 -->

    <!-- 幻灯片 结束 -->

    <div id="aCoursesList">
      <!-- 网校课程 开始 -->
      <div>
        <section class="container">
          <!-- 幻灯片 开始 -->
          <div v-swiper:mySwiper="swiperOption">
            <div class="swiper-wrapper">
              <div
                v-for="banner in banners"
                :key="banner.id"
                class="swiper-slide"
                style="background: #040b1b"
              >
                <a target="" href="/">
                  <img width="100%" :src="banner.imageUrl" :alt="banner.title" />
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
                          class="course-title fsize18 c-333"
                          >{{ course.title }}</a
                        >
                      </nuxt-link>
                    </h3>
                    <section class="mt10 hLh20 of">
                      <span class="fr jgTag bg-green">
                        <i class="c-fff fsize12 f-fA">{{
                          course.price > 0 ? course.price + "元" : "免费"
                        }}</i>
                      </span>
                      <span class="fl jgAttr c-ccc f-fA">
                        <i class="c-999 f-fA">9634人学习</i>
                        |
                        <i class="c-999 f-fA">9634评论</i>
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
          </div>
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
                        <img width="250px" height="150px" :alt="teacher.name" :src="teacher.avatar" />
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
                      <span class="fsize14 c-999">{{ teacher.intro }}</span>
                    </div>
                    <div class="mt15 i-q-txt">
                      <p class="c-999 f-fA">
                        {{ teacher.career }}
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
    </div>
  </div>
</template>

<script>
import { getBannerList } from "@/api/banner";
import { getClientCourseList } from "@/api/course";
import { getClientTeacherList } from "@/api/teacher";

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
      },
      banners: [],
      courses: [],
      teachers: [],
    };
  },
  created() {
    this.fetchData();
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
          this.courses = resp.data;
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

