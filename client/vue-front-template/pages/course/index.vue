<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- /课程列表 开始 -->
    <section class="container">
      <header class="comm-title">
        <h2 class="fl tac">
          <span class="c-333">全部课程</span>
        </h2>
      </header>
      <section class="c-sort-box">
        <section class="c-s-dl">
          <dl>
            <dt>
              <span class="c-999 fsize14">课程类别</span>
            </dt>
            <dd class="c-s-dl-li">
              <ul class="clearfix">
                <li @click="allSelect()">
                  <a title="全部" href="#">全部</a>
                </li>
                <li
                  v-for="subject in subjects"
                  :key="subject.id"
                  @click="changeList($event, subject.id)"
                >
                  <a :title="subject.title" href="#">{{ subject.title }}</a>
                </li>
              </ul>
            </dd>
          </dl>
          <dl>
            <dt>
              <span class="c-999 fsize14"></span>
            </dt>
            <dd class="c-s-dl-li">
              <ul class="clearfix">
                <li
                  v-for="children in subjectChildren"
                  :key="children.id"
                  @click="childrenSelect(children.id)"
                >
                  <a :title="children.title" href="#">{{ children.title }}</a>
                </li>
              </ul>
            </dd>
          </dl>
          <div class="clear"></div>
        </section>
        <div class="js-wrap">
          <section class="fr">
            <span class="c-ccc">
              <i class="c-master f-fM">1</i>/
              <i class="c-666 f-fM">1</i>
            </span>
          </section>
          <section class="fl">
            <ol class="js-tap clearfix">
              <li>
                <a title="关注度" href="#">关注度</a>
              </li>
              <li @click="newestSearch()">
                <a title="最新" href="#">{{form.orderFieldNewestAsc ?(form.orderFieldNewestAsc == 1?"最新":"最旧"):"最旧"}}</a>
              </li>
              <li @click="priceOrderSearch()" class="current bg-orange">
                <a title="价格" href="#"
                  >价格&nbsp;
                  <span>{{form.orderFieldPriceAsc ?(form.orderFieldPriceAsc == 1?"↑":"↓"):"↓"}}</span>
                </a>
              </li>
            </ol>
          </section>
        </div>
        <div class="mt40">
          <!-- /无数据提示 开始-->
          <section
            v-if="courses == null || courses.length == 0"
            class="no-data-wrap"
          >
            <em class="icon30 no-data-ico">&nbsp;</em>
            <span class="c-666 fsize14 ml10 vam"
              >没有相关数据，小编正在努力整理中...</span
            >
          </section>
          <!-- /无数据提示 结束-->
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
                      <a
                        :href="'/course/' + course.id"
                        title="开始学习"
                        class="comm-btn c-btn-1"
                        >开始学习</a
                      >
                    </div>
                  </section>
                  <h3 class="hLh30 txtOf mt10">
                    <a
                      :href="'/course/' + course.id"
                      :title="course.title"
                      class="course-title fsize18 c-333"
                      >{{ course.title }}</a
                    >
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
        </div>
        <!-- 公共分页 开始 -->
        <div>
          <div class="paging">
            <!-- undisable这个class是否存在，取决于数据属性hasPrevious -->
            <a v-if="form.pages > 1 && form.current != 1" href="#" title="首页"
              >首</a
            >
            <a
              v-if="form.pages > 1 && form.current > 1"
              href="#"
              title="前一页"
              @click="nextPage"
              >&lt;</a
            >
            <a
              v-if="form.pages > 1 && form.current < form.pages"
              href="#"
              title="后一页"
              @click="prevPage"
              >&gt;</a
            >
            <a
              v-if="form.pages > 1 && form.current != form.pages"
              href="#"
              title="末页"
              >末</a
            >
            <div class="clear"></div>
          </div>
        </div>
        <!-- 公共分页 结束 -->
      </section>
    </section>
    <!-- /课程列表 结束 -->
  </div>
</template>
<script>
import { getPage } from "@/api/course";
import { getList } from "@/api/subject";
export default {
  data() {
    return {
      subjects: [],
      subjectChildren: [],
      courses: [],
      form: {
        current: 1,
        size: 8,
        total: 0,
        pages: 1,
        orderFieldPriceAsc: null,
        orderFieldNewestAsc: null,
        clientSubjectId: 0,
      },
      liActive: -1,
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      getList().then((resp) => {
        if (resp.code === 200) {
          this.subjects = resp.data.subjects;
        }
      });

      getPage(this.form).then((resp) => {
        if (resp.code === 200) {
          this.courses = resp.data.records;
          this.form.pages = resp.data.pages;
        }
      });
    },
    nextPage() {
      this.form.current++;
    },
    prevPage() {
      this.form.current--;
    },
    changeList($event, id) {
      if (id === 0) {
        let children = [];
        for (let i = 0; i < this.subjects.length; i++) {
          console.log(this.subjects[i].children);
          if (this.subjects[i].children && this.subjects[i].children.length > 0)
            children = children.concat(this.subjects[i].children);
        }
        this.subjectChildren = children;
        console.log(this.subjectChildren);
        return;
      }
      for (let i = 0; i < this.subjects.length; i++) {
        if (this.subjects[i].id == id) {
          this.subjectChildren = this.subjects[i].children;
          break;
        }
      }
    },
    childrenSelect(id) {
      this.liActive = id;
      this.form.clientSubjectId = id;
      this.fetchData();
    },
    allSelect() {
      this.form.clientSubjectId = 0;
      this.fetchData();
    },
    priceOrderSearch() {
      this.form.orderFieldNewestAsc = null;
      this.form.orderFieldPriceAsc = this.form.orderFieldPriceAsc  ? 0 : 1;
      this.fetchData();
    },
    newestSearch() {
      this.form.orderFieldPriceAsc =null;
      this.form.orderFieldNewestAsc = this.form.orderFieldNewestAsc  ? 0 : 1;
      this.fetchData();
    },
  },
};
</script>

