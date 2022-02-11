<template>
  <div class="app-container">
    <el-form :inline="true" ref="form" :model="form">
      <el-form-item label="课程名称" prop="title">
        <el-input v-model="form.title" placeholder="课程名称"></el-input>
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
          <el-option label="所有讲师" key="" value=""> </el-option>
          <el-option
            v-for="(item, index) in teachers"
            :label="item.name"
            :key="item.id"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="课程状态">
        <el-select v-model="form.status" placeholder="请选择">
          <el-option label="已上架" key="1" value="1"> </el-option>
          <el-option label="已下架" key="0" value="0"> </el-option>
          <el-option label="全部" key="" value=""> </el-option>
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

      <el-form-item label="关键字过滤">
        <el-input placeholder="对查询结果进行过滤" v-model="filterText">
        </el-input>
      </el-form-item>
    </el-form>

    <div class="down-tree" style="width: 100%">
      <el-button
        icon="el-icon-circle-plus-outline"
        type="text"
        @click="() => appendRoot(data)"
      >
        添加课程
      </el-button>
      <div class="current">
        <el-tree
          v-loading="listLoading"
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
          :default-expanded-keys="defaultShowNodes"
          @node-expand="handleNodeExpand"
          @node-collapse="handleNodeCollapse"
          @node-click="handleNodeClick"
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
                  : data.chapterId
                  ? `第${data.sort}节:`
                  : ""
              }}&nbsp; {{ data.title }}&nbsp;{{
                data.children && data.children.length > 0
                  ? `(${data.children.length})`
                  : ""
              }}

              <el-button
                v-if="data.teacherId"
                type="text"
                size="mini"
                icon="el-icon-user"
              >
                {{ data.teacherName }}
              </el-button>
            </span>
            <span style="margin-left: 100px">
              <el-button
                v-if="!data.chapterId"
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
    </div>
    <br />
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
    <el-dialog
      :title="chapterFormTitle"
      :visible.sync="chapterFormVisible"
      center=""
    >
      <el-form :model="form" :label-width="formLabelWidth">
        <el-form-item label="章节名称">
          <el-input v-model="chapter.title"></el-input>
        </el-form-item>

        <el-form-item label="当前章节">
          <el-input-number
            v-model="chapter.sort"
            :min="1"
            :max="100"
            label="总课时"
          ></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="chapterFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="chapterFormSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      :title="videoFormTitle"
      :visible.sync="videoFormVisible"
      center=""
    >
      <el-form :model="form" :label-width="formLabelWidth">
        <el-form-item label="小节名称">
          <el-input v-model="video.title"></el-input>
        </el-form-item>

        <el-form-item v-show="video != {}" label="当前小节">
          <el-input-number
            v-model="video.sort"
            :min="1"
            :max="100"
            label="当前小节"
          ></el-input-number>
        </el-form-item>

        <el-form-item label="是否收费">
          <el-radio-group v-model="video.isFree">
            <el-radio-button label="1">免费</el-radio-button>
            <el-radio-button label="0">付费</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <el-form-item v-if="video.videoSourcePath" label="视频路径">
          {{ video.videoSourcePath }}
          <video width="320" controls>
            <source :src="video.videoSourcePath" type="video/mp4" />
            您的浏览器不支持 HTML5 video 标签。
          </video>
        </el-form-item>

        <el-form-item label="上传视频">
          <el-upload
            class="upload-demo"
            :action="baseURL + '/upload/video'"
            :on-preview="handlePreview"
            :on-success="handleVideoSuccess"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            :before-upload="beforeUpload"
            name="video"
            multiple
            :limit="1"
          >
            <el-button size="small" type="primary"
              >{{ video.videoSourcePath ? "重新" : "点击" }}上传</el-button
            >
            <div slot="tip" class="el-upload__tip">
              只能上传mp4/avi文件，且不超过300MB
            </div>
          </el-upload>
          <span v-show="video.videoOriginalName != ''">{{
            percentageFlag
          }}</span>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="videoFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="videoFormSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
let id = 1000;

import { getTree, removeById } from "@/api/course";
import { save, removeChapterById } from "@/api/chapter";
import { saveVideo, removeVideoById } from "@/api/video";
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
      defaultShowNodes: [],
      listLoading: true,
      form: {
        title: "",
        subjectId: null,
        courseId: null,
        createTime: "",
        status: "",
        current: 1,
        size: 8,
        total: 0,
      },
      chapter: {
        title: "",
        courseId: "",
        sort: null,
      },
      video: {
        title: "",
        chapterId: null,
        isFree: 1,
        sort: null,
        videoSourcePath: "",
        duration: null,
        videoOriginalName: "",
        size: null,
      },
      chapterFormTitle: "",
      videoFormTitle: "",
      sizes: [],
      subjectId: [],
      teachers: [],
      subjects: [],
      chapterFormVisible: false,
      videoFormVisible: false,
      formLabelWidth: "120px",
      baseURL: process.env.VUE_APP_BASE_API,
      percentageFlag: "",
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
          this.teachers = resp.data;
        }
      });
      getList().then((resp) => {
        if (resp.code === 200) {
          this.subjects = resp.data.subjects;
        }
      });
    },
    fetchData() {
      this.listLoading = true;
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
            this.listLoading = false;
          }
        }
      });
    },
    // 树节点展开
    handleNodeExpand(data) {
      // 保存当前展开的节点
      let flag = false;
      this.defaultShowNodes.some((item) => {
        if (item === data.id) {
          // 判断当前节点是否存在， 存在不做处理
          flag = true;
          return true;
        }
      });
      if (!flag) {
        // 不存在则存到数组里
        this.defaultShowNodes.push(data.id);
      }
    },
    // 树节点关闭
    handleNodeCollapse(data) {
      this.defaultShowNodes.some((item, i) => {
        if (item === data.id) {
          // 删除关闭节点
          this.defaultShowNodes.length = i;
        }
      });
    },
    handleNodeClick(data) {
      console.log(data.id);
      console.log(this.defaultShowNodes);
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
        this.chapter = {};
        this.chapter.courseId = data.id;
        this.chapterFormTitle = "添加章节";
        this.chapterFormVisible = true;
      } else if (data.courseId) {
        this.video = {};
        this.video.isFree = 1;
        this.video.chapterId = data.id;
        this.videoFormTitle = "添加小节";
        this.videoFormVisible = true;
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
        this.chapter = data;
        this.chapterFormTitle = "修改章节";
        this.chapterFormVisible = true;
      } else if (data.chapterId) {
        this.video = data;
        this.videoFormTitle = "修改小节";
        this.videoFormVisible = true;
      }
    },
    appendRoot(data) {
      this.$router.push({
        path: "/course/save",
      });
    },
    confirmRemove(data) {
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
    remove(node, data) {
      if (data.children && data.children.length > 0) {
        this.$confirm("该节点下的内容不为空，是否继续删除?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            this.confirmRemove(data);
          })
          .catch(() => {
            return;
          });
      } else {
        this.confirmRemove(data);
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
    chapterFormSubmit() {
      save(this.chapter).then((resp) => {
        if (resp.code === 200) {
          this.$message.success(`${this.chapterFormTitle}成功`);
          if (this.chapterFormTitle === "修改章节")
            this.chapterFormVisible = false;
        }
        this.fetchData();
      });
    },
    videoFormSubmit() {
      saveVideo(this.video).then((resp) => {
        if (resp.code === 200) {
          this.$message.success(`${this.videoFormTitle}成功`);
          if (this.videoFormTitle === "修改小节") this.videoFormVisible = false;
        }
        this.fetchData();
      });
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
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    beforeUpload() {
      this.percentageFlag = "努力上传中..";
    },
    handleVideoSuccess(res, file) {
      this.video.videoSourcePath = res.data.path;
      this.video.videoOriginalName = res.data.videoOriginalName;
      this.video.duration = res.data.duration;
      this.video.size = res.data.size;
      this.percentageFlag = "";
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
  min-height: 300px;
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
