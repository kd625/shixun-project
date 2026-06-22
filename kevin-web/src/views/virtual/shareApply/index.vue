<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="申请人" prop="applicantName">
        <el-input v-model="queryParams.applicantName" placeholder="请输入申请人" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="单位" prop="organization">
        <el-input v-model="queryParams.organization" placeholder="请输入单位" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="申请类型" prop="applyType">
        <el-select v-model="queryParams.applyType" placeholder="请选择申请类型" clearable>
          <el-option v-for="dict in dict.type.vt_apply_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="申请状态" prop="applyStatus">
        <el-select v-model="queryParams.applyStatus" placeholder="请选择申请状态" clearable>
          <el-option v-for="dict in dict.type.vt_apply_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['virtual:shareApply:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['virtual:shareApply:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['virtual:shareApply:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['virtual:shareApply:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="shareApplyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="申请人" align="center" prop="applicantName" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="手机号" align="center" prop="phone" width="120" :show-overflow-tooltip="true" />
      <el-table-column label="单位" align="center" prop="organization" :show-overflow-tooltip="true" />
      <el-table-column label="申请类型" align="center" prop="applyType">
        <template slot-scope="scope"><dict-tag :options="dict.type.vt_apply_type" :value="scope.row.applyType" /></template>
      </el-table-column>
      <el-table-column label="申请对象" align="center" prop="targetName" :show-overflow-tooltip="true" />
      <el-table-column label="预约时间" align="center" prop="reserveTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.reserveTime) }}</span></template>
      </el-table-column>
      <el-table-column label="人数" align="center" prop="userCount" width="80" :show-overflow-tooltip="true" />
      <el-table-column label="申请状态" align="center" prop="applyStatus">
        <template slot-scope="scope"><dict-tag :options="dict.type.vt_apply_status" :value="scope.row.applyStatus" /></template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="190" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['virtual:shareApply:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-check" @click="handleAudit(scope.row, '1')" v-hasPermi="['virtual:shareApply:audit']">通过</el-button>
          <el-button size="mini" type="text" icon="el-icon-close" @click="handleAudit(scope.row, '2')" v-hasPermi="['virtual:shareApply:audit']">驳回</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="申请人" prop="applicantName"><el-input v-model="form.applicantName" placeholder="请输入申请人" /></el-form-item>
        <el-form-item label="手机号" prop="phone"><el-input v-model="form.phone" placeholder="请输入手机号" /></el-form-item>
        <el-form-item label="单位" prop="organization"><el-input v-model="form.organization" placeholder="请输入单位" /></el-form-item>
        <el-form-item label="申请类型" prop="applyType">
          <el-radio-group v-model="form.applyType">
            <el-radio v-for="dict in dict.type.vt_apply_type" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="关联ID" prop="targetId"><el-input-number v-model="form.targetId" controls-position="right" :min="0" /></el-form-item>
        <el-form-item label="关联对象" prop="targetName"><el-input v-model="form.targetName" placeholder="请输入关联对象" /></el-form-item>
        <el-form-item label="预约时间" prop="reserveTime"><el-date-picker v-model="form.reserveTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择预约时间" /></el-form-item>
        <el-form-item label="使用人数" prop="userCount"><el-input-number v-model="form.userCount" controls-position="right" :min="0" /></el-form-item>
        <el-form-item label="申请状态" prop="applyStatus">
          <el-radio-group v-model="form.applyStatus">
            <el-radio v-for="dict in dict.type.vt_apply_status" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" prop="auditOpinion"><el-input v-model="form.auditOpinion" type="textarea" :rows="3" placeholder="请输入审核意见" /></el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listShareApply, getShareApply, delShareApply, addShareApply, updateShareApply, auditShareApply } from '@/api/virtual/shareApply'


export default {
  name: 'VtShareApply',
  dicts: ['vt_apply_type', 'vt_apply_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      shareApplyList: [],
      title: '',
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, applicantName: undefined, organization: undefined, applyType: undefined, applyStatus: undefined },
      form: {},
      rules: {
        applicantName: [{ required: true, message: '申请人不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listShareApply(this.queryParams).then(response => {
        this.shareApplyList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = { applyId: undefined, applicantName: undefined, phone: undefined, organization: undefined, applyType: '0', targetId: undefined, targetName: undefined, reserveTime: undefined, userCount: 1, applyStatus: '0', auditOpinion: undefined }
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
      this.ids = selection.map(item => item.applyId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加共享申请'
    },
    handleUpdate(row) {
      this.reset()
      const applyId = row.applyId || this.ids
      getShareApply(applyId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改共享申请'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const isUpdate = this.form.applyId !== undefined
          const request = isUpdate ? updateShareApply(this.form) : addShareApply(this.form)
          request.then(() => {
            this.$modal.msgSuccess(isUpdate ? '修改成功' : '新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.applyId || this.ids
      this.$modal.confirm('是否确认删除共享申请编号为"' + ids + '"的数据项？').then(function() {
        return delShareApply(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleAudit(row, status) {
      const text = status === '1' ? '通过' : '驳回'
      const data = { ...row, applyStatus: status }
      this.$modal.confirm('是否确认' + text + '该共享申请？').then(function() {
        return auditShareApply(data)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('审核成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('virtual/shareApply/export', { ...this.queryParams }, 'shareApply_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
