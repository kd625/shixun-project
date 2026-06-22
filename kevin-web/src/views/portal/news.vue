<template>
  <div class="portal-page">
    <div class="portal-hero"><div><h2>新闻公告</h2><p>查看平台公告、新闻资讯和开放通知。</p></div><el-button icon="el-icon-refresh" @click="loadData">刷新</el-button></div>
    <el-card shadow="never">
      <el-table :data="news" v-loading="loading">
        <el-table-column label="标题" prop="noticeTitle" :show-overflow-tooltip="true" />
        <el-table-column label="类型" prop="noticeType" width="100" />
        <el-table-column label="发布时间" prop="createTime" width="170"><template slot-scope="scope">{{ parseTime(scope.row.createTime) }}</template></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script>
import { listPortalNews } from '@/api/portal'
export default { name: 'PortalNews', data() { return { loading: true, news: [] } }, created() { this.loadData() }, methods: { loadData() { this.loading = true; listPortalNews({}).then(res => { this.news = res.data || []; this.loading = false }) } } }
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
