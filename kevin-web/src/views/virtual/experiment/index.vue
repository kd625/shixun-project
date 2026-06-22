<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="实验名称" prop="experimentName">
        <el-input v-model="queryParams.experimentName" placeholder="请输入实验名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="难度" prop="difficulty">
        <el-select v-model="queryParams.difficulty" placeholder="请选择难度" clearable>
          <el-option v-for="dict in dict.type.vt_difficulty" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="开放状态" prop="openStatus">
        <el-select v-model="queryParams.openStatus" placeholder="请选择开放状态" clearable>
          <el-option v-for="dict in dict.type.vt_open_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['virtual:experiment:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['virtual:experiment:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['virtual:experiment:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['virtual:experiment:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="experimentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="实验名称" align="center" prop="experimentName" :show-overflow-tooltip="true" />
      <el-table-column label="课程ID" align="center" prop="courseId" width="90" :show-overflow-tooltip="true" />
      <el-table-column label="资源ID" align="center" prop="resourceId" width="90" :show-overflow-tooltip="true" />
      <el-table-column label="难度" align="center" prop="difficulty">
        <template slot-scope="scope"><dict-tag :options="dict.type.vt_difficulty" :value="scope.row.difficulty" /></template>
      </el-table-column>
      <el-table-column label="时长" align="center" prop="durationMinutes" width="80" :show-overflow-tooltip="true" />
      <el-table-column label="开放状态" align="center" prop="openStatus">
        <template slot-scope="scope"><dict-tag :options="dict.type.vt_open_status" :value="scope.row.openStatus" /></template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="190" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['virtual:experiment:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['virtual:experiment:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="实验名称" prop="experimentName"><el-input v-model="form.experimentName" placeholder="请输入实验名称" /></el-form-item>
        <el-form-item label="关联课程" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请选择关联课程" filterable clearable>
            <el-option v-for="item in courseOptions" :key="item.courseId" :label="item.courseName" :value="item.courseId" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联资源" prop="resourceId">
          <el-select v-model="form.resourceId" placeholder="请选择关联资源" filterable clearable>
            <el-option v-for="item in resourceOptions" :key="item.resourceId" :label="item.resourceName" :value="item.resourceId" />
          </el-select>
        </el-form-item>
        <el-form-item label="难度" prop="difficulty">
          <el-radio-group v-model="form.difficulty">
            <el-radio v-for="dict in dict.type.vt_difficulty" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="预计时长" prop="durationMinutes"><el-input-number v-model="form.durationMinutes" controls-position="right" :min="0" /></el-form-item>
        <el-form-item label="开放状态" prop="openStatus">
          <el-radio-group v-model="form.openStatus">
            <el-radio v-for="dict in dict.type.vt_open_status" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="实验简介" prop="introduction"><el-input v-model="form.introduction" type="textarea" :rows="3" placeholder="请输入实验简介" /></el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listExperiment, getExperiment, delExperiment, addExperiment, updateExperiment } from '@/api/virtual/experiment'
import { optionselectCourse } from '@/api/virtual/course'
import { optionselectResource } from '@/api/virtual/resource'

export default {
  name: 'VtExperiment',
  dicts: ['vt_difficulty', 'vt_open_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      experimentList: [],
      courseOptions: [],
      resourceOptions: [],
      title: '',
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, experimentName: undefined, difficulty: undefined, openStatus: undefined },
      form: {},
      rules: {
        experimentName: [{ required: true, message: '实验名称不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    optionselectCourse().then(response => { this.courseOptions = response.data || [] })
    optionselectResource().then(response => { this.resourceOptions = response.data || [] })
  },
  methods: {
    getList() {
      this.loading = true
      listExperiment(this.queryParams).then(response => {
        this.experimentList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = { experimentId: undefined, experimentName: undefined, courseId: undefined, resourceId: undefined, difficulty: '1', durationMinutes: 45, openStatus: '0', introduction: undefined }
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
      this.ids = selection.map(item => item.experimentId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加实训实验'
    },
    handleUpdate(row) {
      this.reset()
      const experimentId = row.experimentId || this.ids
      getExperiment(experimentId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改实训实验'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const isUpdate = this.form.experimentId !== undefined
          const request = isUpdate ? updateExperiment(this.form) : addExperiment(this.form)
          request.then(() => {
            this.$modal.msgSuccess(isUpdate ? '修改成功' : '新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.experimentId || this.ids
      this.$modal.confirm('是否确认删除实训实验编号为"' + ids + '"的数据项？').then(function() {
        return delExperiment(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('virtual/experiment/export', { ...this.queryParams }, 'experiment_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
