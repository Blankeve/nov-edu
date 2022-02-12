<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- 讲师列表 开始 -->
    <section class="container">
      <header class="comm-title all-teacher-title">
        <h2 class="fl tac">
          <span class="c-333">全部讲师</span>
        </h2>
        <section class="c-tab-title">
          <a id="subjectAll" title="全部" href="#">全部</a>
          <!-- <c:forEach var="subject" items="${subjectList }">
                            <a id="${subject.subjectId}" title="${subject.subjectName }" href="javascript:void(0)" οnclick="submitForm(${subject.subjectId})">${subject.subjectName }</a>
          </c:forEach>-->
        </section>
      </header>
      <section class="c-sort-box unBr">
        <div>
          <!-- /无数据提示 开始-->
          <section
            v-if="teachers == null || teachers.length == 0"
            class="no-data-wrap"
          >
            <em class="icon30 no-data-ico">&nbsp;</em>
            <span class="c-666 fsize14 ml10 vam"
              >没有相关数据，小编正在努力整理中...</span
            >
          </section>
          <!-- /无数据提示 结束-->
          <article class="i-teacher-list">
            <ul class="of">
              <li v-for="teacher in teachers" :key="teacher.id">
                <section class="i-teach-wrap">
                  <div class="i-teach-pic">
                    <a
                      :href="'/teacher/' + teacher.id"
                      :title="teacher.name"
                      target="_blank"
                    >
                      <img width="250px" height="150px" :src="teacher.avatar" alt />
                    </a>
                  </div>
                  <div class="mt10 hLh30 txtOf tac">
                    <a
                      :href="'/teacher/' + teacher.id"
                      :title="teacher.name"
                      target="_blank"
                      class="fsize18 c-666"
                      >{{ teacher.name }}</a
                    >
                  </div>
                  <div class="hLh30 txtOf tac">
                    <span class="fsize14 c-999">{{ teacher.career }}</span>
                  </div>
                  <div class="mt15 i-q-txt">
                    <p class="c-999 f-fA">{{ teacher.intro }}</p>
                  </div>
                </section>
              </li>
            </ul>
            <div class="clear"></div>
          </article>
        </div>
        <!-- 公共分页 开始 -->
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
        <!-- 公共分页 结束 -->
      </section>
    </section>
    <!-- /讲师列表 结束 -->
  </div>
</template>
<script>
import { getList } from "@/api/teacher";
export default {
  data() {
    return {
      teachers: [],
      form: {
        current: 1,
        size: 8,
        total: 0,
        pages: 1,
      },
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      getList(this.form).then((resp) => {
        if (resp.code === 200) {
          this.teachers = resp.data.records;
          this.form.pages = resp.data.pages;
        }
      });
    },
   nextPage() {
      this.form.current++;
      this.fetchData();
    },
    prevPage() {
      this.form.current--;
      this.fetchData();
    },
    firstPage() {
      this.form.current = 1;
      this.fetchData();
    },
    lastPage() {
      this.form.current = this.form.pages;
      this.fetchData();
    },
  },
};
</script>
