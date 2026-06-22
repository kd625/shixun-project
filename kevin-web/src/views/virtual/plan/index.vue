<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="计划名称" prop="planName">
        <el-input v-model="queryParams.planName" placeholder="请输入计划名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="班级对象" prop="classTarget">
        <el-input v-model="queryParams.classTarget" placeholder="请输入班级对象" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="计划状态" prop="planStatus">
        <el-select v-model="queryParams.planStatus" placeholder="请选择计划状态" clearable>
          <el-option v-for="dict in dict.type.vt_plan_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="负责人" prop="managerName">
        <el-input v-model="queryParams.managerName" placeholder="请输入负责人" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['virtual:plan:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['virtual:plan:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['virtual:plan:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['virtual:plan:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="planList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="计划名称" align="center" prop="planName" :show-overflow-tooltip="true" />
      <el-table-column label="课程ID" align="center" prop="courseId" width="90" :show-overflow-tooltip="true" />
      <el-table-column label="实验ID" align="center" prop="experimentId" width="90" :show-overflow-tooltip="true" />
      <el-table-column label="班级对象" align="center" prop="classTarget" :show-overflow-tooltip="true" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.startTime) }}</span></template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.endTime) }}</span></template>
      </el-table-column>
      <el-table-column label="计划状态" align="center" prop="planStatus">
        <template slot-scope="scope"><dict-tag :options="dict.type.vt_plan_status" :value="scope.row.planStatus" /></template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="190" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['virtual:plan:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['virtual:plan:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="计划名称" prop="planName"><el-input v-model="form.planName" placeholder="请输入计划名称" /></el-form-item>
        <el-form-item label="关联课程" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请选择关联课程" filterable clearable>
            <el-option v-for="item in courseOptions" :key="item.courseId" :label="item.courseName" :value="item.courseId" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联实验" prop="experimentId">
          <el-select v-model="form.experimentId" placeholder="请选择关联实验" filterable clearable>
            <el-option v-for="item in experimentOptions" :key="item.experimentId" :label="item.experimentName" :value="item.experimentId" />
          </el-select>
        </el-form-item>
        <el-form-item label="班级对象" prop="classTarget"><el-input v-model="form.classTarget" placeholder="请输入班级对象" /></el-form-item>
        <el-form-item label="开始时间" prop="startTime"><el-date-picker v-model="form.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择开始时间" /></el-form-item>
        <el-form-item label="结束时间" prop="endTime"><el-date-picker v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择结束时间" /></el-form-item>
        <el-form-item label="计划状态" prop="planStatus">
          <el-radio-group v-model="form.planStatus">
            <el-radio v-for="dict in dict.type.vt_plan_status" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="负责人" prop="managerName"><el-input v-model="form.managerName" placeholder="请输入负责人" /></el-form-item>
        <el-form-item label="备注" prop="remark"><el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" /></el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPlan, getPlan, delPlan, addPlan, updatePlan } from '@/api/virtual/plan'
import { optionselectCourse } from '@/api/virtual/course'
import { optionselectExperiment } from '@/api/virtual/experiment'

export default {
  name: 'VtTeachingPlan',
  dicts: ['vt_plan_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      planList: [],
      courseOptions: [],
      experimentOptions: [],
      title: '',
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, planName: undefined, classTarget: undefined, planStatus: undefined, managerName: undefined },
      form: {},
      rules: {
        planName: [{ required: true, message: '计划名称不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    optionselectCourse().then(response => { this.courseOptions = response.data || [] })
    optionselectExperiment().then(response => { this.experimentOptions = response.data || [] })
  },
  methods: {
    getList() {
      this.loading = true
      listPlan(this.queryParams).then(response => {
        this.planList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = { planId: undefined, planName: undefined, courseId: undefined, experimentId: undefined, classTarget: undefined, startTime: undefined, endTime: undefined, planStatus: '0', managerName: undefined, remark: undefined }
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
      this.ids = selection.map(item => item.planId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加教学计划'
    },
    handleUpdate(row) {
      this.reset()
      const planId = row.planId || this.ids
      getPlan(planId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改教学计划'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const isUpdate = this.form.planId !== undefined
          const request = isUpdate ? updatePlan(this.form) : addPlan(this.form)
          request.then(() => {
            this.$modal.msgSuccess(isUpdate ? '修改成功' : '新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.planId || this.ids
      this.$modal.confirm('是否确认删除教学计划编号为"' + ids + '"的数据项？').then(function() {
        return delPlan(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('virtual/plan/export', { ...this.queryParams }, 'plan_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
