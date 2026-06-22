<template>
  <div class="portal-page">
    <div class="portal-hero"><div><h2>共享开放申请</h2><p>提交资源或实验室开放共享申请，后台审核后安排使用。</p></div></div>
    <el-card shadow="never">
      <el-form ref="form" :model="form" :rules="rules" label-width="110px" style="max-width:720px">
        <el-form-item label="申请人" prop="applicantName"><el-input v-model="form.applicantName" /></el-form-item>
        <el-form-item label="手机号" prop="phone"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="单位" prop="organization"><el-input v-model="form.organization" /></el-form-item>
        <el-form-item label="申请类型" prop="applyType"><el-radio-group v-model="form.applyType"><el-radio label="0">资源</el-radio><el-radio label="1">实验室</el-radio></el-radio-group></el-form-item>
        <el-form-item label="对象名称" prop="targetName"><el-input v-model="form.targetName" placeholder="请输入资源或实验室名称" /></el-form-item>
        <el-form-item label="预约时间" prop="reserveTime"><el-date-picker v-model="form.reserveTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" /></el-form-item>
        <el-form-item label="使用人数" prop="userCount"><el-input-number v-model="form.userCount" :min="1" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" :rows="3" /></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-check" @click="submit">提交申请</el-button><el-button @click="reset">重置</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script>
import { addPortalShare } from '@/api/portal'
export default {
  name: 'PortalShare',
  data() { return { form: { applyType: '0', userCount: 1 }, rules: { applicantName: [{ required: true, message: '申请人不能为空', trigger: 'blur' }], targetName: [{ required: true, message: '对象名称不能为空', trigger: 'blur' }] } } },
  methods: {
    reset() { this.form = { applyType: '0', userCount: 1 }; this.resetForm('form') },
    submit() { this.$refs.form.validate(valid => { if (valid) { addPortalShare(this.form).then(() => { this.$modal.msgSuccess('申请提交成功'); this.reset() }) } }) }
  }
}
</script>
<style scoped>
.portal-page { padding: 20px; background: #f5f7fb; min-height: calc(100vh - 84px); }
.portal-hero { display: flex; justify-content: space-between; align-items: center; padding: 24px; margin-bottom: 16px; color: #fff; background: linear-gradient(120deg, #123c69, #16836f); border-radius: 6px; }
.portal-hero h2 { margin: 0 0 8px; font-size: 24px; font-weight: 600; }
.portal-hero p { margin: 0; opacity: .9; }
.filter-card { margin-bottom: 16px; }
.stat-value { font-size: 28px; font-weight: 700; color: #1f2d3d; }
.stat-label { color: #606266; margin-top: 8px; }
.resource-card { margin-bottom: 16px; min-height: 190px; }
.resource-title { font-size: 16px; font-weight: 600; color: #1f2d3d; margin-bottom: 10px; }
.meta { color: #606266; line-height: 24px; }
.chart { height: 360px; }
</style>
