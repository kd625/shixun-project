<template>
  <div class="portal-page">
    <div class="portal-hero"><div><h2>数据大屏展示</h2><p>展示资源汇聚、设备在线、共享开放和实训成效。</p></div></div>
    <el-row :gutter="16" class="filter-card">
      <el-col :span="6" v-for="item in statCards" :key="item.label"><el-card shadow="never"><div class="stat-value">{{ item.value }}</div><div class="stat-label">{{ item.label }}</div></el-card></el-col>
    </el-row>
    <el-row :gutter="16"><el-col :span="12"><el-card><div ref="leftChart" class="chart"></div></el-card></el-col><el-col :span="12"><el-card><div ref="rightChart" class="chart"></div></el-card></el-col></el-row>
  </div>
</template>
<script>
import * as echarts from 'echarts'
import { getPortalScreen } from '@/api/portal'
export default {
  name: 'PortalScreen',
  data() { return { summary: {}, charts: [] } },
  computed: { statCards() { return [{ label: '实验室', value: this.summary.labCount || 0 }, { label: '资源', value: this.summary.resourceCount || 0 }, { label: '设备', value: this.summary.deviceCount || 0 }, { label: '参与人数', value: this.summary.participantCount || 0 }] } },
  mounted() { getPortalScreen().then(res => { this.summary = res.data || {}; this.$nextTick(this.renderCharts) }) },
  beforeDestroy() { this.charts.forEach(chart => chart && chart.dispose()) },
  methods: { renderCharts() { const left = echarts.init(this.$refs.leftChart); const right = echarts.init(this.$refs.rightChart); this.charts = [left, right]; left.setOption({ title: { text: '资源类型占比' }, tooltip: {}, series: [{ type: 'pie', radius: '60%', data: this.summary.resourceTypeStats || [] }] }); right.setOption({ title: { text: '近 7 天实训趋势' }, tooltip: {}, xAxis: { type: 'category', data: (this.summary.trainingTrend || []).map(item => item.name) }, yAxis: { type: 'value' }, series: [{ type: 'line', smooth: true, data: (this.summary.trainingTrend || []).map(item => item.value) }] }) } }
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
