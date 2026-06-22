<template>
  <div class="portal-page">
    <div class="portal-hero">
      <div><h2>虚拟仿真实训教学管理及资源共享云平台</h2><p>汇聚跨专业、跨院校、跨地域的虚拟仿真实训资源。</p></div>
      <el-button type="success" icon="el-icon-connection" @click="$router.push('/portal/share')">共享开放申请</el-button>
    </div>
    <el-row :gutter="16">
      <el-col :xs="12" :sm="6" v-for="item in statCards" :key="item.label">
        <el-card shadow="never"><div class="stat-value">{{ item.value }}</div><div class="stat-label">{{ item.label }}</div></el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" style="margin-top:16px">
      <el-col :xs="24" :md="8" v-for="entry in entries" :key="entry.path">
        <el-card shadow="hover" class="resource-card" @click.native="$router.push(entry.path)">
          <div class="resource-title"><i :class="entry.icon"></i> {{ entry.title }}</div>
          <div class="meta">{{ entry.desc }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { getPortalHome } from '@/api/portal'
export default {
  name: 'PortalHome',
  data() { return { summary: {}, entries: [
    { title: '资源中心', path: '/portal/resources', icon: 'el-icon-collection', desc: '查看虚拟仿真、视频、音频、文档等资源。' },
    { title: '实训实验', path: '/portal/experiments', icon: 'el-icon-magic-stick', desc: '查看开放实验、难度和预计实训时长。' },
    { title: '实验室开放', path: '/portal/labs', icon: 'el-icon-school', desc: '查看实验室容量、位置、负责人和开放状态。' }
  ] } },
  computed: { statCards() { return [
    { label: '实验室', value: this.summary.labCount || 0 },
    { label: '资源', value: this.summary.resourceCount || 0 },
    { label: '设备', value: this.summary.deviceCount || 0 },
    { label: '参与人数', value: this.summary.participantCount || 0 }
  ] } },
  created() { getPortalHome().then(res => { this.summary = res.data || {} }) }
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
