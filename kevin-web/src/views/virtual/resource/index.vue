<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="资源名称" prop="resourceName">
        <el-input v-model="queryParams.resourceName" placeholder="请输入资源名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="资源类型" prop="resourceType">
        <el-select v-model="queryParams.resourceType" placeholder="请选择资源类型" clearable>
          <el-option v-for="dict in dict.type.vt_resource_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="所属专业" prop="majorName">
        <el-input v-model="queryParams.majorName" placeholder="请输入所属专业" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="共享状态" prop="shareStatus">
        <el-select v-model="queryParams.shareStatus" placeholder="请选择共享状态" clearable>
          <el-option v-for="dict in dict.type.vt_share_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['virtual:resource:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['virtual:resource:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['virtual:resource:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['virtual:resource:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="resourceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="资源名称" align="center" prop="resourceName" :show-overflow-tooltip="true" />
      <el-table-column label="资源类型" align="center" prop="resourceType">
        <template slot-scope="scope"><dict-tag :options="dict.type.vt_resource_type" :value="scope.row.resourceType" /></template>
      </el-table-column>
      <el-table-column label="所属专业" align="center" prop="majorName" :show-overflow-tooltip="true" />
      <el-table-column label="适用课程" align="center" prop="courseName" :show-overflow-tooltip="true" />
      <el-table-column label="共享状态" align="center" prop="shareStatus">
        <template slot-scope="scope"><dict-tag :options="dict.type.vt_share_status" :value="scope.row.shareStatus" /></template>
      </el-table-column>
      <el-table-column label="浏览量" align="center" prop="viewCount" width="90" :show-overflow-tooltip="true" />
      <el-table-column label="收藏量" align="center" prop="collectCount" width="90" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" width="190" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['virtual:resource:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['virtual:resource:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body :before-close="handleDialogClose" @closed="handleDialogClosed">
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="资源名称" prop="resourceName"><el-input v-model="form.resourceName" placeholder="请输入资源名称" /></el-form-item>
        <el-form-item label="资源类型" prop="resourceType">
          <el-radio-group v-model="form.resourceType">
            <el-radio v-for="dict in dict.type.vt_resource_type" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="所属专业" prop="majorName"><el-input v-model="form.majorName" placeholder="请输入所属专业" /></el-form-item>
        <el-form-item label="适用课程" prop="courseName"><el-input v-model="form.courseName" placeholder="请输入适用课程" /></el-form-item>
        <el-form-item label="封面地址" prop="coverUrl"><el-input v-model="form.coverUrl" placeholder="请输入封面地址" /></el-form-item>
        <el-form-item label="附件地址" prop="fileUrl"><el-input v-model="form.fileUrl" placeholder="请输入附件地址" /></el-form-item>
        <el-form-item label="共享状态" prop="shareStatus">
          <el-radio-group v-model="form.shareStatus">
            <el-radio v-for="dict in dict.type.vt_share_status" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="浏览量" prop="viewCount"><el-input-number v-model="form.viewCount" controls-position="right" :min="0" /></el-form-item>
        <el-form-item label="收藏量" prop="collectCount"><el-input-number v-model="form.collectCount" controls-position="right" :min="0" /></el-form-item>
        <el-form-item label="简介" prop="introduction">
          <el-input v-model="form.introduction" type="textarea" :rows="4" placeholder="请输入简介" />
          <el-button class="ai-generate-btn" type="primary" plain size="mini" icon="el-icon-magic-stick" :loading="aiGenerating" @click="handleGenerateIntro">
            {{ aiGenerating ? '生成中' : 'AI生成' }}
          </el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="aiGenerating" :disabled="aiGenerating" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listResource, getResource, delResource, addResource, updateResource } from '@/api/virtual/resource'
import { streamGenerateIntro } from '@/api/virtual/ai'

export default {
  name: 'VtResource',
  dicts: ['vt_resource_type', 'vt_share_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      resourceList: [],
      title: '',
      open: false,
      aiGenerating: false,
      aiAbortController: null,
      queryParams: { pageNum: 1, pageSize: 10, resourceName: undefined, resourceType: undefined, majorName: undefined, shareStatus: undefined },
      form: {},
      rules: {
        resourceName: [{ required: true, message: '资源名称不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  beforeDestroy() {
    this.stopAiGenerate()
  },
  methods: {
    getList() {
      this.loading = true
      listResource(this.queryParams).then(response => {
        this.resourceList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.handleDialogClose(() => {
        this.open = false
      })
    },
    reset(options = {}) {
      if (options.stopAi !== false) {
        this.stopAiGenerate()
      }
      this.form = { resourceId: undefined, resourceName: undefined, resourceType: '0', majorName: undefined, courseName: undefined, coverUrl: undefined, fileUrl: undefined, shareStatus: '0', viewCount: 0, collectCount: 0, introduction: undefined }
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
      this.ids = selection.map(item => item.resourceId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleDialogClose(done) {
      this.stopAiGenerate()
      done()
    },
    handleDialogClosed() {
      this.reset({ stopAi: false })
    },
    stopAiGenerate() {
      if (this.aiAbortController) {
        this.aiAbortController.abort()
        this.aiAbortController = null
      }
      this.aiGenerating = false
    },
    handleGenerateIntro() {
      if (!this.form.resourceName) {
        this.$modal.msgWarning('请先填写资源名称')
        return
      }
      this.stopAiGenerate()
      this.form.introduction = ''
      this.aiGenerating = true
      this.aiAbortController = streamGenerateIntro({
        scene: 'resource',
        resourceName: this.form.resourceName,
        resourceType: this.getResourceTypeLabel(this.form.resourceType),
        majorName: this.form.majorName,
        courseName: this.form.courseName
      }, {
        message: content => {
          this.form.introduction = (this.form.introduction || '') + content
        },
        done: () => {
          this.aiGenerating = false
          this.aiAbortController = null
        },
        error: message => {
          this.aiGenerating = false
          this.aiAbortController = null
          this.$modal.msgError(message)
        }
      })
    },
    getResourceTypeLabel(value) {
      const item = this.dict.type.vt_resource_type.find(dict => dict.value === value)
      return item ? item.label : value
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加实训资源'
    },
    handleUpdate(row) {
      this.reset()
      const resourceId = row.resourceId || this.ids
      getResource(resourceId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改实训资源'
      })
    },
    submitForm() {
      if (this.aiGenerating) {
        this.$modal.msgWarning('AI简介生成中，请稍后再提交')
        return
      }
      this.$refs.form.validate(valid => {
        if (valid) {
          const isUpdate = this.form.resourceId !== undefined
          const request = isUpdate ? updateResource(this.form) : addResource(this.form)
          request.then(() => {
            this.$modal.msgSuccess(isUpdate ? '修改成功' : '新增成功')
            this.stopAiGenerate()
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.resourceId || this.ids
      this.$modal.confirm('是否确认删除实训资源编号为"' + ids + '"的数据项？').then(function() {
        return delResource(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('virtual/resource/export', { ...this.queryParams }, 'resource_' + new Date().getTime() + '.xlsx')
    }
  }
}
</script>

<style scoped>
.ai-generate-btn {
  margin-top: 8px;
}
</style>
