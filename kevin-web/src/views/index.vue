<template>
  <div class="app-container platform-home">
    <div class="hero-band">
      <div>
        <h2>虚拟仿真实训教学管理及资源共享云平台</h2>
        <p>面向双高院校的实训场所、设备、资源、课程和共享开放一体化管理平台。</p>
      </div>
      <el-button type="primary" icon="el-icon-data-analysis" @click="$router.push('/portal/screen')">查看数据大屏</el-button>
    </div>

    <el-row :gutter="16" class="stat-row">
      <el-col :xs="12" :sm="6" v-for="item in statCards" :key="item.label">
        <el-card shadow="never" class="stat-card">
          <i :class="item.icon"></i>
          <div class="stat-value">{{ item.value }}</div>
          <div class="stat-label">{{ item.label }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :md="12">
        <el-card shadow="never">
          <div slot="header">业务入口</div>
          <el-row :gutter="12">
            <el-col :span="12" v-for="entry in entries" :key="entry.path">
              <div class="entry" @click="$router.push(entry.path)">
                <i :class="entry.icon"></i>
                <span>{{ entry.title }}</span>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card shadow="never">
          <div slot="header">运行概况</div>
          <el-table :data="overviewRows" size="small" :show-header="false">
            <el-table-column prop="name" />
            <el-table-column prop="value" align="right" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getDashboardSummary } from '@/api/virtual/dashboard'

export default {
  name: 'Index',
  data() {
    return {
      summary: {},
      entries: [
        { title: '资源中心', path: '/portal/resources', icon: 'el-icon-folder-opened' },
        { title: '实训实验', path: '/portal/experiments', icon: 'el-icon-magic-stick' },
        { title: '实验室开放', path: '/portal/labs', icon: 'el-icon-school' },
        { title: '共享申请', path: '/portal/share', icon: 'el-icon-connection' }
      ]
    }
  },
  computed: {
    statCards() {
      return [
        { label: '实验室', value: this.summary.labCount || 0, icon: 'el-icon-school' },
        { label: '实训资源', value: this.summary.resourceCount || 0, icon: 'el-icon-collection' },
        { label: '仿真设备', value: this.summary.deviceCount || 0, icon: 'el-icon-monitor' },
        { label: '参与人数', value: this.summary.participantCount || 0, icon: 'el-icon-user' }
      ]
    },
    overviewRows() {
      return [
        { name: '实验完成率', value: (this.summary.completionRate || 0) + '%' },
        { name: '平均成绩', value: this.summary.averageScore || 0 },
        { name: '资源类型统计', value: (this.summary.resourceTypeStats || []).length + ' 类' },
        { name: '共享申请状态', value: (this.summary.applyStatusStats || []).length + ' 类' }
      ]
    }
  },
  created() {
    getDashboardSummary().then(res => { this.summary = res.data || {} })
  }
}
</script>

<style scoped>
.platform-home { background: #f5f7fb; min-height: calc(100vh - 84px); }
.hero-band { display: flex; justify-content: space-between; align-items: center; padding: 24px; margin-bottom: 16px; color: #fff; background: linear-gradient(120deg, #1d4f91, #16836f); border-radius: 6px; }
.hero-band h2 { margin: 0 0 8px; font-size: 24px; font-weight: 600; }
.hero-band p { margin: 0; opacity: .9; }
.stat-row { margin-bottom: 16px; }
.stat-card { height: 132px; }
.stat-card i { font-size: 24px; color: #16836f; }
.stat-value { margin-top: 16px; font-size: 30px; font-weight: 700; color: #1f2d3d; }
.stat-label { color: #606266; }
.entry { display: flex; align-items: center; gap: 10px; height: 58px; padding: 0 14px; margin-bottom: 12px; border: 1px solid #e5e9f2; border-radius: 6px; cursor: pointer; background: #fff; }
.entry:hover { color: #16836f; border-color: #16836f; }
.entry i { font-size: 20px; }
</style>
