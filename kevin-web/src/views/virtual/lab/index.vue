<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="实验室名称" prop="labName">
        <el-input v-model="queryParams.labName" placeholder="请输入实验室名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="所属院校" prop="collegeName">
        <el-input v-model="queryParams.collegeName" placeholder="请输入所属院校" clearable @keyup.enter.native="handleQuery" />
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
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['virtual:lab:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['virtual:lab:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['virtual:lab:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['virtual:lab:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="labList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="实验室名称" align="center" prop="labName" :show-overflow-tooltip="true" />
      <el-table-column label="所属院校" align="center" prop="collegeName" :show-overflow-tooltip="true" />
      <el-table-column label="地点" align="center" prop="location" :show-overflow-tooltip="true" />
      <el-table-column label="容量" align="center" prop="capacity" width="80" :show-overflow-tooltip="true" />
      <el-table-column label="开放状态" align="center" prop="openStatus">
        <template slot-scope="scope"><dict-tag :options="dict.type.vt_open_status" :value="scope.row.openStatus" /></template>
      </el-table-column>
      <el-table-column label="负责人" align="center" prop="managerName" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" width="190" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['virtual:lab:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['virtual:lab:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="实验室名称" prop="labName"><el-input v-model="form.labName" placeholder="请输入实验室名称" /></el-form-item>
        <el-form-item label="所属院校" prop="collegeName"><el-input v-model="form.collegeName" placeholder="请输入所属院校" /></el-form-item>
        <el-form-item label="地点" prop="location"><el-input v-model="form.location" placeholder="请输入地点" /></el-form-item>
        <el-form-item label="容量" prop="capacity"><el-input-number v-model="form.capacity" controls-position="right" :min="0" /></el-form-item>
        <el-form-item label="开放状态" prop="openStatus">
          <el-radio-group v-model="form.openStatus">
            <el-radio v-for="dict in dict.type.vt_open_status" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="负责人" prop="managerName"><el-input v-model="form.managerName" placeholder="请输入负责人" /></el-form-item>
        <el-form-item label="联系方式" prop="contactPhone"><el-input v-model="form.contactPhone" placeholder="请输入联系方式" /></el-form-item>
        <el-form-item label="简介" prop="introduction"><el-input v-model="form.introduction" type="textarea" :rows="3" placeholder="请输入简介" /></el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listLab, getLab, delLab, addLab, updateLab } from '@/api/virtual/lab'


export default {
  name: 'VtLab',
  dicts: ['vt_open_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      labList: [],
      title: '',
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, labName: undefined, collegeName: undefined, openStatus: undefined },
      form: {},
      rules: {
        labName: [{ required: true, message: '实验室名称不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listLab(this.queryParams).then(response => {
        this.labList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = { labId: undefined, labName: undefined, collegeName: undefined, location: undefined, capacity: 0, openStatus: '0', managerName: undefined, contactPhone: undefined, introduction: undefined }
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
      this.ids = selection.map(item => item.labId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加实验室'
    },
    handleUpdate(row) {
      this.reset()
      const labId = row.labId || this.ids
      getLab(labId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改实验室'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const isUpdate = this.form.labId !== undefined
          const request = isUpdate ? updateLab(this.form) : addLab(this.form)
          request.then(() => {
            this.$modal.msgSuccess(isUpdate ? '修改成功' : '新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.labId || this.ids
      this.$modal.confirm('是否确认删除实验室编号为"' + ids + '"的数据项？').then(function() {
        return delLab(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('virtual/lab/export', { ...this.queryParams }, 'lab_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
