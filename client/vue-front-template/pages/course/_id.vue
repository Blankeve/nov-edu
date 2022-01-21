<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- /课程详情 开始 -->
    <section class="container">
      <section class="path-wrap txtOf hLh30">
        <a href="#" title class="c-999 fsize14">首页</a>
        \
        <a href="#" title class="c-999 fsize14">课程列表</a>
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
                <a class="c-fff vam" title="收藏" href="#">收藏</a>
              </span>
            </section>
            <section class="c-attr-mt">
              <a href="#" title="立即观看" class="comm-btn c-btn-3">立即观看</a>
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
                  <a name="c-i" class="current" title="课程详情">课程详情</a>
                </section>
              </div>
              <article class="ml10 mr10">
                <div>
                  <h6 class="c-g-content c-infor-title">
                    <span>课程大纲</span>
                  </h6>
                  <section class="mt20">
                    <div class="lh-menu-wrap">
                      <menu id="lh-menu" class="lh-menu mt10 mr10">
                        <ul>
                          <!-- 文件目录 -->
                          <li class="lh-menu-stair" v-for="chapter in tree.children" :key="chapter.id">
                            <a
                              href="javascript: void(0)"
                              :title="chapter.title"
                              class="current-1"
                            >
                              <em class="lh-menu-i-1 icon18 mr10"></em>第{{chapter.sort}}章 {{chapter.title}}
                            </a>
                            <ol class="lh-menu-ol" style="display: block">
                              <li class="lh-menu-second ml30" v-for="video in chapter.children" :key="video.id">
                                <a :href="'/video/' + video.id" target="_blank" title>
                                  <span class="fr">
                                    <i class="vam mr10" :class="{ 'pay-icon': video.isFree == 0,'free-icon': video.isFree == 1 }">{{video.isFree == 1?"免费":"付费"}}</i>
                                  </span>
                                  <em class="lh-menu-i-2 icon16 mr5">&nbsp;</em
                                  >第{{video.sort}}节 {{video.title}}
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
                <ul style="height: auto">
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
import { getTree, getOneDetailByCourseId } from "@/api/course";

export default {
  data() {
    return {
      course: {
        courseTitle: "",
      },
      tree: {},
    };
  },
  created() {
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
      getTree({ "id": id }).then((resp) => {
        if (resp.code === 200) {
          this.tree = resp.data.records[0];
        }
      });
    },
  },
};
</script>
