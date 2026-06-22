<template>
  <div class="portal-page">
    <div class="portal-hero"><div><h2>资源中心</h2><p>按条件查询平台开放的资源中心信息。</p></div><el-button icon="el-icon-refresh" @click="loadData">刷新</el-button></div>
    <el-card shadow="never" class="filter-card">
      <el-form :inline="true" :model="query" size="small"><el-form-item label="资源名称"><el-input v-model="query.resourceName" clearable placeholder="请输入资源名称" /></el-form-item><el-form-item label="所属专业"><el-input v-model="query.majorName" clearable placeholder="请输入所属专业" /></el-form-item><el-form-item><el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button></el-form-item></el-form>
    </el-card>
    <el-row :gutter="16">
      <el-col :xs="24" :md="8" v-for="item in resources" :key="item.resourceName">
        <el-card shadow="hover" class="resource-card">
          <div class="resource-title">{{ item.resourceName }}</div>
          <div class="meta">资源名称：{{ item.resourceName || '-' }}</div><div class="meta">资源类型：{{ resourceTypeLabel(item.resourceType) }}</div><div class="meta">所属专业：{{ item.majorName || '-' }}</div><div class="meta">适用课程：{{ item.courseName || '-' }}</div><div class="meta">共享状态：{{ shareStatusLabel(item.shareStatus) }}</div><div class="meta">浏览量：{{ item.viewCount || '-' }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { listPortalResources } from '@/api/portal'
export default {
  name: 'PortalResources',
  data() { return { query: {}, resources: [] } },
  created() { this.loadData() },
  methods: {
    resourceTypeLabel(value) { return ({ '0': '虚拟仿真', '1': '视频', '2': '音频', '3': '文档' })[value] || value || '-' },
    shareStatusLabel(value) { return ({ '0': '开放', '1': '校内', '2': '停用' })[value] || value || '-' },
    openStatusLabel(value) { return ({ '0': '开放', '1': '关闭', '2': '维护' })[value] || value || '-' },
    difficultyLabel(value) { return ({ '1': '初级', '2': '中级', '3': '高级' })[value] || value || '-' },
    loadData() { listPortalResources(this.query).then(res => { this.resources = res.data || [] }) }
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
