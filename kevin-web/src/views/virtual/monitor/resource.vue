<template>
  <div class="analysis-page">
    <el-card shadow="never" class="header-card"><h3>资源监控</h3><p>查看资源总量和资源类型分布。</p></el-card>
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
  name: '资源监控',
  data() { return { summary: {}, chart: null } },
  computed: { rows() { return [{ name: '资源总数', value: this.summary.resourceCount || 0 }, { name: '资源类型', value: (this.summary.resourceTypeStats || []).length + ' 类' }] } },
  mounted() { getDashboardSummary().then(res => { this.summary = res.data || {}; this.$nextTick(this.renderChart) }) },
  beforeDestroy() { if (this.chart) this.chart.dispose() },
  methods: { renderChart() { this.chart = echarts.init(this.$refs.chart); this.chart.setOption({ title: { text: '资源类型占比' }, tooltip: {}, series: [{ type: 'pie', radius: '60%', data: this.summary.resourceTypeStats || [] }] }) } }
}
</script>
<style scoped>
.analysis-page { padding: 20px; background: #f5f7fb; min-height: calc(100vh - 84px); }
.header-card { margin-bottom: 16px; }
.header-card h3 { margin: 0 0 8px; color: #1f2d3d; }
.header-card p { margin: 0; color: #606266; }
.chart { height: 320px; }
</style>
