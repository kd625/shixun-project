<template>
  <div class="portal-page">
    <div class="portal-hero"><div><h2>实验室</h2><p>按条件查询平台开放的实验室信息。</p></div><el-button icon="el-icon-refresh" @click="loadData">刷新</el-button></div>
    <el-card shadow="never" class="filter-card">
      <el-form :inline="true" :model="query" size="small"><el-form-item label="实验室名称"><el-input v-model="query.labName" clearable placeholder="请输入实验室名称" /></el-form-item><el-form-item label="所属院校"><el-input v-model="query.collegeName" clearable placeholder="请输入所属院校" /></el-form-item><el-form-item><el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button></el-form-item></el-form>
    </el-card>
    <el-row :gutter="16">
      <el-col :xs="24" :md="8" v-for="item in labs" :key="item.labName">
        <el-card shadow="hover" class="resource-card">
          <div class="resource-title">{{ item.labName }}</div>
          <div class="meta">实验室名称：{{ item.labName || '-' }}</div><div class="meta">所属院校：{{ item.collegeName || '-' }}</div><div class="meta">地点：{{ item.location || '-' }}</div><div class="meta">容量：{{ item.capacity || '-' }}</div><div class="meta">开放状态：{{ openStatusLabel(item.openStatus) }}</div><div class="meta">负责人：{{ item.managerName || '-' }}</div><div class="meta">联系方式：{{ item.contactPhone || '-' }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { listPortalLabs } from '@/api/portal'
export default {
  name: 'PortalLabs',
  data() { return { query: {}, labs: [] } },
  created() { this.loadData() },
  methods: {
    resourceTypeLabel(value) { return ({ '0': '虚拟仿真', '1': '视频', '2': '音频', '3': '文档' })[value] || value || '-' },
    shareStatusLabel(value) { return ({ '0': '开放', '1': '校内', '2': '停用' })[value] || value || '-' },
    openStatusLabel(value) { return ({ '0': '开放', '1': '关闭', '2': '维护' })[value] || value || '-' },
    difficultyLabel(value) { return ({ '1': '初级', '2': '中级', '3': '高级' })[value] || value || '-' },
    loadData() { listPortalLabs(this.query).then(res => { this.labs = res.data || [] }) }
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
