<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="学员姓名" prop="studentName">
        <el-input v-model="queryParams.studentName" placeholder="请输入学员姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="学号编号" prop="studentNo">
        <el-input v-model="queryParams.studentNo" placeholder="请输入学号编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="完成状态" prop="completeStatus">
        <el-select v-model="queryParams.completeStatus" placeholder="请选择完成状态" clearable>
          <el-option v-for="dict in dict.type.vt_complete_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['virtual:record:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['virtual:record:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['virtual:record:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['virtual:record:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="学员姓名" align="center" prop="studentName" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="学号编号" align="center" prop="studentNo" width="120" :show-overflow-tooltip="true" />
      <el-table-column label="计划ID" align="center" prop="planId" width="90" :show-overflow-tooltip="true" />
      <el-table-column label="完成状态" align="center" prop="completeStatus">
        <template slot-scope="scope"><dict-tag :options="dict.type.vt_complete_status" :value="scope.row.completeStatus" /></template>
      </el-table-column>
      <el-table-column label="得分" align="center" prop="score" width="80" :show-overflow-tooltip="true" />
      <el-table-column label="用时" align="center" prop="durationMinutes" width="80" :show-overflow-tooltip="true" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.startTime) }}</span></template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.endTime) }}</span></template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="190" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['virtual:record:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['virtual:record:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="关联计划" prop="planId">
          <el-select v-model="form.planId" placeholder="请选择关联计划" filterable clearable>
            <el-option v-for="item in planOptions" :key="item.planId" :label="item.planName" :value="item.planId" />
          </el-select>
        </el-form-item>
        <el-form-item label="学员姓名" prop="studentName"><el-input v-model="form.studentName" placeholder="请输入学员姓名" /></el-form-item>
        <el-form-item label="学号编号" prop="studentNo"><el-input v-model="form.studentNo" placeholder="请输入学号编号" /></el-form-item>
        <el-form-item label="开始时间" prop="startTime"><el-date-picker v-model="form.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择开始时间" /></el-form-item>
        <el-form-item label="结束时间" prop="endTime"><el-date-picker v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择结束时间" /></el-form-item>
        <el-form-item label="完成状态" prop="completeStatus">
          <el-radio-group v-model="form.completeStatus">
            <el-radio v-for="dict in dict.type.vt_complete_status" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="得分" prop="score"><el-input-number v-model="form.score" controls-position="right" :min="0" /></el-form-item>
        <el-form-item label="用时分钟" prop="durationMinutes"><el-input-number v-model="form.durationMinutes" controls-position="right" :min="0" /></el-form-item>
        <el-form-item label="评价" prop="evaluation"><el-input v-model="form.evaluation" type="textarea" :rows="3" placeholder="请输入评价" /></el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRecord, getRecord, delRecord, addRecord, updateRecord } from '@/api/virtual/record'
import { optionselectPlan } from '@/api/virtual/plan'

export default {
  name: 'VtTrainingRecord',
  dicts: ['vt_complete_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      recordList: [],
      planOptions: [],
      title: '',
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, studentName: undefined, studentNo: undefined, completeStatus: undefined },
      form: {},
      rules: {
        studentName: [{ required: true, message: '学员姓名不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    optionselectPlan().then(response => { this.planOptions = response.data || [] })
  },
  methods: {
    getList() {
      this.loading = true
      listRecord(this.queryParams).then(response => {
        this.recordList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = { recordId: undefined, planId: undefined, studentName: undefined, studentNo: undefined, startTime: undefined, endTime: undefined, completeStatus: '0', score: 0, durationMinutes: 0, evaluation: undefined }
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
      this.ids = selection.map(item => item.recordId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加过程结果'
    },
    handleUpdate(row) {
      this.reset()
      const recordId = row.recordId || this.ids
      getRecord(recordId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改过程结果'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const isUpdate = this.form.recordId !== undefined
          const request = isUpdate ? updateRecord(this.form) : addRecord(this.form)
          request.then(() => {
            this.$modal.msgSuccess(isUpdate ? '修改成功' : '新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.recordId || this.ids
      this.$modal.confirm('是否确认删除过程结果编号为"' + ids + '"的数据项？').then(function() {
        return delRecord(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('virtual/record/export', { ...this.queryParams }, 'record_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
