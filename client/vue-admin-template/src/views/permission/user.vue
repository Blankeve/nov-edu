<template>
  <div class="app-container">
    <el-form :inline="true" ref="form" :model="form">
      <el-form-item prop="nickname">
        <el-input
          class="mid-input"
          v-model="form.nickname"
          placeholder="用户昵称"
        ></el-input>
      </el-form-item>

      <el-form-item prop="username">
        <el-input
          class="mid-input"
          v-model="form.username"
          placeholder="用户名"
        ></el-input>
      </el-form-item>

      <el-form-item label="所属角色">
        <el-select v-model="form.roleId" placeholder="请选择角色">
          <el-option
            v-for="(item, index) in roles"
            :label="item.name"
            :key="item.id"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="注册时间" prop="createTime">
        <el-date-picker
          v-model="dateRange"
          type="datetimerange"
          :picker-options="pickerOptions"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right"
        >
        </el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="onSubmit"
          >查询</el-button
        >
        <el-button
          type="danger"
          icon="el-icon-refresh-left"
          @click="resetForm('form')"
          >重置</el-button
        >
        <el-button
          type="success"
          icon="el-icon-download"
          @click="exportUserPage"
          >导出当前</el-button
        >
      </el-form-item>
    </el-form>

    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="#" width="50">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="头像" width="120" align="center">
        <template slot-scope="scope">
          <div class="demo-image__preview">
            <el-image
              :src="scope.row.avatar"
              alt="图片获取失败"
              title="点击查看大图"
              width="100px"
              :preview-src-list="[scope.row.avatar]"
            />
          </div>
        </template>
      </el-table-column>

      <el-table-column width="150" label="昵称" align="center">
        <template slot-scope="scope">
          <el-tag size="medium">
            {{ scope.row.nickname }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column width="100" label="用户名" align="center">
        <template slot-scope="scope">
          {{ scope.row.username }}
        </template>
      </el-table-column>

          <el-table-column width="150" label="手机号" align="center">
        <template slot-scope="scope">
          {{ scope.row.mobile }}
        </template>
      </el-table-column>

      <el-table-column width="100" label="角色类型" align="center">
        <template slot-scope="scope">
          {{ scope.row.roleName }}
        </template>
      </el-table-column>

      <el-table-column label="用户状态" width="150" align="center">
        <template slot-scope="scope">
          {{ scope.row.isDisabled == 1 ? "禁言中" : "正常" }}
        </template>
      </el-table-column>

      <el-table-column align="center" prop="created_at" label="注册时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="created_at" label="最后登录时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.lastLoginTime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="最后登录地址" align="center">
        <template slot-scope="scope">
          {{ scope.row.lastLoginIp }}
        </template>
      </el-table-column>

      <el-table-column fixed="right" width="400" align="center" label="操作">
        <template slot-scope="scope">
          <!-- <el-button @click="handleEdit(scope.row.id)" icon="el-icon-edit"
            >编辑</el-button
          > -->
          <el-button
            type="warning"
            icon="el-icon-setting"
            @click="handleSelectRole(scope.row.id)"
            >分配角色</el-button
          >
          <el-button
            v-if="scope.row.roleCode == 5"
            type="primary"
            icon="el-icon-setting"
            @click="handleBindTeacher(scope.row.id)"
            >绑定讲师</el-button
          >
          <!-- <el-button
            :type="scope.row.isDisabled == 1 ? 'danger' : 'primary'"
            :icon="
              'el-icon-circle-' +
              (scope.row.isDisabled == 1 ? 'close' : 'check')
            "
            @click="handleRelease(scope.row.id, scope.row.isDisabled)"
          >
            {{ scope.row.isDisabled == 1 ? "恢复" : "禁言" }}</el-button
          > -->
          &nbsp;
          <el-popconfirm
            title="默认密码为666666,确定重置该用户密码?"
            @onConfirm="handleResetPwd(scope.row.id)"
          >
            <el-button slot="reference" type="danger">重置密码</el-button>
          </el-popconfirm>

          <!-- <el-button
            type="danger"
            @click="handleDelete(scope.row.id)"
            icon="el-icon-delete"
            >删除</el-button
          > -->
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
    <!--分配菜单-->
    <el-dialog title="分配角色" :visible.sync="dialogVisibleRole" width="30%">
      <div align="center">
        <el-form :inline="true" ref="form" :model="form">
          <el-form-item label="所属角色">
            <el-select v-model="roleId" placeholder="请选择角色">
              <el-option
                v-for="(item, index) in roles"
                :label="item.name"
                :key="item.id"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <el-button type="primary" @click="handleSaveRole()">确认</el-button>
        <el-button @click="handleCancel">取消</el-button>
      </div>
    </el-dialog>

    <!--绑定讲师-->
    <el-dialog
      title="绑定讲师"
      :visible.sync="dialogVisibleTeacher"
      width="30%"
    >
      <div align="center">
        <el-form :inline="true" ref="form" :model="tForm">
          <el-form-item label="绑定讲师">
            <el-select v-model="tForm.id" placeholder="请选择讲师">
              <el-option
                v-for="(item, index) in teachers"
                :label="item.name"
                :key="item.id"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <el-button type="primary" @click="handleSaveBind()">确认</el-button>
        <el-button @click="handleCancel2">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getList, saveRoleByUid } from "@/api/role";
import { getPage, resetPwd, exportPage } from "@/api/user";
import { exportExcel } from "@/utils/excel";
import { getAllAndBindId, updateBindByUidAndId } from "@/api/teacher";

export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: "success",
        draft: "gray",
        deleted: "danger",
      };
      return statusMap[status];
    },
  },
  data() {
    return {
      list: null,
      listLoading: true,
      form: {
        username: "",
        nickname: "",
        mobile: "",
        avatar: "",
        roleId: null,
        current: 1,
        size: 8,
        total: 0,
        startTime: null,
        endTime: null,
      },
      uid: null,
      roleId: null,
      //菜单相关
      dialogVisibleRole: false,
      dialogVisibleTeacher: false,
      roles: [],
      tForm: {
        id: "",
        uid: "",
      },
      teachers: [],
      dateRange: [],
      pickerOptions: {
        shortcuts: [
          {
            text: "最近一周",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近三个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
      sizes: [],
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      this.listLoading = true;
      this.handleDateRange();
      this.sizes =
        this.form.size > 1
          ? [this.form.size / 2, this.form.size, this.form.size * 2]
          : [this.form.size, this.form.size * 2];
      getPage(this.form).then((response) => {
        let data = response.data;
        this.form.current = data.current;
        this.form.size = data.size;
        this.form.total = data.total;
        this.list = data.records;
        this.listLoading = false;
      });

      getList().then((resp) => {
        this.roles = resp.data;
      });
    },
    exportUserPage() {
      exportPage(this.form).then((resp) => {
        exportExcel(resp);
      });
    },
    handleBindTeacher(uid) {
      this.tForm.uid = uid;
      this.dialogVisibleTeacher = true;
      getAllAndBindId(uid).then((resp) => {
        if (resp.code === 200) {
          this.teachers = resp.data.list;
          this.tForm.id = resp.data.bind;
        }
      });
    },
    handleSelectRole(uid) {
      this.uid = uid;
      this.dialogVisibleRole = true;
    },
    handleResetPwd(uid) {
      resetPwd(uid).then((resp) => {
        if (resp.code === 200) {
          this.$message.success("重置密码成功");
        }
      });
    },
    handleSaveBind() {
      updateBindByUidAndId(this.tForm).then((resp) => {
        if (resp.code === 200) {
          this.$message.success("绑定讲师成功");
          this.dialogVisibleTeacher = false;
        }
      });
    },
    handleCancel() {
      this.dialogVisibleRole = false;
    },
    handleCancel2() {
      this.dialogVisibleTeacher = false;
    },
    handleSaveRole() {
      saveRoleByUid({ uid: this.uid, roleId: this.roleId }).then((resp) => {
        if (resp.code === 200) {
          this.$message.success("分配角色成功");
          this.dialogVisibleRole = false;
          this.fetchData();
        }
      });
    },
    exportTeacherPage() {
      exportPage(this.form).then((resp) => {
        exportExcel(resp);
      });
    },
    handleDateLength(str) {
      str += "";
      if (str.length < 2) return "0" + str;
      return str;
    },
    handleDateFormat(time) {
      let formatDate =
        time.getFullYear() +
        "-" +
        this.handleDateLength(time.getMonth() + 1) +
        "-" +
        this.handleDateLength(time.getDate()) +
        " " +
        this.handleDateLength(time.getHours()) +
        ":" +
        this.handleDateLength(time.getMinutes()) +
        ":" +
        this.handleDateLength(time.getSeconds());
      return formatDate;
    },
    handleDateRange() {
      if (this.dateRange && this.dateRange.length > 0) {
        this.form.startTime = this.handleDateFormat(
          new Date(this.dateRange[0])
        );
        this.form.endTime = this.handleDateFormat(new Date(this.dateRange[1]));
      }
    },
    handleCurrentChange(p) {
      this.form.current = p;
      this.fetchData();
    },
    handleSizeChange(s) {
      this.form.size = s;
      this.fetchData();
    },
    handleDelete(id) {
      removeById(id).then((response) => {
        this.fetchData();
      });
    },
    handleEdit(id) {},
    onSubmit() {
      this.fetchData();
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dateRange = [];
      this.form.roleId = null;
      this.form.username = "";
    },
  },
};
</script>

<style>
.mid-input {
  width: 80px;
}
.el-pagination {
  text-align: center;
}
</style>