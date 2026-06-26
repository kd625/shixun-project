# 虚拟仿真实训教学管理及资源共享云平台演示说明

## 初始化

1. 创建 MySQL 数据库 `ry-vue`。
2. 导入 `kevin-server/sql/ry_20260417.sql`。
3. 导入 `kevin-server/sql/quartz.sql`。
4. 启动 Redis，默认地址为 `localhost:6379`。
5. 确认后端数据库配置：`kevin-server/ruoyi-admin/src/main/resources/application-druid.yml`。
6. 在 `kevin-server/` 执行 `mvn -pl ruoyi-admin spring-boot:run` 启动后端。
7. 在 `kevin-web/` 执行 `npm run dev` 启动前端。

本地 Docker MySQL 建议用 UTF-8 客户端字符集导入，避免中文初始化数据被写成 `è‹¥ä¾` 这类乱码：

```bash
docker exec shixun-mysql mysql --default-character-set=utf8mb4 -uroot -ppassword -e "CREATE DATABASE IF NOT EXISTS \`ry-vue\` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
docker exec -i shixun-mysql mysql --default-character-set=utf8mb4 -uroot -ppassword ry-vue < kevin-server/sql/ry_20260417.sql
docker exec -i shixun-mysql mysql --default-character-set=utf8mb4 -uroot -ppassword ry-vue < kevin-server/sql/quartz.sql
```

## 演示路径

1. 登录系统，查看首页平台指标。
2. 打开前台门户：平台首页、新闻公告、资源中心、实训实验、实验室、共享开放、数据大屏。
3. 在共享开放页面提交一条申请。
4. 进入后台资源管理，查看并审核共享申请。
5. 进入实训管理，查看课程、实验、教学计划和过程结果。
6. 打开数据概览、监控管理和效能管理，展示统计图表。
7. 展示系统管理、日志和服务监控，说明平台具备权限和审计基础。

## 默认环境

- 后端地址：`http://localhost:8080`
- 前端地址：以 `npm run dev` 输出为准，通常为 `http://localhost:80`
- 数据库：`ry-vue`
- Redis：`localhost:6379`
- 默认账号：沿用 RuoYi 初始化脚本中的 `admin/admin123`

## 关键业务数据

- 业务表：`vt_lab`、`vt_device`、`vt_resource`、`vt_share_apply`、`vt_course`、`vt_experiment`、`vt_teaching_plan`、`vt_training_record`。
- 后台接口：`/virtual/lab`、`/virtual/device`、`/virtual/resource`、`/virtual/shareApply`、`/virtual/course`、`/virtual/experiment`、`/virtual/plan`、`/virtual/record`。
- 门户接口：`/portal/home`、`/portal/news`、`/portal/resources`、`/portal/labs`、`/portal/experiments`、`/portal/share`、`/portal/screen`。
- 统计接口：`/virtual/dashboard/summary`。
