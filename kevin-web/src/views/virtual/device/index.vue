<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="设备名称" prop="deviceName">
        <el-input v-model="queryParams.deviceName" placeholder="请输入设备名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="设备编号" prop="deviceCode">
        <el-input v-model="queryParams.deviceCode" placeholder="请输入设备编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="运行状态" prop="runStatus">
        <el-select v-model="queryParams.runStatus" placeholder="请选择运行状态" clearable>
          <el-option v-for="dict in dict.type.vt_run_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="在线状态" prop="onlineStatus">
        <el-select v-model="queryParams.onlineStatus" placeholder="请选择在线状态" clearable>
          <el-option v-for="dict in dict.type.vt_online_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['virtual:device:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['virtual:device:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['virtual:device:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['virtual:device:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="deviceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="设备名称" align="center" prop="deviceName" :show-overflow-tooltip="true" />
      <el-table-column label="设备编号" align="center" prop="deviceCode" :show-overflow-tooltip="true" />
      <el-table-column label="实验室ID" align="center" prop="labId" width="90" :show-overflow-tooltip="true" />
      <el-table-column label="设备类型" align="center" prop="deviceType" :show-overflow-tooltip="true" />
      <el-table-column label="运行状态" align="center" prop="runStatus">
        <template slot-scope="scope"><dict-tag :options="dict.type.vt_run_status" :value="scope.row.runStatus" /></template>
      </el-table-column>
      <el-table-column label="在线状态" align="center" prop="onlineStatus">
        <template slot-scope="scope"><dict-tag :options="dict.type.vt_online_status" :value="scope.row.onlineStatus" /></template>
      </el-table-column>
      <el-table-column label="检测时间" align="center" prop="lastCheckTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.lastCheckTime) }}</span></template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="190" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['virtual:device:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['virtual:device:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="设备名称" prop="deviceName"><el-input v-model="form.deviceName" placeholder="请输入设备名称" /></el-form-item>
        <el-form-item label="设备编号" prop="deviceCode"><el-input v-model="form.deviceCode" placeholder="请输入设备编号" /></el-form-item>
        <el-form-item label="所属实验室" prop="labId">
          <el-select v-model="form.labId" placeholder="请选择所属实验室" filterable clearable>
            <el-option v-for="item in labOptions" :key="item.labId" :label="item.labName" :value="item.labId" />
          </el-select>
        </el-form-item>
        <el-form-item label="设备类型" prop="deviceType"><el-input v-model="form.deviceType" placeholder="请输入设备类型" /></el-form-item>
        <el-form-item label="运行状态" prop="runStatus">
          <el-radio-group v-model="form.runStatus">
            <el-radio v-for="dict in dict.type.vt_run_status" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="在线状态" prop="onlineStatus">
          <el-radio-group v-model="form.onlineStatus">
            <el-radio v-for="dict in dict.type.vt_online_status" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="检测时间" prop="lastCheckTime"><el-date-picker v-model="form.lastCheckTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择检测时间" /></el-form-item>
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
import { listDevice, getDevice, delDevice, addDevice, updateDevice } from '@/api/virtual/device'
import { optionselectLab } from '@/api/virtual/lab'

export default {
  name: 'VtDevice',
  dicts: ['vt_run_status', 'vt_online_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      deviceList: [],
      labOptions: [],
      title: '',
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, deviceName: undefined, deviceCode: undefined, runStatus: undefined, onlineStatus: undefined },
      form: {},
      rules: {
        deviceName: [{ required: true, message: '设备名称不能为空', trigger: 'blur' }],
        deviceCode: [{ required: true, message: '设备编号不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    optionselectLab().then(response => { this.labOptions = response.data || [] })
  },
  methods: {
    getList() {
      this.loading = true
      listDevice(this.queryParams).then(response => {
        this.deviceList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = { deviceId: undefined, deviceName: undefined, deviceCode: undefined, labId: undefined, deviceType: undefined, runStatus: '0', onlineStatus: '0', lastCheckTime: undefined, remark: undefined }
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
      this.ids = selection.map(item => item.deviceId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加仿真设备'
    },
    handleUpdate(row) {
      this.reset()
      const deviceId = row.deviceId || this.ids
      getDevice(deviceId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改仿真设备'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const isUpdate = this.form.deviceId !== undefined
          const request = isUpdate ? updateDevice(this.form) : addDevice(this.form)
          request.then(() => {
            this.$modal.msgSuccess(isUpdate ? '修改成功' : '新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.deviceId || this.ids
      this.$modal.confirm('是否确认删除仿真设备编号为"' + ids + '"的数据项？').then(function() {
        return delDevice(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('virtual/device/export', { ...this.queryParams }, 'device_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>
