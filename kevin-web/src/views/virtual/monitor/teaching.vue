<template>
  <div class="analysis-page">
    <el-card shadow="never" class="header-card"><h3>教学监控</h3><p>查看参与人数和近 7 天实训趋势。</p></el-card>
    <el-row :gutter="16">
      <el-col :xs="24" :md="12"><el-card shadow="never"><el-table :data="rows" size="small"><el-table-column label="指标" prop="name" /><el-table-column label="数值" prop="value" align="right" /></el-table></el-card></el-col>
      <el-col :xs="24" :md="12"><el-card shadow="never"><div ref="chart" class="chart"></div></el-card></el-col>
    </el-row>
  </div>
</template>
<script>
import * as echarts from 'echarts'
import { getDashboardSummary } from '@/api/virtual/dashboard'
export default {
  name: '教学监控',
  data() { return { summary: {}, chart: null } },
  computed: { rows() { return [{ name: '参与人数', value: this.summary.participantCount || 0 }, { name: '完成率', value: (this.summary.completionRate || 0) + '%' }] } },
  mounted() { getDashboardSummary().then(res => { this.summary = res.data || {}; this.$nextTick(this.renderChart) }) },
  beforeDestroy() { if (this.chart) this.chart.dispose() },
  methods: { renderChart() { this.chart = echarts.init(this.$refs.chart); this.chart.setOption({ title: { text: '近 7 天实训趋势' }, tooltip: {}, xAxis: { type: 'category', data: (this.summary.trainingTrend || []).map(item => item.name) }, yAxis: { type: 'value' }, series: [{ type: 'line', smooth: true, data: (this.summary.trainingTrend || []).map(item => item.value), areaStyle: {} }] }) } }
}
</script>
<style scoped>
.analysis-page { padding: 20px; background: #f5f7fb; min-height: calc(100vh - 84px); }
.header-card { margin-bottom: 16px; }
.header-card h3 { margin: 0 0 8px; color: #1f2d3d; }
.header-card p { margin: 0; color: #606266; }
.chart { height: 320px; }
</style>
