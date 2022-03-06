<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" class="mgb20" style="height: 262px">
          <div class="user-info">
            <img :src="userInfo.avatar" class="user-avator" alt />
            <div class="user-info-cont">
              <div class="user-info-name">{{ userInfo.username }}</div>
              <div>
                {{ userInfo.rolename
                }}{{ teacherName ? "-" + teacherName : "" }}
              </div>
            </div>
          </div>
          <div class="user-info-list">
            上次登录时间：
            <span>{{ userInfo.lastLoginTime }}</span>
          </div>
          <div class="user-info-list">
            上次登录地点：
            <span>{{ userInfo.lastLoginIp }}</span>
          </div>
        </el-card>
        <el-card v-if="code != 5" shadow="hover" style="height: 415px">
          <template #header>
            <div class="clearfix">
              <span>课程分类详情</span>
            </div>
          </template>
          <div v-if="subjectRatios && subjectRatios.length > 0">
            {{ subjectRatios[0].title }}
            <el-progress
              :percentage="subjectRatios[0].value"
              color="#42b983"
            ></el-progress>
          </div>

          <div v-if="subjectRatios && subjectRatios.length > 1">
            {{ subjectRatios[1].title }}
            <el-progress
              :percentage="subjectRatios[1].value"
              color="#f1e05a"
            ></el-progress>
          </div>
          <div v-if="subjectRatios && subjectRatios.length > 2">
            {{ subjectRatios[2].title }}
            <el-progress :percentage="subjectRatios[2].value"></el-progress>
          </div>
          <div v-if="subjectRatios && subjectRatios.length > 3">
            {{ subjectRatios[3].title }}
            <el-progress
              :percentage="subjectRatios[3].value"
              color="#f56c6c"
            ></el-progress>
          </div>
          <div v-if="subjectRatios && subjectRatios.length > 4">
            {{ subjectRatios[4].title }}
            <el-progress
              :percentage="subjectRatios[4].value"
              color="#f56c6c"
            ></el-progress>
          </div>
          <div v-if="subjectRatios && subjectRatios.length > 5">
            {{ subjectRatios[5].title }}
            <el-progress
              :percentage="subjectRatios[5].value"
              color="#f56c6c"
            ></el-progress>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-row :gutter="20" class="mgb20">
          <el-col v-if="code != 5" :span="6">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-1">
                <i class="el-icon-user-solid grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ userInfo.users }}</div>
                  <div>用户数</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col v-if="code != 5" :span="6">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-1">
                <i class="el-icon-view grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ userInfo.accessNum }}</div>
                  <div>首页访问量</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-2">
                <i class="el-icon-reading grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ courseCount }}</div>
                  <div>上架课程数</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-3">
                <i class="el-icon-s-goods grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ orderCount }}</div>
                  <div>已售课程数</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-card shadow="hover" style="height: 273px">
          <template #header>
            <div class="clearfix">
              <span>最近上架课程</span>
            </div>
          </template>

          <el-table
            :show-header="false"
            :data="recentAddCourses"
            style="width: 100%"
          >
            <el-table-column>
              <template #default="scope">
                <div class="todo-item">
                  {{ scope.row.title }}
                </div>
              </template>
            </el-table-column>

            <el-table-column align="right">
              <template #default="scope">
                <div class="todo-item">
                  {{ scope.row.createTime }}
                </div>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
        <el-card v-if="code != 5" shadow="hover" style="height: 313px">
          <template #header>
            <div class="clearfix">
              <span>最近加入用户</span>
            </div>
          </template>
          <el-table
            :show-header="false"
            :data="userInfo.recentAddUsers"
            style="width: 100%"
          >
            <el-table-column width="120" align="left">
              <template slot-scope="scope">
                <div class="demo-image__preview">
                  <el-image
                    :src="scope.row.avatar"
                    alt="图片获取失败"
                    title="点击查看大图"
                    style="height: 50px"
                    :preview-src-list="[scope.row.avatar]"
                  />
                </div>
              </template>
            </el-table-column>
            <el-table-column align="left">
              <template #default="scope">
                <div class="todo-item">用户名: {{ scope.row.username }}</div>
              </template>
            </el-table-column>
            <el-table-column align="left">
              <template #default="scope">
                <div class="todo-item">
                  {{ scope.row.nickname }}
                </div>
              </template>
            </el-table-column>
            <el-table-column>
              <template #default="scope">
                <div class="todo-item">
                  {{ scope.row.createTime }}
                </div>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
    <el-row v-show="code != 5" :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <schart
            ref="bar"
            class="schart"
            canvasId="bar"
            :options="barCharts"
          ></schart>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <schart
            ref="line"
            class="schart"
            canvasId="line"
            :options="lineCharts"
          ></schart>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Schart from "vue-schart";
import { getDashboardInfo } from "@/api/subject";
import { getUserDashBoardInfo } from "@/api/user";
import { getAWeekUserRegisterAndLoginCount } from "@/api/statistics";
import store from "@/store";
import { mapGetters } from "vuex";

export default {
  components: { Schart },
  data() {
    return {
      teacherName: "",
      courseCount: 0,
      orderCount: 0,
      barCharts: {},
      lineCharts: {},
      recentAddCourses: [],
      subjectRatios: [],
      userInfo: {
        avatar: "",
        username: "",
        users: 0,
        rolename: "",
        lastLoginIp: "",
        lastLoginTime: "",
        recentAddUsers: [],
        accessNum: 0,
      },
    };
  },
  computed: {
    ...mapGetters(["sidebar", "avatar", "name", "role", "code"]),
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      getDashboardInfo().then((resp) => {
        if (resp.code === 200) {
          this.courseCount = resp.data.courseCount;
          this.orderCount = resp.data.orderCount;
          this.subjectRatios = resp.data.subjectRatios;
          this.recentAddCourses = resp.data.recentAddCourses;
          this.teacherName = resp.data.teacherName;
        }
      });
      getUserDashBoardInfo().then((resp) => {
        if (resp.code === 200) {
          this.userInfo = resp.data.userInfo;
        }
      });
      if (this.code != 5) {
        getAWeekUserRegisterAndLoginCount().then((resp) => {
          if (resp.code === 200) {
            this.barCharts = resp.data.logAndRegBC;
            this.lineCharts = resp.data.logAndRegLC;
          }
        });
      }
    },
  },
};
</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
}

.grid-content {
  display: flex;
  align-items: center;
  height: 100px;
}

.grid-cont-right {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: #999;
}

.grid-num {
  font-size: 30px;
  font-weight: bold;
}

.grid-con-icon {
  font-size: 50px;
  width: 100px;
  height: 100px;
  text-align: center;
  line-height: 100px;
  color: #fff;
}

.grid-con-1 .grid-con-icon {
  background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
  color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
  background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
  color: rgb(45, 140, 240);
}

.grid-con-3 .grid-con-icon {
  background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
  color: rgb(242, 94, 67);
}

.user-info {
  display: flex;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 2px solid #ccc;
  margin-bottom: 20px;
}

.user-avator {
  width: 120px;
  height: 120px;
  border-radius: 50%;
}

.user-info-cont {
  padding-left: 50px;
  flex: 1;
  font-size: 14px;
  color: #999;
}

.user-info-cont div:first-child {
  font-size: 30px;
  color: #222;
}

.user-info-list {
  font-size: 14px;
  color: #999;
  line-height: 25px;
}

.user-info-list span {
  margin-left: 70px;
}

.mgb20 {
  margin-bottom: 20px;
}

.todo-item {
  font-size: 14px;
}

.todo-item-del {
  text-decoration: line-through;
  color: #999;
}

.schart {
  width: 100%;
  height: 300px;
}
</style>
