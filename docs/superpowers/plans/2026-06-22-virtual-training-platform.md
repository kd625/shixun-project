# Virtual Training Platform Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a demonstrable virtual simulation training management and resource sharing platform on the existing RuoYi Spring Boot + Vue 2 codebase.

**Architecture:** Keep RuoYi authentication, permissions, menu loading, logging, pagination, and Excel export unchanged. Add a `virtual` business area in the backend with 8 CRUD resources plus dashboard and portal read APIs, and add Vue pages that follow the existing Element UI list, dialog, and table conventions. Append all schema, menu, dictionary, and demo data into `kevin-server/sql/ry_20260417.sql` so a fresh database can be initialized from the standard SQL files.

**Tech Stack:** Spring Boot 2.5, MyBatis XML, RuoYi common APIs, MySQL, Redis, Vue 2, Vuex, Vue Router, Element UI, ECharts.

---

## File Structure

### Backend Files

- Modify: `kevin-server/sql/ry_20260417.sql`  
  Add `vt_*` business tables, dictionary values, menu permissions, and demo rows.
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain/VtLab.java`
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain/VtDevice.java`
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain/VtResource.java`
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain/VtShareApply.java`
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain/VtCourse.java`
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain/VtExperiment.java`
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain/VtTeachingPlan.java`
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain/VtTrainingRecord.java`
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain/VtDashboardSummary.java`
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/mapper/virtualmapper/*.java`
- Create: `kevin-server/ruoyi-system/src/main/resources/mapper/virtual/*.xml`
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/*.java`
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/impl/*.java`
- Create: `kevin-server/ruoyi-admin/src/main/java/com/ruoyi/web/controller/virtual/*.java`
- Create: `kevin-server/ruoyi-admin/src/test/java/com/ruoyi/web/controller/virtual/VirtualControllerSmokeTest.java`

### Frontend Files

- Modify: `kevin-web/src/router/index.js`
- Modify: `kevin-web/src/views/index.vue`
- Create: `kevin-web/src/api/virtual/lab.js`
- Create: `kevin-web/src/api/virtual/device.js`
- Create: `kevin-web/src/api/virtual/resource.js`
- Create: `kevin-web/src/api/virtual/shareApply.js`
- Create: `kevin-web/src/api/virtual/course.js`
- Create: `kevin-web/src/api/virtual/experiment.js`
- Create: `kevin-web/src/api/virtual/plan.js`
- Create: `kevin-web/src/api/virtual/record.js`
- Create: `kevin-web/src/api/virtual/dashboard.js`
- Create: `kevin-web/src/api/portal/index.js`
- Create: `kevin-web/src/views/virtual/lab/index.vue`
- Create: `kevin-web/src/views/virtual/device/index.vue`
- Create: `kevin-web/src/views/virtual/resource/index.vue`
- Create: `kevin-web/src/views/virtual/shareApply/index.vue`
- Create: `kevin-web/src/views/virtual/course/index.vue`
- Create: `kevin-web/src/views/virtual/experiment/index.vue`
- Create: `kevin-web/src/views/virtual/plan/index.vue`
- Create: `kevin-web/src/views/virtual/record/index.vue`
- Create: `kevin-web/src/views/virtual/dashboard/index.vue`
- Create: `kevin-web/src/views/virtual/monitor/device.vue`
- Create: `kevin-web/src/views/virtual/monitor/resource.vue`
- Create: `kevin-web/src/views/virtual/monitor/teaching.vue`
- Create: `kevin-web/src/views/virtual/effect/resource.vue`
- Create: `kevin-web/src/views/virtual/effect/experiment.vue`
- Create: `kevin-web/src/views/virtual/effect/share.vue`
- Create: `kevin-web/src/views/portal/home.vue`
- Create: `kevin-web/src/views/portal/news.vue`
- Create: `kevin-web/src/views/portal/resources.vue`
- Create: `kevin-web/src/views/portal/experiments.vue`
- Create: `kevin-web/src/views/portal/labs.vue`
- Create: `kevin-web/src/views/portal/share.vue`
- Create: `kevin-web/src/views/portal/screen.vue`

---

## Task 1: SQL Schema, Dictionaries, Menus, and Demo Data

**Files:**
- Modify: `kevin-server/sql/ry_20260417.sql`

- [ ] **Step 1: Append business table DDL after the existing system tables**

Add this SQL block near the end of `kevin-server/sql/ry_20260417.sql`, before final initialization comments if present:

```sql
-- ----------------------------
-- 虚拟仿真实训业务表
-- ----------------------------
drop table if exists vt_lab;
create table vt_lab (
  lab_id bigint(20) not null auto_increment comment '实验室ID',
  lab_name varchar(100) not null comment '实验室名称',
  college_name varchar(100) default '' comment '所属院校或院系',
  location varchar(200) default '' comment '地点',
  capacity int(11) default 0 comment '容量',
  open_status char(1) default '0' comment '开放状态（0开放 1关闭 2维护）',
  manager_name varchar(50) default '' comment '负责人',
  contact_phone varchar(30) default '' comment '联系方式',
  introduction varchar(1000) default '' comment '简介',
  create_by varchar(64) default '',
  create_time datetime,
  update_by varchar(64) default '',
  update_time datetime,
  remark varchar(500) default '',
  primary key (lab_id)
) engine=innodb auto_increment=100 comment='虚拟仿真实验室';

drop table if exists vt_device;
create table vt_device (
  device_id bigint(20) not null auto_increment comment '设备ID',
  device_name varchar(100) not null comment '设备名称',
  device_code varchar(64) not null comment '设备编号',
  lab_id bigint(20) default null comment '所属实验室ID',
  device_type varchar(50) default '' comment '设备类型',
  run_status char(1) default '0' comment '运行状态（0正常 1维护 2故障）',
  online_status char(1) default '0' comment '在线状态（0在线 1离线）',
  last_check_time datetime comment '最近检测时间',
  create_by varchar(64) default '',
  create_time datetime,
  update_by varchar(64) default '',
  update_time datetime,
  remark varchar(500) default '',
  primary key (device_id),
  unique key uk_vt_device_code (device_code),
  key idx_vt_device_lab (lab_id)
) engine=innodb auto_increment=100 comment='虚拟仿真设备';

drop table if exists vt_resource;
create table vt_resource (
  resource_id bigint(20) not null auto_increment comment '资源ID',
  resource_name varchar(120) not null comment '资源名称',
  resource_type char(1) default '0' comment '资源类型（0虚拟仿真 1视频 2音频 3文档）',
  major_name varchar(100) default '' comment '所属专业',
  course_name varchar(100) default '' comment '适用课程',
  cover_url varchar(500) default '' comment '封面地址',
  file_url varchar(500) default '' comment '附件地址',
  share_status char(1) default '0' comment '共享状态（0开放 1校内 2停用）',
  view_count int(11) default 0 comment '浏览量',
  collect_count int(11) default 0 comment '收藏量',
  introduction varchar(1000) default '' comment '简介',
  create_by varchar(64) default '',
  create_time datetime,
  update_by varchar(64) default '',
  update_time datetime,
  remark varchar(500) default '',
  primary key (resource_id)
) engine=innodb auto_increment=100 comment='虚拟仿真实训资源';

drop table if exists vt_share_apply;
create table vt_share_apply (
  apply_id bigint(20) not null auto_increment comment '申请ID',
  applicant_name varchar(50) not null comment '申请人',
  phone varchar(30) default '' comment '手机号',
  organization varchar(120) default '' comment '单位',
  apply_type char(1) default '0' comment '申请类型（0资源 1实验室）',
  target_id bigint(20) default null comment '关联资源或实验室ID',
  target_name varchar(120) default '' comment '关联对象名称',
  reserve_time datetime comment '预约时间',
  user_count int(11) default 1 comment '使用人数',
  apply_status char(1) default '0' comment '申请状态（0待审核 1通过 2驳回）',
  audit_opinion varchar(500) default '' comment '审核意见',
  create_by varchar(64) default '',
  create_time datetime,
  update_by varchar(64) default '',
  update_time datetime,
  remark varchar(500) default '',
  primary key (apply_id)
) engine=innodb auto_increment=100 comment='共享开放申请';

drop table if exists vt_course;
create table vt_course (
  course_id bigint(20) not null auto_increment comment '课程ID',
  course_name varchar(100) not null comment '课程名称',
  major_direction varchar(100) default '' comment '专业方向',
  class_hours int(11) default 0 comment '课时',
  teacher_name varchar(50) default '' comment '授课教师',
  course_status char(1) default '0' comment '课程状态（0启用 1停用）',
  introduction varchar(1000) default '' comment '课程简介',
  create_by varchar(64) default '',
  create_time datetime,
  update_by varchar(64) default '',
  update_time datetime,
  remark varchar(500) default '',
  primary key (course_id)
) engine=innodb auto_increment=100 comment='实训课程';

drop table if exists vt_experiment;
create table vt_experiment (
  experiment_id bigint(20) not null auto_increment comment '实验ID',
  experiment_name varchar(120) not null comment '实验名称',
  course_id bigint(20) default null comment '课程ID',
  resource_id bigint(20) default null comment '资源ID',
  difficulty char(1) default '1' comment '难度（1初级 2中级 3高级）',
  duration_minutes int(11) default 45 comment '预计时长分钟',
  open_status char(1) default '0' comment '开放状态（0开放 1关闭）',
  introduction varchar(1000) default '' comment '实验简介',
  create_by varchar(64) default '',
  create_time datetime,
  update_by varchar(64) default '',
  update_time datetime,
  remark varchar(500) default '',
  primary key (experiment_id),
  key idx_vt_exp_course (course_id),
  key idx_vt_exp_resource (resource_id)
) engine=innodb auto_increment=100 comment='实训实验';

drop table if exists vt_teaching_plan;
create table vt_teaching_plan (
  plan_id bigint(20) not null auto_increment comment '计划ID',
  plan_name varchar(120) not null comment '计划名称',
  course_id bigint(20) default null comment '课程ID',
  experiment_id bigint(20) default null comment '实验ID',
  class_target varchar(100) default '' comment '班级或对象',
  start_time datetime comment '计划开始时间',
  end_time datetime comment '计划结束时间',
  plan_status char(1) default '0' comment '计划状态（0未开始 1进行中 2已结束）',
  manager_name varchar(50) default '' comment '负责人',
  create_by varchar(64) default '',
  create_time datetime,
  update_by varchar(64) default '',
  update_time datetime,
  remark varchar(500) default '',
  primary key (plan_id),
  key idx_vt_plan_course (course_id),
  key idx_vt_plan_exp (experiment_id)
) engine=innodb auto_increment=100 comment='教学计划';

drop table if exists vt_training_record;
create table vt_training_record (
  record_id bigint(20) not null auto_increment comment '记录ID',
  plan_id bigint(20) default null comment '计划ID',
  student_name varchar(50) not null comment '学员姓名',
  student_no varchar(50) default '' comment '学号或编号',
  start_time datetime comment '开始时间',
  end_time datetime comment '结束时间',
  complete_status char(1) default '0' comment '完成状态（0未完成 1已完成 2异常）',
  score decimal(5,2) default 0 comment '得分',
  duration_minutes int(11) default 0 comment '用时分钟',
  evaluation varchar(500) default '' comment '评价',
  create_by varchar(64) default '',
  create_time datetime,
  update_by varchar(64) default '',
  update_time datetime,
  remark varchar(500) default '',
  primary key (record_id),
  key idx_vt_record_plan (plan_id)
) engine=innodb auto_increment=100 comment='实训过程结果';
```

- [ ] **Step 2: Append dictionary seed rows**

Add these rows after the existing `sys_dict_type` and `sys_dict_data` inserts:

```sql
insert into sys_dict_type values(100, '实验室开放状态', 'vt_open_status', '0', 'admin', sysdate(), '', null, '实验室开放状态');
insert into sys_dict_type values(101, '设备运行状态', 'vt_run_status', '0', 'admin', sysdate(), '', null, '设备运行状态');
insert into sys_dict_type values(102, '设备在线状态', 'vt_online_status', '0', 'admin', sysdate(), '', null, '设备在线状态');
insert into sys_dict_type values(103, '资源类型', 'vt_resource_type', '0', 'admin', sysdate(), '', null, '资源类型');
insert into sys_dict_type values(104, '共享状态', 'vt_share_status', '0', 'admin', sysdate(), '', null, '共享状态');
insert into sys_dict_type values(105, '申请类型', 'vt_apply_type', '0', 'admin', sysdate(), '', null, '申请类型');
insert into sys_dict_type values(106, '申请状态', 'vt_apply_status', '0', 'admin', sysdate(), '', null, '申请状态');
insert into sys_dict_type values(107, '实验难度', 'vt_difficulty', '0', 'admin', sysdate(), '', null, '实验难度');
insert into sys_dict_type values(108, '教学计划状态', 'vt_plan_status', '0', 'admin', sysdate(), '', null, '教学计划状态');
insert into sys_dict_type values(109, '完成状态', 'vt_complete_status', '0', 'admin', sysdate(), '', null, '完成状态');

insert into sys_dict_data values(100, 1, '开放', '0', 'vt_open_status', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '开放');
insert into sys_dict_data values(101, 2, '关闭', '1', 'vt_open_status', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '关闭');
insert into sys_dict_data values(102, 3, '维护', '2', 'vt_open_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '维护');
insert into sys_dict_data values(103, 1, '正常', '0', 'vt_run_status', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '正常');
insert into sys_dict_data values(104, 2, '维护', '1', 'vt_run_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '维护');
insert into sys_dict_data values(105, 3, '故障', '2', 'vt_run_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '故障');
insert into sys_dict_data values(106, 1, '在线', '0', 'vt_online_status', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '在线');
insert into sys_dict_data values(107, 2, '离线', '1', 'vt_online_status', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '离线');
insert into sys_dict_data values(108, 1, '虚拟仿真', '0', 'vt_resource_type', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '虚拟仿真');
insert into sys_dict_data values(109, 2, '视频', '1', 'vt_resource_type', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '视频');
insert into sys_dict_data values(110, 3, '音频', '2', 'vt_resource_type', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '音频');
insert into sys_dict_data values(111, 4, '文档', '3', 'vt_resource_type', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '文档');
insert into sys_dict_data values(112, 1, '开放', '0', 'vt_share_status', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '开放');
insert into sys_dict_data values(113, 2, '校内', '1', 'vt_share_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '校内');
insert into sys_dict_data values(114, 3, '停用', '2', 'vt_share_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '停用');
insert into sys_dict_data values(115, 1, '资源', '0', 'vt_apply_type', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '资源');
insert into sys_dict_data values(116, 2, '实验室', '1', 'vt_apply_type', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '实验室');
insert into sys_dict_data values(117, 1, '待审核', '0', 'vt_apply_status', '', 'warning', 'Y', '0', 'admin', sysdate(), '', null, '待审核');
insert into sys_dict_data values(118, 2, '通过', '1', 'vt_apply_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '通过');
insert into sys_dict_data values(119, 3, '驳回', '2', 'vt_apply_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '驳回');
insert into sys_dict_data values(120, 1, '初级', '1', 'vt_difficulty', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '初级');
insert into sys_dict_data values(121, 2, '中级', '2', 'vt_difficulty', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '中级');
insert into sys_dict_data values(122, 3, '高级', '3', 'vt_difficulty', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '高级');
insert into sys_dict_data values(123, 1, '未开始', '0', 'vt_plan_status', '', 'info', 'Y', '0', 'admin', sysdate(), '', null, '未开始');
insert into sys_dict_data values(124, 2, '进行中', '1', 'vt_plan_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '进行中');
insert into sys_dict_data values(125, 3, '已结束', '2', 'vt_plan_status', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '已结束');
insert into sys_dict_data values(126, 1, '未完成', '0', 'vt_complete_status', '', 'warning', 'Y', '0', 'admin', sysdate(), '', null, '未完成');
insert into sys_dict_data values(127, 2, '已完成', '1', 'vt_complete_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '已完成');
insert into sys_dict_data values(128, 3, '异常', '2', 'vt_complete_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '异常');
```

- [ ] **Step 3: Append menu and permission seed rows**

Add these rows after the existing `sys_menu` inserts:

```sql
insert into sys_menu values('2000', '资源管理', '0', '5', 'virtual-resource', null, '', '', 1, 0, 'M', '0', '0', '', 'education', 'admin', sysdate(), '', null, '虚拟仿真资源管理');
insert into sys_menu values('2001', '实训管理', '0', '6', 'virtual-training', null, '', '', 1, 0, 'M', '0', '0', '', 'skill', 'admin', sysdate(), '', null, '虚拟仿真实训管理');
insert into sys_menu values('2002', '数据概览', '0', '7', 'virtual-dashboard', null, '', '', 1, 0, 'M', '0', '0', '', 'chart', 'admin', sysdate(), '', null, '虚拟仿真数据概览');
insert into sys_menu values('2003', '监控管理', '0', '8', 'virtual-monitor', null, '', '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', sysdate(), '', null, '虚拟仿真监控管理');
insert into sys_menu values('2004', '效能管理', '0', '9', 'virtual-effect', null, '', '', 1, 0, 'M', '0', '0', '', 'dashboard', 'admin', sysdate(), '', null, '虚拟仿真效能管理');

insert into sys_menu values('2010', '实验室', '2000', '1', 'lab', 'virtual/lab/index', '', '', 1, 0, 'C', '0', '0', 'virtual:lab:list', 'education', 'admin', sysdate(), '', null, '实验室菜单');
insert into sys_menu values('2011', '仿真设备', '2000', '2', 'device', 'virtual/device/index', '', '', 1, 0, 'C', '0', '0', 'virtual:device:list', 'monitor', 'admin', sysdate(), '', null, '仿真设备菜单');
insert into sys_menu values('2012', '实训资源', '2000', '3', 'resource', 'virtual/resource/index', '', '', 1, 0, 'C', '0', '0', 'virtual:resource:list', 'documentation', 'admin', sysdate(), '', null, '实训资源菜单');
insert into sys_menu values('2013', '共享申请', '2000', '4', 'shareApply', 'virtual/shareApply/index', '', '', 1, 0, 'C', '0', '0', 'virtual:shareApply:list', 'form', 'admin', sysdate(), '', null, '共享申请菜单');
insert into sys_menu values('2020', '课程管理', '2001', '1', 'course', 'virtual/course/index', '', '', 1, 0, 'C', '0', '0', 'virtual:course:list', 'education', 'admin', sysdate(), '', null, '课程管理菜单');
insert into sys_menu values('2021', '实训实验', '2001', '2', 'experiment', 'virtual/experiment/index', '', '', 1, 0, 'C', '0', '0', 'virtual:experiment:list', 'skill', 'admin', sysdate(), '', null, '实训实验菜单');
insert into sys_menu values('2022', '教学计划', '2001', '3', 'plan', 'virtual/plan/index', '', '', 1, 0, 'C', '0', '0', 'virtual:plan:list', 'date', 'admin', sysdate(), '', null, '教学计划菜单');
insert into sys_menu values('2023', '过程结果', '2001', '4', 'record', 'virtual/record/index', '', '', 1, 0, 'C', '0', '0', 'virtual:record:list', 'clipboard', 'admin', sysdate(), '', null, '过程结果菜单');
insert into sys_menu values('2030', '概览统计', '2002', '1', 'index', 'virtual/dashboard/index', '', '', 1, 0, 'C', '0', '0', 'virtual:dashboard:view', 'chart', 'admin', sysdate(), '', null, '概览统计菜单');
insert into sys_menu values('2031', '实训分析', '2002', '2', 'analysis', 'virtual/dashboard/index', '', '', 1, 0, 'C', '0', '0', 'virtual:dashboard:view', 'data-line', 'admin', sysdate(), '', null, '实训分析菜单');
insert into sys_menu values('2032', '数据导出', '2002', '3', 'export', 'virtual/dashboard/index', '', '', 1, 0, 'C', '0', '0', 'virtual:dashboard:view', 'download', 'admin', sysdate(), '', null, '数据导出菜单');
insert into sys_menu values('2040', '设备监控', '2003', '1', 'device', 'virtual/monitor/device', '', '', 1, 0, 'C', '0', '0', 'virtual:monitor:view', 'monitor', 'admin', sysdate(), '', null, '设备监控菜单');
insert into sys_menu values('2041', '资源监控', '2003', '2', 'resource', 'virtual/monitor/resource', '', '', 1, 0, 'C', '0', '0', 'virtual:monitor:view', 'redis-list', 'admin', sysdate(), '', null, '资源监控菜单');
insert into sys_menu values('2042', '教学监控', '2003', '3', 'teaching', 'virtual/monitor/teaching', '', '', 1, 0, 'C', '0', '0', 'virtual:monitor:view', 'peoples', 'admin', sysdate(), '', null, '教学监控菜单');
insert into sys_menu values('2050', '资源利用率', '2004', '1', 'resource', 'virtual/effect/resource', '', '', 1, 0, 'C', '0', '0', 'virtual:effect:view', 'rate', 'admin', sysdate(), '', null, '资源利用率菜单');
insert into sys_menu values('2051', '实验完成率', '2004', '2', 'experiment', 'virtual/effect/experiment', '', '', 1, 0, 'C', '0', '0', 'virtual:effect:view', 'checkbox', 'admin', sysdate(), '', null, '实验完成率菜单');
insert into sys_menu values('2052', '开放共享成效', '2004', '3', 'share', 'virtual/effect/share', '', '', 1, 0, 'C', '0', '0', 'virtual:effect:view', 'link', 'admin', sysdate(), '', null, '开放共享成效菜单');
```

Use button permissions for every CRUD page. For each page prefix below, insert `query`, `add`, `edit`, `remove`, and `export` rows under that page ID using IDs `2100` through `2139`. The lab rows are:

```sql
insert into sys_menu values('2100', '实验室查询', '2010', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:lab:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2101', '实验室新增', '2010', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:lab:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2102', '实验室修改', '2010', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:lab:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2103', '实验室删除', '2010', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:lab:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2104', '实验室导出', '2010', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:lab:export', '#', 'admin', sysdate(), '', null, '');
```

Add the corresponding five button rows for every mapping in this table. The row order is query, add, edit, remove, export; `order_num` values are `1` through `5`; permission suffixes are `:query`, `:add`, `:edit`, `:remove`, and `:export`.

| IDs | Parent | Label | Permission prefix |
| --- | --- | --- | --- |
| `2105-2109` | `2011` | 仿真设备 | `virtual:device` |
| `2110-2114` | `2012` | 实训资源 | `virtual:resource` |
| `2115-2119` | `2013` | 共享申请 | `virtual:shareApply` |
| `2120-2124` | `2020` | 课程 | `virtual:course` |
| `2125-2129` | `2021` | 实训实验 | `virtual:experiment` |
| `2130-2134` | `2022` | 教学计划 | `virtual:plan` |
| `2135-2139` | `2023` | 过程结果 | `virtual:record` |

- [ ] **Step 4: Append demo data**

Use deterministic demo data with IDs starting at `100`:

```sql
insert into vt_lab values(100, '智能制造虚拟仿真实训室', '智能制造学院', '实训楼A301', 48, '0', '李老师', '13800000001', '面向智能产线、工业机器人和数字孪生课程开放。', 'admin', sysdate(), '', null, '');
insert into vt_lab values(101, '新能源汽车虚拟仿真实训室', '汽车工程学院', '实训楼B205', 36, '0', '王老师', '13800000002', '支持新能源汽车结构认知、故障诊断和维护实训。', 'admin', sysdate(), '', null, '');
insert into vt_lab values(102, '护理急救虚拟仿真实训室', '医护学院', '实训楼C108', 30, '2', '张老师', '13800000003', '支持急救流程、护理操作和应急处置仿真实训。', 'admin', sysdate(), '', null, '');

insert into vt_device values(100, '工业机器人仿真工作站', 'DEV-IR-001', 100, 'VR工作站', '0', '0', sysdate(), 'admin', sysdate(), '', null, '');
insert into vt_device values(101, '数字孪生产线终端', 'DEV-DT-002', 100, '仿真终端', '0', '0', sysdate(), 'admin', sysdate(), '', null, '');
insert into vt_device values(102, '新能源汽车故障诊断台', 'DEV-NEV-003', 101, '仿真台架', '1', '0', sysdate(), 'admin', sysdate(), '', null, '');
insert into vt_device values(103, '急救技能VR训练终端', 'DEV-ER-004', 102, 'VR终端', '2', '1', sysdate(), 'admin', sysdate(), '', null, '');

insert into vt_resource values(100, '工业机器人轨迹规划虚拟仿真', '0', '智能制造', '工业机器人应用', '', '', '0', 238, 42, '通过虚拟仿真完成机器人坐标系、轨迹规划和碰撞检测训练。', 'admin', sysdate(), '', null, '');
insert into vt_resource values(101, '新能源汽车高压安全操作视频', '1', '新能源汽车', '新能源汽车维护', '', '', '0', 184, 30, '演示高压安全防护、断电检测和安全操作流程。', 'admin', sysdate(), '', null, '');
insert into vt_resource values(102, '急救心肺复苏操作音频指导', '2', '护理', '急救护理', '', '', '1', 96, 14, '提供心肺复苏节奏、步骤和注意事项音频指导。', 'admin', sysdate(), '', null, '');
insert into vt_resource values(103, '数字孪生产线实训指导书', '3', '智能制造', '数字孪生技术', '', '', '0', 151, 26, '用于数字孪生产线实训前准备和过程记录。', 'admin', sysdate(), '', null, '');

insert into vt_share_apply values(100, '赵同学', '13900000001', '星河职业技术学院', '0', 100, '工业机器人轨迹规划虚拟仿真', date_add(sysdate(), interval 1 day), 8, '0', '', 'admin', sysdate(), '', null, '');
insert into vt_share_apply values(101, '陈老师', '13900000002', '东湖职业学院', '1', 101, '新能源汽车虚拟仿真实训室', date_add(sysdate(), interval 2 day), 20, '1', '同意开放半天，请按预约时间到场。', 'admin', sysdate(), '', null, '');
insert into vt_share_apply values(102, '周老师', '13900000003', '南城技师学院', '0', 102, '急救心肺复苏操作音频指导', date_add(sysdate(), interval 3 day), 12, '2', '该资源当前仅面向校内开放。', 'admin', sysdate(), '', null, '');

insert into vt_course values(100, '工业机器人应用基础', '智能制造', 64, '李老师', '0', '面向工业机器人基础操作、轨迹规划和工作站调试。', 'admin', sysdate(), '', null, '');
insert into vt_course values(101, '新能源汽车维护实训', '新能源汽车', 48, '王老师', '0', '面向新能源汽车安全、诊断和维护流程训练。', 'admin', sysdate(), '', null, '');
insert into vt_course values(102, '急救护理虚拟实训', '护理', 32, '张老师', '0', '面向护理专业急救流程和操作规范训练。', 'admin', sysdate(), '', null, '');

insert into vt_experiment values(100, '机器人搬运轨迹规划实验', 100, 100, '2', 90, '0', '完成机器人取放、路径规划和仿真验证。', 'admin', sysdate(), '', null, '');
insert into vt_experiment values(101, '新能源汽车高压下电实验', 101, 101, '2', 60, '0', '完成高压下电、安全检测和故障记录。', 'admin', sysdate(), '', null, '');
insert into vt_experiment values(102, '心肺复苏流程训练实验', 102, 102, '1', 45, '1', '通过虚拟场景训练急救判断和心肺复苏流程。', 'admin', sysdate(), '', null, '');

insert into vt_teaching_plan values(100, '2026春季机器人应用实训计划', 100, 100, '智能制造2301班', date_sub(sysdate(), interval 1 day), date_add(sysdate(), interval 6 day), '1', '李老师', 'admin', sysdate(), '', null, '');
insert into vt_teaching_plan values(101, '新能源汽车维护开放实训计划', 101, 101, '新能源汽车2302班', date_add(sysdate(), interval 2 day), date_add(sysdate(), interval 8 day), '0', '王老师', 'admin', sysdate(), '', null, '');
insert into vt_teaching_plan values(102, '急救护理综合训练计划', 102, 102, '护理2303班', date_sub(sysdate(), interval 8 day), date_sub(sysdate(), interval 1 day), '2', '张老师', 'admin', sysdate(), '', null, '');

insert into vt_training_record values(100, 100, '刘一', 'S2026001', date_sub(sysdate(), interval 4 hour), date_sub(sysdate(), interval 2 hour), '1', 92.50, 82, '操作流程完整，路径规划合理。', 'admin', sysdate(), '', null, '');
insert into vt_training_record values(101, 100, '陈二', 'S2026002', date_sub(sysdate(), interval 3 hour), date_sub(sysdate(), interval 1 hour), '1', 88.00, 86, '碰撞检测环节需加强。', 'admin', sysdate(), '', null, '');
insert into vt_training_record values(102, 101, '王三', 'S2026003', null, null, '0', 0, 0, '计划未开始。', 'admin', sysdate(), '', null, '');
insert into vt_training_record values(103, 102, '赵四', 'S2026004', date_sub(sysdate(), interval 6 day), date_sub(sysdate(), interval 6 day), '2', 65.00, 40, '流程中断，需重新训练。', 'admin', sysdate(), '', null, '');
```

- [ ] **Step 5: Validate SQL syntax**

Run:

```bash
cd /Users/Zhuanz/Documents/Develop/javaWork/shixun_project
rg -n "vt_lab|virtual:lab|vt_resource_type" kevin-server/sql/ry_20260417.sql
```

Expected: output includes table DDL, dictionary rows, and menu permission rows.

- [ ] **Step 6: Commit SQL seed work**

```bash
git add kevin-server/sql/ry_20260417.sql
git commit -m "新增虚拟仿真实训业务初始化SQL"
```

---

## Task 2: Backend Domain, Mapper, Service, and CRUD Controllers

**Files:**
- Create: all backend files listed in the Backend Files section except dashboard and portal-specific methods.
- Test: `kevin-server/ruoyi-admin/src/test/java/com/ruoyi/web/controller/virtual/VirtualControllerSmokeTest.java`

- [ ] **Step 1: Create the domain package**

Create directory:

```bash
mkdir -p kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain
```

- [ ] **Step 2: Create `VtLab` domain**

Create `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain/VtLab.java`:

```java
package com.ruoyi.system.domain.virtualdomain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class VtLab extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "实验室ID")
    private Long labId;

    @Excel(name = "实验室名称")
    private String labName;

    @Excel(name = "所属院校或院系")
    private String collegeName;

    @Excel(name = "地点")
    private String location;

    @Excel(name = "容量")
    private Integer capacity;

    @Excel(name = "开放状态", readConverterExp = "0=开放,1=关闭,2=维护")
    private String openStatus;

    @Excel(name = "负责人")
    private String managerName;

    @Excel(name = "联系方式")
    private String contactPhone;

    private String introduction;

    public Long getLabId() { return labId; }
    public void setLabId(Long labId) { this.labId = labId; }

    @NotBlank(message = "实验室名称不能为空")
    @Size(max = 100, message = "实验室名称长度不能超过100个字符")
    public String getLabName() { return labName; }
    public void setLabName(String labName) { this.labName = labName; }

    public String getCollegeName() { return collegeName; }
    public void setCollegeName(String collegeName) { this.collegeName = collegeName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getOpenStatus() { return openStatus; }
    public void setOpenStatus(String openStatus) { this.openStatus = openStatus; }

    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
}
```

- [ ] **Step 3: Create the other domain classes**

Create each class with the exact primary key and field names below. All classes extend `BaseEntity`; all required name fields use `@NotBlank`; all exportable list fields use `@Excel`. Use `java.util.Date` for date fields and `java.math.BigDecimal` for `score`.

| File | Class | ID field | Required field | Other fields |
| --- | --- | --- | --- | --- |
| `VtDevice.java` | `VtDevice` | `deviceId` | `deviceName` | `deviceCode`, `labId`, `deviceType`, `runStatus`, `onlineStatus`, `lastCheckTime` |
| `VtResource.java` | `VtResource` | `resourceId` | `resourceName` | `resourceType`, `majorName`, `courseName`, `coverUrl`, `fileUrl`, `shareStatus`, `viewCount`, `collectCount`, `introduction` |
| `VtShareApply.java` | `VtShareApply` | `applyId` | `applicantName` | `phone`, `organization`, `applyType`, `targetId`, `targetName`, `reserveTime`, `userCount`, `applyStatus`, `auditOpinion` |
| `VtCourse.java` | `VtCourse` | `courseId` | `courseName` | `majorDirection`, `classHours`, `teacherName`, `courseStatus`, `introduction` |
| `VtExperiment.java` | `VtExperiment` | `experimentId` | `experimentName` | `courseId`, `resourceId`, `difficulty`, `durationMinutes`, `openStatus`, `introduction` |
| `VtTeachingPlan.java` | `VtTeachingPlan` | `planId` | `planName` | `courseId`, `experimentId`, `classTarget`, `startTime`, `endTime`, `planStatus`, `managerName` |
| `VtTrainingRecord.java` | `VtTrainingRecord` | `recordId` | `studentName` | `planId`, `studentNo`, `startTime`, `endTime`, `completeStatus`, `score`, `durationMinutes`, `evaluation` |

- [ ] **Step 4: Create mapper interfaces**

For each domain, create a mapper interface under `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/mapper/virtualmapper/`. Example for lab:

```java
package com.ruoyi.system.mapper.virtualmapper;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtLab;

public interface VtLabMapper
{
    public List<VtLab> selectVtLabList(VtLab vtLab);
    public List<VtLab> selectVtLabAll();
    public VtLab selectVtLabById(Long labId);
    public int insertVtLab(VtLab vtLab);
    public int updateVtLab(VtLab vtLab);
    public int deleteVtLabById(Long labId);
    public int deleteVtLabByIds(Long[] labIds);
}
```

Create the interfaces listed below. Each interface must contain the same seven method categories as `VtLabMapper`: list, all, get by id, insert, update, delete by id, and delete by ids.

| Mapper | Entity | ID array parameter |
| --- | --- | --- |
| `VtDeviceMapper` | `VtDevice` | `deviceIds` |
| `VtResourceMapper` | `VtResource` | `resourceIds` |
| `VtShareApplyMapper` | `VtShareApply` | `applyIds` |
| `VtCourseMapper` | `VtCourse` | `courseIds` |
| `VtExperimentMapper` | `VtExperiment` | `experimentIds` |
| `VtTeachingPlanMapper` | `VtTeachingPlan` | `planIds` |
| `VtTrainingRecordMapper` | `VtTrainingRecord` | `recordIds` |

- [ ] **Step 5: Create MyBatis XML files**

Create `kevin-server/ruoyi-system/src/main/resources/mapper/virtual/VtLabMapper.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.ruoyi.system.mapper.virtualmapper.VtLabMapper">
    <resultMap type="VtLab" id="VtLabResult">
        <id property="labId" column="lab_id" />
        <result property="labName" column="lab_name" />
        <result property="collegeName" column="college_name" />
        <result property="location" column="location" />
        <result property="capacity" column="capacity" />
        <result property="openStatus" column="open_status" />
        <result property="managerName" column="manager_name" />
        <result property="contactPhone" column="contact_phone" />
        <result property="introduction" column="introduction" />
        <result property="createBy" column="create_by" />
        <result property="createTime" column="create_time" />
        <result property="updateBy" column="update_by" />
        <result property="updateTime" column="update_time" />
        <result property="remark" column="remark" />
    </resultMap>

    <sql id="selectVtLabVo">
        select lab_id, lab_name, college_name, location, capacity, open_status, manager_name, contact_phone, introduction, create_by, create_time, update_by, update_time, remark
        from vt_lab
    </sql>

    <select id="selectVtLabList" parameterType="VtLab" resultMap="VtLabResult">
        <include refid="selectVtLabVo"/>
        <where>
            <if test="labName != null and labName != ''">and lab_name like concat('%', #{labName}, '%')</if>
            <if test="collegeName != null and collegeName != ''">and college_name like concat('%', #{collegeName}, '%')</if>
            <if test="openStatus != null and openStatus != ''">and open_status = #{openStatus}</if>
        </where>
        order by lab_id desc
    </select>

    <select id="selectVtLabAll" resultMap="VtLabResult">
        <include refid="selectVtLabVo"/>
        order by lab_id desc
    </select>

    <select id="selectVtLabById" parameterType="Long" resultMap="VtLabResult">
        <include refid="selectVtLabVo"/>
        where lab_id = #{labId}
    </select>

    <insert id="insertVtLab" parameterType="VtLab" useGeneratedKeys="true" keyProperty="labId">
        insert into vt_lab(lab_name, college_name, location, capacity, open_status, manager_name, contact_phone, introduction, create_by, create_time, remark)
        values(#{labName}, #{collegeName}, #{location}, #{capacity}, #{openStatus}, #{managerName}, #{contactPhone}, #{introduction}, #{createBy}, sysdate(), #{remark})
    </insert>

    <update id="updateVtLab" parameterType="VtLab">
        update vt_lab
        <set>
            <if test="labName != null and labName != ''">lab_name = #{labName},</if>
            <if test="collegeName != null">college_name = #{collegeName},</if>
            <if test="location != null">location = #{location},</if>
            <if test="capacity != null">capacity = #{capacity},</if>
            <if test="openStatus != null and openStatus != ''">open_status = #{openStatus},</if>
            <if test="managerName != null">manager_name = #{managerName},</if>
            <if test="contactPhone != null">contact_phone = #{contactPhone},</if>
            <if test="introduction != null">introduction = #{introduction},</if>
            <if test="remark != null">remark = #{remark},</if>
            <if test="updateBy != null and updateBy != ''">update_by = #{updateBy},</if>
            update_time = sysdate()
        </set>
        where lab_id = #{labId}
    </update>

    <delete id="deleteVtLabById" parameterType="Long">
        delete from vt_lab where lab_id = #{labId}
    </delete>

    <delete id="deleteVtLabByIds" parameterType="Long">
        delete from vt_lab where lab_id in
        <foreach collection="array" item="labId" open="(" separator="," close=")">
            #{labId}
        </foreach>
    </delete>
</mapper>
```

Create the XML files listed below. Each file must define a result map, `select...Vo` SQL fragment, paged list query, all query, get-by-id query, insert, update, delete-by-id, and delete-by-ids. Match the table columns from Task 1 and use these query filters:

| XML | Filters |
| --- | --- |
| `VtDeviceMapper.xml` | `deviceName`, `deviceCode`, `labId`, `runStatus`, `onlineStatus` |
| `VtResourceMapper.xml` | `resourceName`, `resourceType`, `majorName`, `shareStatus` |
| `VtShareApplyMapper.xml` | `applicantName`, `phone`, `applyType`, `applyStatus` |
| `VtCourseMapper.xml` | `courseName`, `majorDirection`, `courseStatus` |
| `VtExperimentMapper.xml` | `experimentName`, `courseId`, `difficulty`, `openStatus` |
| `VtTeachingPlanMapper.xml` | `planName`, `courseId`, `planStatus`, `managerName` |
| `VtTrainingRecordMapper.xml` | `studentName`, `studentNo`, `planId`, `completeStatus` |

- [ ] **Step 6: Create service interfaces and implementations**

Create service interface and implementation for each mapper. Example lab interface:

```java
package com.ruoyi.system.service.virtualservice;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtLab;

public interface IVtLabService
{
    public List<VtLab> selectVtLabList(VtLab vtLab);
    public List<VtLab> selectVtLabAll();
    public VtLab selectVtLabById(Long labId);
    public int insertVtLab(VtLab vtLab);
    public int updateVtLab(VtLab vtLab);
    public int deleteVtLabByIds(Long[] labIds);
}
```

Example lab implementation:

```java
package com.ruoyi.system.service.virtualservice.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.virtualdomain.VtLab;
import com.ruoyi.system.mapper.virtualmapper.VtLabMapper;
import com.ruoyi.system.service.virtualservice.IVtLabService;

@Service
public class VtLabServiceImpl implements IVtLabService
{
    @Autowired
    private VtLabMapper vtLabMapper;

    @Override
    public List<VtLab> selectVtLabList(VtLab vtLab)
    {
        return vtLabMapper.selectVtLabList(vtLab);
    }

    @Override
    public List<VtLab> selectVtLabAll()
    {
        return vtLabMapper.selectVtLabAll();
    }

    @Override
    public VtLab selectVtLabById(Long labId)
    {
        return vtLabMapper.selectVtLabById(labId);
    }

    @Override
    public int insertVtLab(VtLab vtLab)
    {
        return vtLabMapper.insertVtLab(vtLab);
    }

    @Override
    public int updateVtLab(VtLab vtLab)
    {
        return vtLabMapper.updateVtLab(vtLab);
    }

    @Override
    public int deleteVtLabByIds(Long[] labIds)
    {
        return vtLabMapper.deleteVtLabByIds(labIds);
    }
}
```

- [ ] **Step 7: Create CRUD controllers**

Create `kevin-server/ruoyi-admin/src/main/java/com/ruoyi/web/controller/virtual/VtLabController.java`:

```java
package com.ruoyi.web.controller.virtual;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.virtualdomain.VtLab;
import com.ruoyi.system.service.virtualservice.IVtLabService;

@RestController
@RequestMapping("/virtual/lab")
public class VtLabController extends BaseController
{
    @Autowired
    private IVtLabService vtLabService;

    @PreAuthorize("@ss.hasPermi('virtual:lab:list')")
    @GetMapping("/list")
    public TableDataInfo list(VtLab vtLab)
    {
        startPage();
        List<VtLab> list = vtLabService.selectVtLabList(vtLab);
        return getDataTable(list);
    }

    @Log(title = "实验室", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('virtual:lab:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, VtLab vtLab)
    {
        List<VtLab> list = vtLabService.selectVtLabList(vtLab);
        ExcelUtil<VtLab> util = new ExcelUtil<VtLab>(VtLab.class);
        util.exportExcel(response, list, "实验室数据");
    }

    @PreAuthorize("@ss.hasPermi('virtual:lab:query')")
    @GetMapping(value = "/{labId}")
    public AjaxResult getInfo(@PathVariable Long labId)
    {
        return success(vtLabService.selectVtLabById(labId));
    }

    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        return success(vtLabService.selectVtLabAll());
    }

    @PreAuthorize("@ss.hasPermi('virtual:lab:add')")
    @Log(title = "实验室", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody VtLab vtLab)
    {
        vtLab.setCreateBy(getUsername());
        return toAjax(vtLabService.insertVtLab(vtLab));
    }

    @PreAuthorize("@ss.hasPermi('virtual:lab:edit')")
    @Log(title = "实验室", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody VtLab vtLab)
    {
        vtLab.setUpdateBy(getUsername());
        return toAjax(vtLabService.updateVtLab(vtLab));
    }

    @PreAuthorize("@ss.hasPermi('virtual:lab:remove')")
    @Log(title = "实验室", businessType = BusinessType.DELETE)
    @DeleteMapping("/{labIds}")
    public AjaxResult remove(@PathVariable Long[] labIds)
    {
        return toAjax(vtLabService.deleteVtLabByIds(labIds));
    }
}
```

Create the other seven controllers with the same endpoint set as `VtLabController`: `/list`, `/export`, `/{id}`, `/optionselect` when that entity is used by a selector, `POST`, `PUT`, and `DELETE /{ids}`. Use these class names, service types, endpoint bases, and permission prefixes:

| Controller | Service | Base path | Permission prefix | Selector endpoint |
| --- | --- | --- | --- | --- |
| `VtDeviceController` | `IVtDeviceService` | `/virtual/device` | `virtual:device` | no |
| `VtResourceController` | `IVtResourceService` | `/virtual/resource` | `virtual:resource` | no |
| `VtShareApplyController` | `IVtShareApplyService` | `/virtual/shareApply` | `virtual:shareApply` | no |
| `VtCourseController` | `IVtCourseService` | `/virtual/course` | `virtual:course` | yes |
| `VtExperimentController` | `IVtExperimentService` | `/virtual/experiment` | `virtual:experiment` | yes |
| `VtTeachingPlanController` | `IVtTeachingPlanService` | `/virtual/plan` | `virtual:plan` | yes |
| `VtTrainingRecordController` | `IVtTrainingRecordService` | `/virtual/record` | `virtual:record` | no |

- [ ] **Step 8: Add backend smoke test**

Create `kevin-server/ruoyi-admin/src/test/java/com/ruoyi/web/controller/virtual/VirtualControllerSmokeTest.java`:

```java
package com.ruoyi.web.controller.virtual;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class VirtualControllerSmokeTest
{
    @Test
    public void controllersCanBeConstructed()
    {
        assertNotNull(new VtLabController());
        assertNotNull(new VtDeviceController());
        assertNotNull(new VtResourceController());
        assertNotNull(new VtShareApplyController());
        assertNotNull(new VtCourseController());
        assertNotNull(new VtExperimentController());
        assertNotNull(new VtTeachingPlanController());
        assertNotNull(new VtTrainingRecordController());
    }
}
```

- [ ] **Step 9: Run backend test**

Run:

```bash
cd /Users/Zhuanz/Documents/Develop/javaWork/shixun_project/kevin-server
mvn -pl ruoyi-admin -am test -Dtest=VirtualControllerSmokeTest
```

Expected: `BUILD SUCCESS` and a test summary containing `Tests run: 1`.

- [ ] **Step 10: Commit backend CRUD**

```bash
git add kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain \
  kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/mapper/virtualmapper \
  kevin-server/ruoyi-system/src/main/resources/mapper/virtual \
  kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice \
  kevin-server/ruoyi-admin/src/main/java/com/ruoyi/web/controller/virtual \
  kevin-server/ruoyi-admin/src/test/java/com/ruoyi/web/controller/virtual/VirtualControllerSmokeTest.java
git commit -m "新增虚拟仿真实训后台CRUD接口"
```

---

## Task 3: Dashboard and Portal Backend APIs

**Files:**
- Modify: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain/VtDashboardSummary.java`
- Modify: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/mapper/virtualmapper/VtDashboardMapper.java`
- Modify: `kevin-server/ruoyi-system/src/main/resources/mapper/virtual/VtDashboardMapper.xml`
- Modify: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/IVtDashboardService.java`
- Modify: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/impl/VtDashboardServiceImpl.java`
- Create: `kevin-server/ruoyi-admin/src/main/java/com/ruoyi/web/controller/virtual/VtDashboardController.java`
- Create: `kevin-server/ruoyi-admin/src/main/java/com/ruoyi/web/controller/virtual/VtPortalController.java`

- [ ] **Step 1: Create summary DTO**

Create `VtDashboardSummary.java`:

```java
package com.ruoyi.system.domain.virtualdomain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class VtDashboardSummary implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer labCount;
    private Integer resourceCount;
    private Integer deviceCount;
    private Integer participantCount;
    private BigDecimal completionRate;
    private BigDecimal averageScore;
    private List<Map<String, Object>> resourceTypeStats;
    private List<Map<String, Object>> deviceStatusStats;
    private List<Map<String, Object>> applyStatusStats;
    private List<Map<String, Object>> trainingTrend;

    public Integer getLabCount() { return labCount; }
    public void setLabCount(Integer labCount) { this.labCount = labCount; }
    public Integer getResourceCount() { return resourceCount; }
    public void setResourceCount(Integer resourceCount) { this.resourceCount = resourceCount; }
    public Integer getDeviceCount() { return deviceCount; }
    public void setDeviceCount(Integer deviceCount) { this.deviceCount = deviceCount; }
    public Integer getParticipantCount() { return participantCount; }
    public void setParticipantCount(Integer participantCount) { this.participantCount = participantCount; }
    public BigDecimal getCompletionRate() { return completionRate; }
    public void setCompletionRate(BigDecimal completionRate) { this.completionRate = completionRate; }
    public BigDecimal getAverageScore() { return averageScore; }
    public void setAverageScore(BigDecimal averageScore) { this.averageScore = averageScore; }
    public List<Map<String, Object>> getResourceTypeStats() { return resourceTypeStats; }
    public void setResourceTypeStats(List<Map<String, Object>> resourceTypeStats) { this.resourceTypeStats = resourceTypeStats; }
    public List<Map<String, Object>> getDeviceStatusStats() { return deviceStatusStats; }
    public void setDeviceStatusStats(List<Map<String, Object>> deviceStatusStats) { this.deviceStatusStats = deviceStatusStats; }
    public List<Map<String, Object>> getApplyStatusStats() { return applyStatusStats; }
    public void setApplyStatusStats(List<Map<String, Object>> applyStatusStats) { this.applyStatusStats = applyStatusStats; }
    public List<Map<String, Object>> getTrainingTrend() { return trainingTrend; }
    public void setTrainingTrend(List<Map<String, Object>> trainingTrend) { this.trainingTrend = trainingTrend; }
}
```

- [ ] **Step 2: Create dashboard mapper**

Create `VtDashboardMapper.java`:

```java
package com.ruoyi.system.mapper.virtualmapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface VtDashboardMapper
{
    public Integer countLabs();
    public Integer countResources();
    public Integer countDevices();
    public Integer countParticipants();
    public BigDecimal selectCompletionRate();
    public BigDecimal selectAverageScore();
    public List<Map<String, Object>> selectResourceTypeStats();
    public List<Map<String, Object>> selectDeviceStatusStats();
    public List<Map<String, Object>> selectApplyStatusStats();
    public List<Map<String, Object>> selectTrainingTrend();
}
```

Create `VtDashboardMapper.xml` with count and group queries:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.ruoyi.system.mapper.virtualmapper.VtDashboardMapper">
    <select id="countLabs" resultType="Integer">select count(*) from vt_lab</select>
    <select id="countResources" resultType="Integer">select count(*) from vt_resource</select>
    <select id="countDevices" resultType="Integer">select count(*) from vt_device</select>
    <select id="countParticipants" resultType="Integer">select count(distinct student_no) from vt_training_record where student_no is not null and student_no != ''</select>
    <select id="selectCompletionRate" resultType="java.math.BigDecimal">
        select ifnull(round(sum(case when complete_status = '1' then 1 else 0 end) * 100 / nullif(count(*), 0), 2), 0) from vt_training_record
    </select>
    <select id="selectAverageScore" resultType="java.math.BigDecimal">
        select ifnull(round(avg(score), 2), 0) from vt_training_record where complete_status = '1'
    </select>
    <select id="selectResourceTypeStats" resultType="java.util.HashMap">
        select resource_type as name, count(*) as value from vt_resource group by resource_type order by resource_type
    </select>
    <select id="selectDeviceStatusStats" resultType="java.util.HashMap">
        select online_status as name, count(*) as value from vt_device group by online_status order by online_status
    </select>
    <select id="selectApplyStatusStats" resultType="java.util.HashMap">
        select apply_status as name, count(*) as value from vt_share_apply group by apply_status order by apply_status
    </select>
    <select id="selectTrainingTrend" resultType="java.util.HashMap">
        select date_format(create_time, '%m-%d') as name, count(*) as value
        from vt_training_record
        where create_time >= date_sub(curdate(), interval 7 day)
        group by date_format(create_time, '%m-%d')
        order by name
    </select>
</mapper>
```

- [ ] **Step 3: Create dashboard service**

Create `IVtDashboardService.java`:

```java
package com.ruoyi.system.service.virtualservice;

import com.ruoyi.system.domain.virtualdomain.VtDashboardSummary;

public interface IVtDashboardService
{
    public VtDashboardSummary selectSummary();
}
```

Create `VtDashboardServiceImpl.java`:

```java
package com.ruoyi.system.service.virtualservice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.virtualdomain.VtDashboardSummary;
import com.ruoyi.system.mapper.virtualmapper.VtDashboardMapper;
import com.ruoyi.system.service.virtualservice.IVtDashboardService;

@Service
public class VtDashboardServiceImpl implements IVtDashboardService
{
    @Autowired
    private VtDashboardMapper vtDashboardMapper;

    @Override
    public VtDashboardSummary selectSummary()
    {
        VtDashboardSummary summary = new VtDashboardSummary();
        summary.setLabCount(vtDashboardMapper.countLabs());
        summary.setResourceCount(vtDashboardMapper.countResources());
        summary.setDeviceCount(vtDashboardMapper.countDevices());
        summary.setParticipantCount(vtDashboardMapper.countParticipants());
        summary.setCompletionRate(vtDashboardMapper.selectCompletionRate());
        summary.setAverageScore(vtDashboardMapper.selectAverageScore());
        summary.setResourceTypeStats(vtDashboardMapper.selectResourceTypeStats());
        summary.setDeviceStatusStats(vtDashboardMapper.selectDeviceStatusStats());
        summary.setApplyStatusStats(vtDashboardMapper.selectApplyStatusStats());
        summary.setTrainingTrend(vtDashboardMapper.selectTrainingTrend());
        return summary;
    }
}
```

- [ ] **Step 4: Create dashboard and portal controllers**

Create `VtDashboardController.java`:

```java
package com.ruoyi.web.controller.virtual;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.virtualservice.IVtDashboardService;

@RestController
@RequestMapping("/virtual/dashboard")
public class VtDashboardController extends BaseController
{
    @Autowired
    private IVtDashboardService vtDashboardService;

    @PreAuthorize("@ss.hasPermi('virtual:dashboard:view')")
    @GetMapping("/summary")
    public AjaxResult summary()
    {
        return success(vtDashboardService.selectSummary());
    }
}
```

Create `VtPortalController.java` with no permission annotation for public demo reads:

```java
package com.ruoyi.web.controller.virtual;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.virtualdomain.VtLab;
import com.ruoyi.system.domain.virtualdomain.VtResource;
import com.ruoyi.system.domain.virtualdomain.VtShareApply;
import com.ruoyi.system.domain.virtualdomain.VtExperiment;
import com.ruoyi.system.service.virtualservice.IVtDashboardService;
import com.ruoyi.system.service.virtualservice.IVtLabService;
import com.ruoyi.system.service.virtualservice.IVtResourceService;
import com.ruoyi.system.service.virtualservice.IVtShareApplyService;
import com.ruoyi.system.service.virtualservice.IVtExperimentService;

@RestController
@RequestMapping("/portal")
public class VtPortalController extends BaseController
{
    @Autowired private IVtDashboardService vtDashboardService;
    @Autowired private IVtResourceService vtResourceService;
    @Autowired private IVtLabService vtLabService;
    @Autowired private IVtExperimentService vtExperimentService;
    @Autowired private IVtShareApplyService vtShareApplyService;

    @GetMapping("/home")
    public AjaxResult home()
    {
        return success(vtDashboardService.selectSummary());
    }

    @GetMapping("/resources")
    public AjaxResult resources(VtResource query)
    {
        return success(vtResourceService.selectVtResourceList(query));
    }

    @GetMapping("/labs")
    public AjaxResult labs(VtLab query)
    {
        return success(vtLabService.selectVtLabList(query));
    }

    @GetMapping("/experiments")
    public AjaxResult experiments(VtExperiment query)
    {
        return success(vtExperimentService.selectVtExperimentList(query));
    }

    @GetMapping("/screen")
    public AjaxResult screen()
    {
        return success(vtDashboardService.selectSummary());
    }

    @PostMapping("/share")
    public AjaxResult share(@RequestBody VtShareApply apply)
    {
        apply.setApplyStatus("0");
        apply.setCreateBy("portal");
        return toAjax(vtShareApplyService.insertVtShareApply(apply));
    }
}
```

- [ ] **Step 5: Allow public portal URLs through security**

Modify the Spring Security anonymous URL list in `kevin-server/ruoyi-framework/src/main/java/com/ruoyi/framework/config/SecurityConfig.java` by adding:

```java
.antMatchers("/portal/**").permitAll()
```

Place it with the other anonymous static or login-related matchers.

- [ ] **Step 6: Run backend test**

Run:

```bash
cd /Users/Zhuanz/Documents/Develop/javaWork/shixun_project/kevin-server
mvn -pl ruoyi-admin -am test -Dtest=VirtualControllerSmokeTest
```

Expected: `BUILD SUCCESS` and `Tests run: 1`.

- [ ] **Step 7: Commit dashboard and portal APIs**

```bash
git add kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain/VtDashboardSummary.java \
  kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/mapper/virtualmapper/VtDashboardMapper.java \
  kevin-server/ruoyi-system/src/main/resources/mapper/virtual/VtDashboardMapper.xml \
  kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/IVtDashboardService.java \
  kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/impl/VtDashboardServiceImpl.java \
  kevin-server/ruoyi-admin/src/main/java/com/ruoyi/web/controller/virtual/VtDashboardController.java \
  kevin-server/ruoyi-admin/src/main/java/com/ruoyi/web/controller/virtual/VtPortalController.java \
  kevin-server/ruoyi-framework/src/main/java/com/ruoyi/framework/config/SecurityConfig.java
git commit -m "新增虚拟仿真门户和数据概览接口"
```

---

## Task 4: Frontend API Modules and Static Portal Routes

**Files:**
- Modify: `kevin-web/src/router/index.js`
- Create: `kevin-web/src/api/virtual/*.js`
- Create: `kevin-web/src/api/portal/index.js`

- [ ] **Step 1: Create virtual API modules**

Create `kevin-web/src/api/virtual/lab.js`:

```js
import request from '@/utils/request'

export function listLab(query) {
  return request({ url: '/virtual/lab/list', method: 'get', params: query })
}

export function getLab(labId) {
  return request({ url: '/virtual/lab/' + labId, method: 'get' })
}

export function addLab(data) {
  return request({ url: '/virtual/lab', method: 'post', data: data })
}

export function updateLab(data) {
  return request({ url: '/virtual/lab', method: 'put', data: data })
}

export function delLab(labId) {
  return request({ url: '/virtual/lab/' + labId, method: 'delete' })
}

export function optionselectLab() {
  return request({ url: '/virtual/lab/optionselect', method: 'get' })
}
```

Create the API modules listed below. Each module exports list, get, add, update, and delete functions; selector-enabled modules also export `optionselect...`.

| File | Functions | URL base |
| --- | --- | --- |
| `device.js` | `listDevice`, `getDevice`, `addDevice`, `updateDevice`, `delDevice` | `/virtual/device` |
| `resource.js` | `listResource`, `getResource`, `addResource`, `updateResource`, `delResource` | `/virtual/resource` |
| `shareApply.js` | `listShareApply`, `getShareApply`, `addShareApply`, `updateShareApply`, `delShareApply` | `/virtual/shareApply` |
| `course.js` | `listCourse`, `getCourse`, `addCourse`, `updateCourse`, `delCourse`, `optionselectCourse` | `/virtual/course` |
| `experiment.js` | `listExperiment`, `getExperiment`, `addExperiment`, `updateExperiment`, `delExperiment`, `optionselectExperiment` | `/virtual/experiment` |
| `plan.js` | `listPlan`, `getPlan`, `addPlan`, `updatePlan`, `delPlan`, `optionselectPlan` | `/virtual/plan` |
| `record.js` | `listRecord`, `getRecord`, `addRecord`, `updateRecord`, `delRecord` | `/virtual/record` |

- [ ] **Step 2: Create dashboard and portal API modules**

Create `kevin-web/src/api/virtual/dashboard.js`:

```js
import request from '@/utils/request'

export function getDashboardSummary() {
  return request({ url: '/virtual/dashboard/summary', method: 'get' })
}
```

Create `kevin-web/src/api/portal/index.js`:

```js
import request from '@/utils/request'

export function getPortalHome() {
  return request({ url: '/portal/home', method: 'get', headers: { isToken: false } })
}

export function listPortalResources(query) {
  return request({ url: '/portal/resources', method: 'get', params: query, headers: { isToken: false } })
}

export function listPortalLabs(query) {
  return request({ url: '/portal/labs', method: 'get', params: query, headers: { isToken: false } })
}

export function listPortalExperiments(query) {
  return request({ url: '/portal/experiments', method: 'get', params: query, headers: { isToken: false } })
}

export function getPortalScreen() {
  return request({ url: '/portal/screen', method: 'get', headers: { isToken: false } })
}

export function addPortalShare(data) {
  return request({ url: '/portal/share', method: 'post', data: data, headers: { isToken: false } })
}
```

- [ ] **Step 3: Add static portal routes**

Modify `kevin-web/src/router/index.js` and add these entries to `constantRoutes` before the `/404` route:

```js
{
  path: '/portal',
  component: Layout,
  redirect: '/portal/home',
  children: [
    { path: 'home', component: () => import('@/views/portal/home'), name: 'PortalHome', meta: { title: '平台首页', icon: 'dashboard' } },
    { path: 'news', component: () => import('@/views/portal/news'), name: 'PortalNews', meta: { title: '新闻公告', icon: 'message' } },
    { path: 'resources', component: () => import('@/views/portal/resources'), name: 'PortalResources', meta: { title: '资源中心', icon: 'documentation' } },
    { path: 'experiments', component: () => import('@/views/portal/experiments'), name: 'PortalExperiments', meta: { title: '实训实验', icon: 'skill' } },
    { path: 'labs', component: () => import('@/views/portal/labs'), name: 'PortalLabs', meta: { title: '实验室', icon: 'education' } },
    { path: 'share', component: () => import('@/views/portal/share'), name: 'PortalShare', meta: { title: '共享开放', icon: 'link' } },
    { path: 'screen', component: () => import('@/views/portal/screen'), name: 'PortalScreen', meta: { title: '数据大屏', icon: 'chart' } }
  ]
}
```

- [ ] **Step 4: Run frontend syntax check through build**

Run:

```bash
cd /Users/Zhuanz/Documents/Develop/javaWork/shixun_project/kevin-web
npm run build:prod
```

Expected: build reaches compilation. It can fail at this step only because the Vue view files do not exist yet; if it fails for import syntax in API modules or router syntax, fix before continuing.

- [ ] **Step 5: Commit frontend API and routes**

```bash
git add kevin-web/src/api/virtual kevin-web/src/api/portal/index.js kevin-web/src/router/index.js
git commit -m "新增虚拟仿真前端接口和门户路由"
```

---

## Task 5: Frontend CRUD Pages

**Files:**
- Create: `kevin-web/src/views/virtual/*/index.vue`

- [ ] **Step 1: Create lab CRUD page**

Create `kevin-web/src/views/virtual/lab/index.vue` using the existing RuoYi table layout:

```vue
<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="实验室名称" prop="labName">
        <el-input v-model="queryParams.labName" placeholder="请输入实验室名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="所属院校" prop="collegeName">
        <el-input v-model="queryParams.collegeName" placeholder="请输入所属院校" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="开放状态" prop="openStatus">
        <el-select v-model="queryParams.openStatus" placeholder="请选择开放状态" clearable>
          <el-option v-for="dict in dict.type.vt_open_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['virtual:lab:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['virtual:lab:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['virtual:lab:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['virtual:lab:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="labList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="实验室名称" align="center" prop="labName" :show-overflow-tooltip="true" />
      <el-table-column label="所属院校" align="center" prop="collegeName" :show-overflow-tooltip="true" />
      <el-table-column label="地点" align="center" prop="location" :show-overflow-tooltip="true" />
      <el-table-column label="容量" align="center" prop="capacity" width="80" />
      <el-table-column label="开放状态" align="center" prop="openStatus">
        <template slot-scope="scope"><dict-tag :options="dict.type.vt_open_status" :value="scope.row.openStatus" /></template>
      </el-table-column>
      <el-table-column label="负责人" align="center" prop="managerName" width="100" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['virtual:lab:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['virtual:lab:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="实验室名称" prop="labName"><el-input v-model="form.labName" placeholder="请输入实验室名称" /></el-form-item>
        <el-form-item label="所属院校" prop="collegeName"><el-input v-model="form.collegeName" placeholder="请输入所属院校或院系" /></el-form-item>
        <el-form-item label="地点" prop="location"><el-input v-model="form.location" placeholder="请输入地点" /></el-form-item>
        <el-form-item label="容量" prop="capacity"><el-input-number v-model="form.capacity" :min="0" controls-position="right" /></el-form-item>
        <el-form-item label="开放状态" prop="openStatus">
          <el-radio-group v-model="form.openStatus">
            <el-radio v-for="dict in dict.type.vt_open_status" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="负责人" prop="managerName"><el-input v-model="form.managerName" placeholder="请输入负责人" /></el-form-item>
        <el-form-item label="联系方式" prop="contactPhone"><el-input v-model="form.contactPhone" placeholder="请输入联系方式" /></el-form-item>
        <el-form-item label="简介" prop="introduction"><el-input v-model="form.introduction" type="textarea" :rows="4" placeholder="请输入简介" /></el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listLab, getLab, delLab, addLab, updateLab } from '@/api/virtual/lab'

export default {
  name: 'VtLab',
  dicts: ['vt_open_status'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      labList: [],
      title: '',
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, labName: undefined, collegeName: undefined, openStatus: undefined },
      form: {},
      rules: { labName: [{ required: true, message: '实验室名称不能为空', trigger: 'blur' }] }
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      listLab(this.queryParams).then(response => {
        this.labList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() { this.open = false; this.reset() },
    reset() {
      this.form = { labId: undefined, labName: undefined, collegeName: undefined, location: undefined, capacity: 0, openStatus: '0', managerName: undefined, contactPhone: undefined, introduction: undefined }
      this.resetForm('form')
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm('queryForm'); this.handleQuery() },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.labId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() { this.reset(); this.open = true; this.title = '添加实验室' },
    handleUpdate(row) {
      this.reset()
      const labId = row.labId || this.ids
      getLab(labId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改实验室'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const request = this.form.labId !== undefined ? updateLab(this.form) : addLab(this.form)
          request.then(() => {
            this.$modal.msgSuccess(this.form.labId !== undefined ? '修改成功' : '新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const labIds = row.labId || this.ids
      this.$modal.confirm('是否确认删除实验室编号为"' + labIds + '"的数据项？').then(function() {
        return delLab(labIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('virtual/lab/export', { ...this.queryParams }, `lab_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
```

- [ ] **Step 2: Create the other CRUD pages**

Create the other seven pages with the same visible controls as the lab page: search form, toolbar, table, pagination, edit dialog, add, edit, delete, export, and permission directives. Use the API modules from Task 4 and the page-specific lists and dictionary arrays below:

| Page | Data list | ID field | Dicts |
| --- | --- | --- | --- |
| `virtual/device/index.vue` | `deviceList` | `deviceId` | `['vt_run_status', 'vt_online_status']` |
| `virtual/resource/index.vue` | `resourceList` | `resourceId` | `['vt_resource_type', 'vt_share_status']` |
| `virtual/shareApply/index.vue` | `shareApplyList` | `applyId` | `['vt_apply_type', 'vt_apply_status']` |
| `virtual/course/index.vue` | `courseList` | `courseId` | `['sys_normal_disable']` |
| `virtual/experiment/index.vue` | `experimentList` | `experimentId` | `['vt_difficulty', 'vt_open_status']` |
| `virtual/plan/index.vue` | `planList` | `planId` | `['vt_plan_status']` |
| `virtual/record/index.vue` | `recordList` | `recordId` | `['vt_complete_status']` |

Each page must include search, table, add, edit, delete, export, dialog form, and permission directives matching the SQL menu permissions.

- [ ] **Step 3: Run frontend build**

Run:

```bash
cd /Users/Zhuanz/Documents/Develop/javaWork/shixun_project/kevin-web
npm run build:prod
```

Expected: `Build complete` or Vue CLI successful production output.

- [ ] **Step 4: Commit CRUD pages**

```bash
git add kevin-web/src/views/virtual/lab kevin-web/src/views/virtual/device kevin-web/src/views/virtual/resource \
  kevin-web/src/views/virtual/shareApply kevin-web/src/views/virtual/course kevin-web/src/views/virtual/experiment \
  kevin-web/src/views/virtual/plan kevin-web/src/views/virtual/record
git commit -m "新增虚拟仿真实训后台管理页面"
```

---

## Task 6: Portal, Dashboard, Monitor, and Effect Pages

**Files:**
- Modify: `kevin-web/src/views/index.vue`
- Create: portal, dashboard, monitor, and effect files from the Frontend Files section.

- [ ] **Step 1: Replace RuoYi default home page copy**

Modify `kevin-web/src/views/index.vue` to show platform metrics and entry cards instead of the RuoYi introduction. Use `getDashboardSummary` and display lab, resource, device, and participant counts.

- [ ] **Step 2: Create dashboard page**

Create `kevin-web/src/views/virtual/dashboard/index.vue`:

```vue
<template>
  <div class="dashboard-page">
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6" v-for="item in statCards" :key="item.label">
        <el-card shadow="never"><div class="stat-value">{{ item.value }}</div><div class="stat-label">{{ item.label }}</div></el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16">
      <el-col :span="12"><el-card shadow="never"><div ref="resourceChart" class="chart"></div></el-card></el-col>
      <el-col :span="12"><el-card shadow="never"><div ref="deviceChart" class="chart"></div></el-card></el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getDashboardSummary } from '@/api/virtual/dashboard'

export default {
  name: 'VirtualDashboard',
  data() {
    return { summary: {}, resourceChart: null, deviceChart: null }
  },
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
  methods: {
    loadData() {
      getDashboardSummary().then(res => {
        this.summary = res.data || {}
        this.$nextTick(this.renderCharts)
      })
    },
    renderCharts() {
      this.resourceChart = echarts.init(this.$refs.resourceChart)
      this.deviceChart = echarts.init(this.$refs.deviceChart)
      this.resourceChart.setOption({ title: { text: '资源类型占比' }, tooltip: {}, series: [{ type: 'pie', radius: '60%', data: this.summary.resourceTypeStats || [] }] })
      this.deviceChart.setOption({ title: { text: '设备在线状态' }, tooltip: {}, xAxis: { type: 'category', data: (this.summary.deviceStatusStats || []).map(item => item.name) }, yAxis: { type: 'value' }, series: [{ type: 'bar', data: (this.summary.deviceStatusStats || []).map(item => item.value) }] })
    }
  }
}
</script>

<style scoped>
.dashboard-page { padding: 20px; }
.stat-row { margin-bottom: 16px; }
.stat-value { font-size: 28px; font-weight: 600; color: #1f2d3d; }
.stat-label { margin-top: 8px; color: #606266; }
.chart { height: 320px; }
</style>
```

- [ ] **Step 3: Create portal pages**

Create `portal/home.vue`, `portal/resources.vue`, `portal/experiments.vue`, `portal/labs.vue`, `portal/share.vue`, `portal/screen.vue`, and `portal/news.vue`. Each page uses `app-container`, Element UI cards/tables/forms, and the portal API module. `portal/share.vue` must call `addPortalShare` and show success message after submission.

- [ ] **Step 4: Create monitor and effect pages**

Create the six lightweight analysis pages:

- `virtual/monitor/device.vue`: show device status cards and a table using dashboard summary.
- `virtual/monitor/resource.vue`: show resource type and share status cards.
- `virtual/monitor/teaching.vue`: show plan status and completion stats.
- `virtual/effect/resource.vue`: show resource usage numbers from view and collect counts.
- `virtual/effect/experiment.vue`: show completion rate and average score.
- `virtual/effect/share.vue`: show apply status distribution.

Each page imports `getDashboardSummary` and renders at least one chart or table so menu clicks do not lead to blank pages.

- [ ] **Step 5: Run frontend build**

Run:

```bash
cd /Users/Zhuanz/Documents/Develop/javaWork/shixun_project/kevin-web
npm run build:prod
```

Expected: Vue CLI production build succeeds.

- [ ] **Step 6: Commit portal and dashboard pages**

```bash
git add kevin-web/src/views/index.vue kevin-web/src/views/virtual/dashboard kevin-web/src/views/virtual/monitor \
  kevin-web/src/views/virtual/effect kevin-web/src/views/portal
git commit -m "新增虚拟仿真门户和数据大屏页面"
```

---

## Task 7: End-to-End Verification and Demo Notes

**Files:**
- Create: `kevin-doc/demo-guide.md`

- [ ] **Step 1: Run full backend verification**

Run:

```bash
cd /Users/Zhuanz/Documents/Develop/javaWork/shixun_project/kevin-server
mvn test
```

Expected: `BUILD SUCCESS`; if tests are skipped, record that Maven compiled successfully but did not run real tests.

- [ ] **Step 2: Run frontend verification**

Run:

```bash
cd /Users/Zhuanz/Documents/Develop/javaWork/shixun_project/kevin-web
npm run build:prod
```

Expected: production build succeeds.

- [ ] **Step 3: Create demo guide**

Create `kevin-doc/demo-guide.md`:

```markdown
# 虚拟仿真实训教学管理及资源共享云平台演示说明

## 初始化

1. 创建 MySQL 数据库 `ry-vue`。
2. 导入 `kevin-server/sql/ry_20260417.sql`。
3. 导入 `kevin-server/sql/quartz.sql`。
4. 启动 Redis。
5. 启动后端：在 `kevin-server/` 执行 `mvn -pl ruoyi-admin spring-boot:run`。
6. 启动前端：在 `kevin-web/` 执行 `npm run dev`。

## 演示路径

1. 登录系统，查看首页平台指标。
2. 打开前台门户：平台首页、资源中心、实训实验、实验室、共享开放、数据大屏。
3. 在共享开放页面提交一条申请。
4. 进入后台资源管理，查看并审核共享申请。
5. 进入实训管理，查看课程、实验、教学计划和过程结果。
6. 打开数据概览、监控管理和效能管理，展示统计图表。
7. 展示系统管理、日志和服务监控，说明平台具备权限和审计基础。

## 默认环境

- 后端地址：`http://localhost:8080`
- 前端地址：以 `npm run dev` 输出为准
- 数据库：`ry-vue`
- Redis：`localhost:6379`
```

- [ ] **Step 4: Commit demo guide and any verification fixes**

```bash
git add kevin-doc/demo-guide.md
git status --short
git commit -m "补充虚拟仿真平台演示说明"
```

- [ ] **Step 5: Final manual smoke check**

Start backend and frontend, then verify:

```bash
curl -sSf http://127.0.0.1:8080/portal/home
```

Expected: JSON response with code `200` and `data.labCount`.

Open the frontend and verify these routes render:

- `/index`
- `/portal/home`
- `/portal/resources`
- `/portal/share`
- `/portal/screen`
- `/virtual/lab/index`
- `/virtual/dashboard/index`

---

## Self-Review Notes

Spec coverage:

- Resource sharing CRUD is covered by Tasks 1, 2, 4, and 5.
- Teaching training CRUD is covered by Tasks 1, 2, 4, and 5.
- Portal pages are covered by Tasks 3, 4, and 6.
- Data screen, monitoring, and effect pages are covered by Tasks 3 and 6.
- Unified SQL initialization is covered by Task 1.
- Verification and demo documentation are covered by Task 7.

Implementation constraints:

- SQL IDs start at `2000` for menus and `100` for business dictionaries and demo data, avoiding existing RuoYi seed IDs.
- Public portal APIs are read-oriented except `/portal/share`, which creates pending applications.
- Existing RuoYi auth and permission behavior remains unchanged except `/portal/**` anonymous access.
