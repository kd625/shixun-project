<template>
  <div class="dashboard-page">
    <el-row :gutter="16" class="stat-row">
      <el-col :xs="12" :sm="6" v-for="item in statCards" :key="item.label">
        <el-card shadow="never"><div class="stat-value">{{ item.value }}</div><div class="stat-label">{{ item.label }}</div></el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16">
      <el-col :xs="24" :md="12"><el-card shadow="never"><div ref="resourceChart" class="chart"></div></el-card></el-col>
      <el-col :xs="24" :md="12"><el-card shadow="never"><div ref="deviceChart" class="chart"></div></el-card></el-col>
    </el-row>
    <el-row :gutter="16" class="chart-row">
      <el-col :xs="24" :md="12"><el-card shadow="never"><div ref="applyChart" class="chart"></div></el-card></el-col>
      <el-col :xs="24" :md="12"><el-card shadow="never"><div ref="trendChart" class="chart"></div></el-card></el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getDashboardSummary } from '@/api/virtual/dashboard'

export default {
  name: 'VirtualDashboard',
  data() { return { summary: {}, charts: [] } },
  computed: {
    statCards() {
      return [
        { label: '实验室数量', value: this.summary.labCount || 0 },
        { label: '资源数量', value: this.summary.resourceCount || 0 },
        { label: '设备数量', value: this.summary.deviceCount || 0 },
        { label: '参与人数', value: this.summary.participantCount || 0 }
      ]
    }
  },
  mounted() { this.loadData() },
  beforeDestroy() { this.charts.forEach(chart => chart && chart.dispose()) },
  methods: {
    loadData() {
      getDashboardSummary().then(res => {
        this.summary = res.data || {}
        this.$nextTick(this.renderCharts)
      })
    },
    renderCharts() {
      this.charts.forEach(chart => chart && chart.dispose())
      const resourceChart = echarts.init(this.$refs.resourceChart)
      const deviceChart = echarts.init(this.$refs.deviceChart)
      const applyChart = echarts.init(this.$refs.applyChart)
      const trendChart = echarts.init(this.$refs.trendChart)
      this.charts = [resourceChart, deviceChart, applyChart, trendChart]
      resourceChart.setOption({ title: { text: '资源类型占比' }, tooltip: {}, series: [{ type: 'pie', radius: '60%', data: this.summary.resourceTypeStats || [] }] })
      deviceChart.setOption({ title: { text: '设备在线状态' }, tooltip: {}, xAxis: { type: 'category', data: (this.summary.deviceStatusStats || []).map(item => item.name) }, yAxis: { type: 'value' }, series: [{ type: 'bar', data: (this.summary.deviceStatusStats || []).map(item => item.value), itemStyle: { color: '#1d4f91' } }] })
      applyChart.setOption({ title: { text: '申请状态分布' }, tooltip: {}, series: [{ type: 'pie', radius: ['42%', '68%'], data: this.summary.applyStatusStats || [] }] })
      trendChart.setOption({ title: { text: '近 7 天实训趋势' }, tooltip: {}, xAxis: { type: 'category', data: (this.summary.trainingTrend || []).map(item => item.name) }, yAxis: { type: 'value' }, series: [{ type: 'line', smooth: true, data: (this.summary.trainingTrend || []).map(item => item.value), areaStyle: {} }] })
    }
  }
}
</script>

<style scoped>
.dashboard-page { padding: 20px; background: #f5f7fb; min-height: calc(100vh - 84px); }
.stat-row, .chart-row { margin-bottom: 16px; }
.stat-value { font-size: 28px; font-weight: 600; color: #1f2d3d; }
.stat-label { margin-top: 8px; color: #606266; }
.chart { height: 320px; }
</style>
