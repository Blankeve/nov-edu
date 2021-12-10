<template>
  <div class="app-container">
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="ID" width="50">
        <template slot-scope="scope">
          {{ scope.$index }}
        </template>
      </el-table-column>

      <el-table-column label="name" width="110" align="center">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>

      <el-table-column label="intro" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.intro }}</span>
        </template>
      </el-table-column>

      <el-table-column label="career" width="300" align="center">
        <template slot-scope="scope">
          {{ scope.row.career }}
        </template>
      </el-table-column>

      <el-table-column label="level" width="50" align="center">
        <template slot-scope="scope">
          {{ scope.row.level }}
        </template>
      </el-table-column>

      <el-table-column label="avatar" width="200" align="center">
        <template slot-scope="scope">
          <img :src="scope.row.avatar" />
        </template>
      </el-table-column>

      <!-- <el-table-column class-name="status-col" label="Status" width="110" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column> -->

      <el-table-column
        align="center"
        prop="created_at"
        label="joinDate"
        width="200"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.joinDate }}</span>
        </template>
      </el-table-column>

      <el-table-column 
       align="center"
      label="操作">
      <template slot-scope="scope">
        <el-button
          @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
        <el-button
          type="danger"
          @click="handleDelete(scope.$index, scope.row)">删除</el-button>
      </template>
    </el-table-column>

    </el-table>
    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page.current"
        :page-sizes="sizes"
        :page-size="page.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="page.total"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { getList } from "@/api/teacher";

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
      page: {
        current: 1,
        size: 4,
        total: 0
      },
      sizes: []
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      this.listLoading = true;
      this.sizes = [this.page.size/2,this.page.size,this.page.size*2]
      getList(this.page).then((response) => {
        let data = response.data;
        this.page.current = data.current;
        this.page.size = data.size;
        this.page.total = data.total;
        this.list = data.records;
        this.listLoading = false;
      });
    },
    handleCurrentChange(p){
      this.page.current = p;
      this.fetchData();
    },
    handleSizeChange(s){
      this.page.size = s;
      this.fetchData();
    }
  },
};
</script>
