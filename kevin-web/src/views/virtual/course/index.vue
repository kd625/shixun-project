<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="课程名称" prop="courseName">
        <el-input v-model="queryParams.courseName" placeholder="请输入课程名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="专业方向" prop="majorDirection">
        <el-input v-model="queryParams.majorDirection" placeholder="请输入专业方向" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="授课教师" prop="teacherName">
        <el-input v-model="queryParams.teacherName" placeholder="请输入授课教师" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="课程状态" prop="courseStatus">
        <el-select v-model="queryParams.courseStatus" placeholder="请选择课程状态" clearable>
          <el-option v-for="dict in dict.type.vt_course_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['virtual:course:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['virtual:course:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['virtual:course:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['virtual:course:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="courseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="课程名称" align="center" prop="courseName" :show-overflow-tooltip="true" />
      <el-table-column label="专业方向" align="center" prop="majorDirection" :show-overflow-tooltip="true" />
      <el-table-column label="课时" align="center" prop="classHours" width="80" :show-overflow-tooltip="true" />
      <el-table-column label="授课教师" align="center" prop="teacherName" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="课程状态" align="center" prop="courseStatus">
        <template slot-scope="scope"><dict-tag :options="dict.type.vt_course_status" :value="scope.row.courseStatus" /></template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="190" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['virtual:course:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['virtual:course:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="课程名称" prop="courseName"><el-input v-model="form.courseName" placeholder="请输入课程名称" /></el-form-item>
        <el-form-item label="专业方向" prop="majorDirection"><el-input v-model="form.majorDirection" placeholder="请输入专业方向" /></el-form-item>
        <el-form-item label="课时" prop="classHours"><el-input-number v-model="form.classHours" controls-position="right" :min="0" /></el-form-item>
        <el-form-item label="授课教师" prop="teacherName"><el-input v-model="form.teacherName" placeholder="请输入授课教师" /></el-form-item>
        <el-form-item label="课程状态" prop="courseStatus">
          <el-radio-group v-model="form.courseStatus">
            <el-radio v-for="dict in dict.type.vt_course_status" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="课程简介" prop="introduction"><el-input v-model="form.introduction" type="textarea" :rows="3" placeholder="请输入课程简介" /></el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCourse, getCourse, delCourse, addCourse, updateCourse } from '@/api/virtual/course'


export default {
  name: 'VtCourse',
  dicts: ['vt_course_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      courseList: [],
      title: '',
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, courseName: undefined, majorDirection: undefined, teacherName: undefined, courseStatus: undefined },
      form: {},
      rules: {
        courseName: [{ required: true, message: '课程名称不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listCourse(this.queryParams).then(response => {
        this.courseList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = { courseId: undefined, courseName: undefined, majorDirection: undefined, classHours: 0, teacherName: undefined, courseStatus: '0', introduction: undefined }
      this.resetForm('form')
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.courseId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加课程'
    },
    handleUpdate(row) {
      this.reset()
      const courseId = row.courseId || this.ids
      getCourse(courseId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改课程'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const isUpdate = this.form.courseId !== undefined
          const request = isUpdate ? updateCourse(this.form) : addCourse(this.form)
          request.then(() => {
            this.$modal.msgSuccess(isUpdate ? '修改成功' : '新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.courseId || this.ids
      this.$modal.confirm('是否确认删除课程编号为"' + ids + '"的数据项？').then(function() {
        return delCourse(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('virtual/course/export', { ...this.queryParams }, 'course_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
