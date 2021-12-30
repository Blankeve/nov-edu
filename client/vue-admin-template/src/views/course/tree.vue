<template>
  <div class="app-container">
    <el-form :inline="true" ref="form" :model="form">
      <el-form-item label="课程名称" prop="title">
        <el-input
          class="mid-input"
          v-model="form.title"
          placeholder="课程名称"
        ></el-input>
      </el-form-item>

      <el-form-item label="课程分类">
        <el-cascader
          v-model="subjectId"
          :options="subjects"
          :props="{ expandTrigger: 'hover', label: 'title', value: 'id' }"
        ></el-cascader>
      </el-form-item>
      <el-form-item label="课程讲师">
        <el-select v-model="form.teacherId" placeholder="请选择讲师">
          <el-option
            v-for="(item, index) in teacher"
            :label="item.name"
            :key="item.id"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="添加时间" prop="createTime">
        <el-date-picker
          v-model="form.createTime"
          type="datetime"
          placeholder="课程添加时间"
          value-format="yyyy-MM-dd HH:mm:ss"
        >
        </el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="searchForm">查询</el-button>
        <el-button @click="resetForm('form')">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="down-tree" style="width: 100%">
      <el-button type="text" size="mini" @click="() => appendRoot(data)">
        添加课程
      </el-button>
      <el-button type="text" size="mini" @click="() => (expandAll = true)">
        展开所有
      </el-button>
      <el-button type="text" size="mini" @click="() => (expandAll = false)">
        收缩所有
      </el-button>
      <div class="current">
        <el-tree
          :data="data"
          node-key="id"
          ref="tree"
          :filter-node-method="filterNode"
          @node-drag-start="handleDragStart"
          @node-drag-enter="handleDragEnter"
          @node-drag-leave="handleDragLeave"
          @node-drag-over="handleDragOver"
          @node-drag-end="handleDragEnd"
          @node-drop="handleDrop"
          :expand-on-click-node="false"
        >
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <i
              :class="
                data.subjectId
                  ? 'course-node-icon'
                  : data.courseId
                  ? 'chapter-node-icon '
                  : 'video-node-icon'
              "
            ></i>
            <span>
              {{
                data.courseId
                  ? `第${data.sort}章:`
                  :(data.chapterId
                  ? `第${data.sort}节:`
                  : "") 
              }}&nbsp; {{ data.title }}&nbsp;{{
                data.children && data.children.length > 0
                  ? `(${data.children.length})`
                  : ""
              }}</span
            >
            <span style="margin-left: 100px">
              <el-button
                type="text"
                size="mini"
                icon="el-icon-circle-plus-outline"
                @click="() => append(data)"
              >
              </el-button>
              <el-button
                type="text"
                size="mini"
                icon="el-icon-edit"
                circle
                @click="() => edit(node, data)"
              >
              </el-button>
              <el-button
                type="text"
                size="mini"
                icon="el-icon-delete"
                @click="() => remove(node, data)"
              >
              </el-button>
            </span>
          </span>
        </el-tree>
      </div>
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
    </div>
    <br />
    <el-button type="primary" @click="onSubmit">保存</el-button>
    <el-button @click="initTree">重置</el-button>
  </div>
</template>

<script>
let id = 1000;

import { getTree, update, removeById } from "@/api/course";
import { removeChapterById } from "@/api/chapter";
import { removeVideoById } from "@/api/video";
import { getAll } from "@/api/teacher";
import { getList } from "@/api/subject";

export default {
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },
  },
  data() {
    return {
      filterText: "",
      data: [],
      resetData: [],
      expandAll: false,
      form: {
        title: "",
        subjectId: null,
        courseId: null,
        createTime: "",
        current: 1,
        size: 8,
        total: 0,
      },
      sizes: [],
      subjectId: [],
      teacher: [],
      subjects: [],
    };
  },
  created() {
    this.getOptions();
    this.fetchData();
  },
  methods: {
    getOptions() {
      getAll().then((resp) => {
        if (resp.code === 200) {
          this.teacher = resp.data;
        }
      });
      getList().then((resp) => {
        if (resp.code === 200) {
          this.subjects = resp.data.subjects;
        }
      });
    },
    fetchData() {
      this.sizes =
        this.form.size > 1
          ? [this.form.size / 2, this.form.size, this.form.size * 2]
          : [this.form.size, this.form.size * 2];
      if (this.subjectId && this.subjectId.length > 0)
        this.form.subjectId = this.subjectId[this.subjectId.length - 1];
      getTree(this.form).then((resp) => {
        if (resp.code === 200) {
          let data = resp.data;
          if (data) {
            this.data = data.records;
            this.resetData = JSON.parse(JSON.stringify(data.records));
            this.form.current = data.current;
            this.form.size = data.size;
            this.form.total = data.total;
          }
        }
      });
    },
    initTree() {
      this.data = JSON.parse(JSON.stringify(this.resetData));
    },
    handleCurrentChange(p) {
      this.form.current = p;
      this.fetchData();
    },
    handleSizeChange(s) {
      this.form.size = s;
      this.fetchData();
    },
    searchForm() {
      this.fetchData();
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.subjectId = null;
      this.form.subjectId = null;
    },
    onSubmit() {
      update({ eduSubjects: this.data }).then((resp) => {
        this.$message({
          message: "保存成功",
          type: resp.code === 200 ? "success" : "error",
        });
        this.fetchData();
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.title.indexOf(value) !== -1;
    },
    append(data) {
      if (data.subjectId) {
        this.$router.push({
          path: "/chapter/save",
          query: {
            course: data.id,
          },
        });
      } else if (data.courseId) {
        this.$router.push({
          path: "/video/save",
          query: {
            chapter: data.id,
          },
        });
      }
    },
    edit(node, data) {
      if (data.subjectId) {
        this.$router.push({
          path: "/course/edit",
          query: {
            course: data.id,
          },
        });
      } else if (data.courseId) {
        this.$router.push({
          path: "/chapter/edit",
          query: {
            chapter: data.id,
          },
        });
      } else if (data.chapterId) {
        this.$router.push({
          path: "/video/edit",
          query: {
            video: data.id,
          },
        });
      }
    },
    appendRoot(data) {
      this.$router.push({
        path: "/course/save",
      });
    },
    remove(node, data) {
      if (data.subjectId) {
        removeById(data.id).then((resp) => {
          if (resp.code === 200) {
            this.$message.success("删除成功");
            this.fetchData();
          }
        });
      } else if (data.courseId) {
        removeChapterById(data.id).then((resp) => {
          if (resp.code === 200) {
            this.$message.success("删除成功");
            this.fetchData();
          }
        });
      } else if (data.chapterId) {
        removeVideoById(data.id).then((resp) => {
          if (resp.code === 200) {
            this.$message.success("删除成功");
            this.fetchData();
          }
        });
      }
    },

    renderContent(h, { node, data, store }) {
      return (
        <span class="custom-tree-node">
          <span>{node.title}</span>
          <span>
            <el-button
              size="mini"
              type="text"
              on-click={() => this.append(data)}
            >
              Append
            </el-button>
            <el-button
              size="mini"
              type="text"
              on-click={() => this.remove(node, data)}
            >
              Delete
            </el-button>
          </span>
        </span>
      );
    },

    handleDragStart(node, ev) {
      console.log("drag start", node);
    },
    handleDragEnter(draggingNode, dropNode, ev) {
      console.log("tree drag enter: ", dropNode.title);
    },
    handleDragLeave(draggingNode, dropNode, ev) {
      console.log("tree drag leave: ", dropNode.title);
    },
    handleDragOver(draggingNode, dropNode, ev) {
      console.log("tree drag over: ", dropNode.title);
    },
    handleDragEnd(draggingNode, dropNode, dropType, ev) {
      console.log("tree drag end: ", dropNode && dropNode.title, dropType);
    },
    handleDrop(draggingNode, dropNode, dropType, ev) {
      console.log("tree drop: ", dropNode.title, dropType);
    },
    allowDrop(draggingNode, dropNode, type) {
      if (!dropNode.data.title) {
        return type !== "inner";
      } else {
        return true;
      }
    },
    allowDrag(draggingNode) {
      return draggingNode.data.title != undefined;
    },
  },
};
</script>

<style >
.course-node-icon {
  background: url("../../icons/png/book.png") no-repeat;
  content: "";
  display: block;
  width: 28px;
  height: 28px;
  font-size: 28px;
  background-size: 25px;
}

.chapter-node-icon {
  background: url("../../icons/png/chapter.png") no-repeat;
  content: "";
  display: block;
  width: 28px;
  height: 28px;
  font-size: 28px;
  background-size: 25px;
}

.video-node-icon {
  background: url("../../icons/png/video.png") no-repeat;
  content: "";
  display: block;
  width: 28px;
  height: 28px;
  font-size: 28px;
  background-size: 25px;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  font-size: 14px;
  padding-right: 8px;
}

.down-tree {
  flex: 1;
  max-width: 1200px;
  height: 100%;
  background: rgba(245, 248, 250, 1);
  border-radius: 3px;
  border: 1px solid rgba(211, 219, 222, 1);
  margin-left: 12px;
  padding: 14px;
}

.el-tree-node__label {
  font-size: 12px;
}

.el-tree .el-tree-node__expand-icon.expanded {
  -webkit-transform: rotate(0deg);
  transform: rotate(0deg);
}
.current .el-tree .el-icon-caret-right:before {
  background: url("../../icons/png/expand.png") no-repeat;
  content: "";
  display: block;
  width: 28px;
  height: 28px;
  font-size: 28px;
  background-size: 25px;
}

.current .el-tree-node__expand-icon.is-leaf::before {
  background: none;
  content: "";
  display: block;
  width: 28px;
  height: 28px;
  font-size: 28px;
  background-size: 25px;
}

.el-tree-node__content {
  background: #f5f8fa;
  height: 36px;
}

.el-tree-node.is-current > .el-tree-node__content {
  background-color: #fde9be !important;
  color: #333333;
}
</style>
