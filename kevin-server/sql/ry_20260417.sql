SET NAMES utf8mb4;

-- ----------------------------
-- 1、部门表
-- ----------------------------
drop table if exists sys_dept;
create table sys_dept (
  dept_id           bigint(20)      not null auto_increment    comment '部门id',
  parent_id         bigint(20)      default 0                  comment '父部门id',
  ancestors         varchar(50)     default ''                 comment '祖级列表',
  dept_name         varchar(30)     default ''                 comment '部门名称',
  order_num         int(4)          default 0                  comment '显示顺序',
  leader            varchar(20)     default null               comment '负责人',
  phone             varchar(11)     default null               comment '联系电话',
  email             varchar(50)     default null               comment '邮箱',
  status            char(1)         default '0'                comment '部门状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (dept_id)
) engine=innodb auto_increment=200 comment = '部门表';

-- ----------------------------
-- 初始化-部门表数据
-- ----------------------------
insert into sys_dept values(100,  0,   '0',          '若依科技',   0, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(101,  100, '0,100',      '深圳总公司', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(102,  100, '0,100',      '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(103,  101, '0,100,101',  '研发部门',   1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(104,  101, '0,100,101',  '市场部门',   2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(105,  101, '0,100,101',  '测试部门',   3, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(106,  101, '0,100,101',  '财务部门',   4, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(107,  101, '0,100,101',  '运维部门',   5, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(108,  102, '0,100,102',  '市场部门',   1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(109,  102, '0,100,102',  '财务部门',   2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);


-- ----------------------------
-- 2、用户信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user (
  user_id           bigint(20)      not null auto_increment    comment '用户ID',
  dept_id           bigint(20)      default null               comment '部门ID',
  user_name         varchar(30)     not null                   comment '用户账号',
  nick_name         varchar(30)     not null                   comment '用户昵称',
  user_type         varchar(2)      default '00'               comment '用户类型（00系统用户）',
  email             varchar(50)     default ''                 comment '用户邮箱',
  phonenumber       varchar(11)     default ''                 comment '手机号码',
  sex               char(1)         default '0'                comment '用户性别（0男 1女 2未知）',
  avatar            varchar(100)    default ''                 comment '头像地址',
  password          varchar(100)    default ''                 comment '密码',
  status            char(1)         default '0'                comment '账号状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  login_ip          varchar(128)    default ''                 comment '最后登录IP',
  login_date        datetime                                   comment '最后登录时间',
  pwd_update_date   datetime                                   comment '密码最后更新时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (user_id)
) engine=innodb auto_increment=100 comment = '用户信息表';

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
insert into sys_user values(1,  103, 'admin', '若依', '00', 'ry@163.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '管理员');
insert into sys_user values(2,  105, 'ry',    '若依', '00', 'ry@qq.com',  '15666666666', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '测试员');


-- ----------------------------
-- 3、岗位信息表
-- ----------------------------
drop table if exists sys_post;
create table sys_post
(
  post_id       bigint(20)      not null auto_increment    comment '岗位ID',
  post_code     varchar(64)     not null                   comment '岗位编码',
  post_name     varchar(50)     not null                   comment '岗位名称',
  post_sort     int(4)          not null                   comment '显示顺序',
  status        char(1)         not null                   comment '状态（0正常 1停用）',
  create_by     varchar(64)     default ''                 comment '创建者',
  create_time   datetime                                   comment '创建时间',
  update_by     varchar(64)     default ''			       comment '更新者',
  update_time   datetime                                   comment '更新时间',
  remark        varchar(500)    default null               comment '备注',
  primary key (post_id)
) engine=innodb comment = '岗位信息表';

-- ----------------------------
-- 初始化-岗位信息表数据
-- ----------------------------
insert into sys_post values(1, 'ceo',  '董事长',    1, '0', 'admin', sysdate(), '', null, '');
insert into sys_post values(2, 'se',   '项目经理',  2, '0', 'admin', sysdate(), '', null, '');
insert into sys_post values(3, 'hr',   '人力资源',  3, '0', 'admin', sysdate(), '', null, '');
insert into sys_post values(4, 'user', '普通员工',  4, '0', 'admin', sysdate(), '', null, '');


-- ----------------------------
-- 4、角色信息表
-- ----------------------------
drop table if exists sys_role;
create table sys_role (
  role_id              bigint(20)      not null auto_increment    comment '角色ID',
  role_name            varchar(30)     not null                   comment '角色名称',
  role_key             varchar(100)    not null                   comment '角色权限字符串',
  role_sort            int(4)          not null                   comment '显示顺序',
  data_scope           char(1)         default '1'                comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  menu_check_strictly  tinyint(1)      default 1                  comment '菜单树选择项是否关联显示',
  dept_check_strictly  tinyint(1)      default 1                  comment '部门树选择项是否关联显示',
  status               char(1)         not null                   comment '角色状态（0正常 1停用）',
  del_flag             char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by            varchar(64)     default ''                 comment '创建者',
  create_time          datetime                                   comment '创建时间',
  update_by            varchar(64)     default ''                 comment '更新者',
  update_time          datetime                                   comment '更新时间',
  remark               varchar(500)    default null               comment '备注',
  primary key (role_id)
) engine=innodb auto_increment=100 comment = '角色信息表';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
insert into sys_role values('1', '超级管理员',  'admin',  1, 1, 1, 1, '0', '0', 'admin', sysdate(), '', null, '超级管理员');
insert into sys_role values('2', '普通角色',    'common', 2, 2, 1, 1, '0', '0', 'admin', sysdate(), '', null, '普通角色');


-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu (
  menu_id           bigint(20)      not null auto_increment    comment '菜单ID',
  menu_name         varchar(50)     not null                   comment '菜单名称',
  parent_id         bigint(20)      default 0                  comment '父菜单ID',
  order_num         int(4)          default 0                  comment '显示顺序',
  path              varchar(200)    default ''                 comment '路由地址',
  component         varchar(255)    default null               comment '组件路径',
  query             varchar(255)    default null               comment '路由参数',
  route_name        varchar(50)     default ''                 comment '路由名称',
  is_frame          int(1)          default 1                  comment '是否为外链（0是 1否）',
  is_cache          int(1)          default 0                  comment '是否缓存（0缓存 1不缓存）',
  menu_type         char(1)         default ''                 comment '菜单类型（M目录 C菜单 F按钮）',
  visible           char(1)         default 0                  comment '菜单状态（0显示 1隐藏）',
  status            char(1)         default 0                  comment '菜单状态（0正常 1停用）',
  perms             varchar(100)    default null               comment '权限标识',
  icon              varchar(100)    default '#'                comment '菜单图标',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default ''                 comment '备注',
  primary key (menu_id)
) engine=innodb auto_increment=2000 comment = '菜单权限表';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单
insert into sys_menu values('1', '系统管理', '0', '1', 'system',           null, '', '', 1, 0, 'M', '0', '0', '', 'system',   'admin', sysdate(), '', null, '系统管理目录');
insert into sys_menu values('2', '系统监控', '0', '2', 'monitor',          null, '', '', 1, 0, 'M', '0', '0', '', 'monitor',  'admin', sysdate(), '', null, '系统监控目录');
insert into sys_menu values('3', '系统工具', '0', '3', 'tool',             null, '', '', 1, 0, 'M', '0', '0', '', 'tool',     'admin', sysdate(), '', null, '系统工具目录');
insert into sys_menu values('4', '若依官网', '0', '4', 'http://ruoyi.vip', null, '', '', 0, 0, 'M', '0', '0', '', 'guide',    'admin', sysdate(), '', null, '若依官网地址');
-- 二级菜单
insert into sys_menu values('100',  '用户管理', '1',   '1', 'user',       'system/user/index',        '', '', 1, 0, 'C', '0', '0', 'system:user:list',        'user',          'admin', sysdate(), '', null, '用户管理菜单');
insert into sys_menu values('101',  '角色管理', '1',   '2', 'role',       'system/role/index',        '', '', 1, 0, 'C', '0', '0', 'system:role:list',        'peoples',       'admin', sysdate(), '', null, '角色管理菜单');
insert into sys_menu values('102',  '菜单管理', '1',   '3', 'menu',       'system/menu/index',        '', '', 1, 0, 'C', '0', '0', 'system:menu:list',        'tree-table',    'admin', sysdate(), '', null, '菜单管理菜单');
insert into sys_menu values('103',  '部门管理', '1',   '4', 'dept',       'system/dept/index',        '', '', 1, 0, 'C', '0', '0', 'system:dept:list',        'tree',          'admin', sysdate(), '', null, '部门管理菜单');
insert into sys_menu values('104',  '岗位管理', '1',   '5', 'post',       'system/post/index',        '', '', 1, 0, 'C', '0', '0', 'system:post:list',        'post',          'admin', sysdate(), '', null, '岗位管理菜单');
insert into sys_menu values('105',  '字典管理', '1',   '6', 'dict',       'system/dict/index',        '', '', 1, 0, 'C', '0', '0', 'system:dict:list',        'dict',          'admin', sysdate(), '', null, '字典管理菜单');
insert into sys_menu values('106',  '参数设置', '1',   '7', 'config',     'system/config/index',      '', '', 1, 0, 'C', '0', '0', 'system:config:list',      'edit',          'admin', sysdate(), '', null, '参数设置菜单');
insert into sys_menu values('107',  '通知公告', '1',   '8', 'notice',     'system/notice/index',      '', '', 1, 0, 'C', '0', '0', 'system:notice:list',      'message',       'admin', sysdate(), '', null, '通知公告菜单');
insert into sys_menu values('108',  '日志管理', '1',   '9', 'log',        '',                         '', '', 1, 0, 'M', '0', '0', '',                        'log',           'admin', sysdate(), '', null, '日志管理菜单');
insert into sys_menu values('109',  '在线用户', '2',   '1', 'online',     'monitor/online/index',     '', '', 1, 0, 'C', '0', '0', 'monitor:online:list',     'online',        'admin', sysdate(), '', null, '在线用户菜单');
insert into sys_menu values('110',  '定时任务', '2',   '2', 'job',        'monitor/job/index',        '', '', 1, 0, 'C', '0', '0', 'monitor:job:list',        'job',           'admin', sysdate(), '', null, '定时任务菜单');
insert into sys_menu values('111',  '数据监控', '2',   '3', 'druid',      'monitor/druid/index',      '', '', 1, 0, 'C', '0', '0', 'monitor:druid:list',      'druid',         'admin', sysdate(), '', null, '数据监控菜单');
insert into sys_menu values('112',  '服务监控', '2',   '4', 'server',     'monitor/server/index',     '', '', 1, 0, 'C', '0', '0', 'monitor:server:list',     'server',        'admin', sysdate(), '', null, '服务监控菜单');
insert into sys_menu values('113',  '缓存监控', '2',   '5', 'cache',      'monitor/cache/index',      '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list',      'redis',         'admin', sysdate(), '', null, '缓存监控菜单');
insert into sys_menu values('114',  '缓存列表', '2',   '6', 'cacheList',  'monitor/cache/list',       '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list',      'redis-list',    'admin', sysdate(), '', null, '缓存列表菜单');
insert into sys_menu values('115',  '表单构建', '3',   '1', 'build',      'tool/build/index',         '', '', 1, 0, 'C', '0', '0', 'tool:build:list',         'build',         'admin', sysdate(), '', null, '表单构建菜单');
insert into sys_menu values('116',  '代码生成', '3',   '2', 'gen',        'tool/gen/index',           '', '', 1, 0, 'C', '0', '0', 'tool:gen:list',           'code',          'admin', sysdate(), '', null, '代码生成菜单');
insert into sys_menu values('117',  '系统接口', '3',   '3', 'swagger',    'tool/swagger/index',       '', '', 1, 0, 'C', '0', '0', 'tool:swagger:list',       'swagger',       'admin', sysdate(), '', null, '系统接口菜单');
-- 三级菜单
insert into sys_menu values('500',  '操作日志', '108', '1', 'operlog',    'monitor/operlog/index',    '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list',    'form',          'admin', sysdate(), '', null, '操作日志菜单');
insert into sys_menu values('501',  '登录日志', '108', '2', 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor',    'admin', sysdate(), '', null, '登录日志菜单');
-- 用户管理按钮
insert into sys_menu values('1000', '用户查询', '100', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1001', '用户新增', '100', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1002', '用户修改', '100', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1003', '用户删除', '100', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1004', '用户导出', '100', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1005', '用户导入', '100', '6',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1006', '重置密码', '100', '7',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd',       '#', 'admin', sysdate(), '', null, '');
-- 角色管理按钮
insert into sys_menu values('1007', '角色查询', '101', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1008', '角色新增', '101', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1009', '角色修改', '101', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1010', '角色删除', '101', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1011', '角色导出', '101', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export',         '#', 'admin', sysdate(), '', null, '');
-- 菜单管理按钮
insert into sys_menu values('1012', '菜单查询', '102', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1013', '菜单新增', '102', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1014', '菜单修改', '102', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1015', '菜单删除', '102', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove',         '#', 'admin', sysdate(), '', null, '');
-- 部门管理按钮
insert into sys_menu values('1016', '部门查询', '103', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1017', '部门新增', '103', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1018', '部门修改', '103', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1019', '部门删除', '103', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove',         '#', 'admin', sysdate(), '', null, '');
-- 岗位管理按钮
insert into sys_menu values('1020', '岗位查询', '104', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1021', '岗位新增', '104', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1022', '岗位修改', '104', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1023', '岗位删除', '104', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1024', '岗位导出', '104', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export',         '#', 'admin', sysdate(), '', null, '');
-- 字典管理按钮
insert into sys_menu values('1025', '字典查询', '105', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1026', '字典新增', '105', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1027', '字典修改', '105', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1028', '字典删除', '105', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1029', '字典导出', '105', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export',         '#', 'admin', sysdate(), '', null, '');
-- 参数设置按钮
insert into sys_menu values('1030', '参数查询', '106', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1031', '参数新增', '106', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1032', '参数修改', '106', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1033', '参数删除', '106', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1034', '参数导出', '106', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export',       '#', 'admin', sysdate(), '', null, '');
-- 通知公告按钮
insert into sys_menu values('1035', '公告查询', '107', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1036', '公告新增', '107', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1037', '公告修改', '107', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1038', '公告删除', '107', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove',       '#', 'admin', sysdate(), '', null, '');
-- 操作日志按钮
insert into sys_menu values('1039', '操作查询', '500', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query',      '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1040', '操作删除', '500', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove',     '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1041', '日志导出', '500', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export',     '#', 'admin', sysdate(), '', null, '');
-- 登录日志按钮
insert into sys_menu values('1042', '登录查询', '501', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query',   '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1043', '登录删除', '501', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove',  '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1044', '日志导出', '501', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export',  '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1045', '账户解锁', '501', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock',  '#', 'admin', sysdate(), '', null, '');
-- 在线用户按钮
insert into sys_menu values('1046', '在线查询', '109', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1047', '批量强退', '109', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1048', '单条强退', '109', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', sysdate(), '', null, '');
-- 定时任务按钮
insert into sys_menu values('1049', '任务查询', '110', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1050', '任务新增', '110', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1051', '任务修改', '110', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1052', '任务删除', '110', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1053', '状态修改', '110', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus',   '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1054', '任务导出', '110', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export',         '#', 'admin', sysdate(), '', null, '');
-- 代码生成按钮
insert into sys_menu values('1055', '生成查询', '116', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query',             '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1056', '生成修改', '116', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit',              '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1057', '生成删除', '116', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1058', '导入代码', '116', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1059', '预览代码', '116', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1060', '生成代码', '116', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code',              '#', 'admin', sysdate(), '', null, '');


-- ----------------------------
-- 6、用户和角色关联表  用户N-1角色
-- ----------------------------
drop table if exists sys_user_role;
create table sys_user_role (
  user_id   bigint(20) not null comment '用户ID',
  role_id   bigint(20) not null comment '角色ID',
  primary key(user_id, role_id)
) engine=innodb comment = '用户和角色关联表';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
insert into sys_user_role values ('1', '1');
insert into sys_user_role values ('2', '2');


-- ----------------------------
-- 7、角色和菜单关联表  角色1-N菜单
-- ----------------------------
drop table if exists sys_role_menu;
create table sys_role_menu (
  role_id   bigint(20) not null comment '角色ID',
  menu_id   bigint(20) not null comment '菜单ID',
  primary key(role_id, menu_id)
) engine=innodb comment = '角色和菜单关联表';

-- ----------------------------
-- 初始化-角色和菜单关联表数据
-- ----------------------------
insert into sys_role_menu values ('2', '1');
insert into sys_role_menu values ('2', '2');
insert into sys_role_menu values ('2', '3');
insert into sys_role_menu values ('2', '4');
insert into sys_role_menu values ('2', '100');
insert into sys_role_menu values ('2', '101');
insert into sys_role_menu values ('2', '102');
insert into sys_role_menu values ('2', '103');
insert into sys_role_menu values ('2', '104');
insert into sys_role_menu values ('2', '105');
insert into sys_role_menu values ('2', '106');
insert into sys_role_menu values ('2', '107');
insert into sys_role_menu values ('2', '108');
insert into sys_role_menu values ('2', '109');
insert into sys_role_menu values ('2', '110');
insert into sys_role_menu values ('2', '111');
insert into sys_role_menu values ('2', '112');
insert into sys_role_menu values ('2', '113');
insert into sys_role_menu values ('2', '114');
insert into sys_role_menu values ('2', '115');
insert into sys_role_menu values ('2', '116');
insert into sys_role_menu values ('2', '117');
insert into sys_role_menu values ('2', '500');
insert into sys_role_menu values ('2', '501');
insert into sys_role_menu values ('2', '1000');
insert into sys_role_menu values ('2', '1001');
insert into sys_role_menu values ('2', '1002');
insert into sys_role_menu values ('2', '1003');
insert into sys_role_menu values ('2', '1004');
insert into sys_role_menu values ('2', '1005');
insert into sys_role_menu values ('2', '1006');
insert into sys_role_menu values ('2', '1007');
insert into sys_role_menu values ('2', '1008');
insert into sys_role_menu values ('2', '1009');
insert into sys_role_menu values ('2', '1010');
insert into sys_role_menu values ('2', '1011');
insert into sys_role_menu values ('2', '1012');
insert into sys_role_menu values ('2', '1013');
insert into sys_role_menu values ('2', '1014');
insert into sys_role_menu values ('2', '1015');
insert into sys_role_menu values ('2', '1016');
insert into sys_role_menu values ('2', '1017');
insert into sys_role_menu values ('2', '1018');
insert into sys_role_menu values ('2', '1019');
insert into sys_role_menu values ('2', '1020');
insert into sys_role_menu values ('2', '1021');
insert into sys_role_menu values ('2', '1022');
insert into sys_role_menu values ('2', '1023');
insert into sys_role_menu values ('2', '1024');
insert into sys_role_menu values ('2', '1025');
insert into sys_role_menu values ('2', '1026');
insert into sys_role_menu values ('2', '1027');
insert into sys_role_menu values ('2', '1028');
insert into sys_role_menu values ('2', '1029');
insert into sys_role_menu values ('2', '1030');
insert into sys_role_menu values ('2', '1031');
insert into sys_role_menu values ('2', '1032');
insert into sys_role_menu values ('2', '1033');
insert into sys_role_menu values ('2', '1034');
insert into sys_role_menu values ('2', '1035');
insert into sys_role_menu values ('2', '1036');
insert into sys_role_menu values ('2', '1037');
insert into sys_role_menu values ('2', '1038');
insert into sys_role_menu values ('2', '1039');
insert into sys_role_menu values ('2', '1040');
insert into sys_role_menu values ('2', '1041');
insert into sys_role_menu values ('2', '1042');
insert into sys_role_menu values ('2', '1043');
insert into sys_role_menu values ('2', '1044');
insert into sys_role_menu values ('2', '1045');
insert into sys_role_menu values ('2', '1046');
insert into sys_role_menu values ('2', '1047');
insert into sys_role_menu values ('2', '1048');
insert into sys_role_menu values ('2', '1049');
insert into sys_role_menu values ('2', '1050');
insert into sys_role_menu values ('2', '1051');
insert into sys_role_menu values ('2', '1052');
insert into sys_role_menu values ('2', '1053');
insert into sys_role_menu values ('2', '1054');
insert into sys_role_menu values ('2', '1055');
insert into sys_role_menu values ('2', '1056');
insert into sys_role_menu values ('2', '1057');
insert into sys_role_menu values ('2', '1058');
insert into sys_role_menu values ('2', '1059');
insert into sys_role_menu values ('2', '1060');

-- ----------------------------
-- 8、角色和部门关联表  角色1-N部门
-- ----------------------------
drop table if exists sys_role_dept;
create table sys_role_dept (
  role_id   bigint(20) not null comment '角色ID',
  dept_id   bigint(20) not null comment '部门ID',
  primary key(role_id, dept_id)
) engine=innodb comment = '角色和部门关联表';

-- ----------------------------
-- 初始化-角色和部门关联表数据
-- ----------------------------
insert into sys_role_dept values ('2', '100');
insert into sys_role_dept values ('2', '101');
insert into sys_role_dept values ('2', '105');


-- ----------------------------
-- 9、用户与岗位关联表  用户1-N岗位
-- ----------------------------
drop table if exists sys_user_post;
create table sys_user_post
(
  user_id   bigint(20) not null comment '用户ID',
  post_id   bigint(20) not null comment '岗位ID',
  primary key (user_id, post_id)
) engine=innodb comment = '用户与岗位关联表';

-- ----------------------------
-- 初始化-用户与岗位关联表数据
-- ----------------------------
insert into sys_user_post values ('1', '1');
insert into sys_user_post values ('2', '2');


-- ----------------------------
-- 10、操作日志记录
-- ----------------------------
drop table if exists sys_oper_log;
create table sys_oper_log (
  oper_id           bigint(20)      not null auto_increment    comment '日志主键',
  title             varchar(50)     default ''                 comment '模块标题',
  business_type     int(2)          default 0                  comment '业务类型（0其它 1新增 2修改 3删除）',
  method            varchar(200)    default ''                 comment '方法名称',
  request_method    varchar(10)     default ''                 comment '请求方式',
  operator_type     int(1)          default 0                  comment '操作类别（0其它 1后台用户 2手机端用户）',
  oper_name         varchar(50)     default ''                 comment '操作人员',
  dept_name         varchar(50)     default ''                 comment '部门名称',
  oper_url          varchar(255)    default ''                 comment '请求URL',
  oper_ip           varchar(128)    default ''                 comment '主机地址',
  oper_location     varchar(255)    default ''                 comment '操作地点',
  oper_param        varchar(2000)   default ''                 comment '请求参数',
  json_result       varchar(2000)   default ''                 comment '返回参数',
  status            int(1)          default 0                  comment '操作状态（0正常 1异常）',
  error_msg         varchar(2000)   default ''                 comment '错误消息',
  oper_time         datetime                                   comment '操作时间',
  cost_time         bigint(20)      default 0                  comment '消耗时间',
  primary key (oper_id),
  key idx_sys_oper_log_bt (business_type),
  key idx_sys_oper_log_s  (status),
  key idx_sys_oper_log_ot (oper_time)
) engine=innodb auto_increment=100 comment = '操作日志记录';


-- ----------------------------
-- 11、字典类型表
-- ----------------------------
drop table if exists sys_dict_type;
create table sys_dict_type
(
  dict_id          bigint(20)      not null auto_increment    comment '字典主键',
  dict_name        varchar(100)    default ''                 comment '字典名称',
  dict_type        varchar(100)    default ''                 comment '字典类型',
  status           char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                   comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                   comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  primary key (dict_id),
  unique (dict_type)
) engine=innodb auto_increment=100 comment = '字典类型表';

insert into sys_dict_type values(1,  '用户性别', 'sys_user_sex',        '0', 'admin', sysdate(), '', null, '用户性别列表');
insert into sys_dict_type values(2,  '菜单状态', 'sys_show_hide',       '0', 'admin', sysdate(), '', null, '菜单状态列表');
insert into sys_dict_type values(3,  '系统开关', 'sys_normal_disable',  '0', 'admin', sysdate(), '', null, '系统开关列表');
insert into sys_dict_type values(4,  '任务状态', 'sys_job_status',      '0', 'admin', sysdate(), '', null, '任务状态列表');
insert into sys_dict_type values(5,  '任务分组', 'sys_job_group',       '0', 'admin', sysdate(), '', null, '任务分组列表');
insert into sys_dict_type values(6,  '系统是否', 'sys_yes_no',          '0', 'admin', sysdate(), '', null, '系统是否列表');
insert into sys_dict_type values(7,  '通知类型', 'sys_notice_type',     '0', 'admin', sysdate(), '', null, '通知类型列表');
insert into sys_dict_type values(8,  '通知状态', 'sys_notice_status',   '0', 'admin', sysdate(), '', null, '通知状态列表');
insert into sys_dict_type values(9,  '操作类型', 'sys_oper_type',       '0', 'admin', sysdate(), '', null, '操作类型列表');
insert into sys_dict_type values(10, '系统状态', 'sys_common_status',   '0', 'admin', sysdate(), '', null, '登录状态列表');


-- ----------------------------
-- 12、字典数据表
-- ----------------------------
drop table if exists sys_dict_data;
create table sys_dict_data
(
  dict_code        bigint(20)      not null auto_increment    comment '字典编码',
  dict_sort        int(4)          default 0                  comment '字典排序',
  dict_label       varchar(100)    default ''                 comment '字典标签',
  dict_value       varchar(100)    default ''                 comment '字典键值',
  dict_type        varchar(100)    default ''                 comment '字典类型',
  css_class        varchar(100)    default null               comment '样式属性（其他样式扩展）',
  list_class       varchar(100)    default null               comment '表格回显样式',
  is_default       char(1)         default 'N'                comment '是否默认（Y是 N否）',
  status           char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                   comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                   comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  primary key (dict_code)
) engine=innodb auto_increment=100 comment = '字典数据表';

insert into sys_dict_data values(1,  1,  '男',       '0',       'sys_user_sex',        '',   '',        'Y', '0', 'admin', sysdate(), '', null, '性别男');
insert into sys_dict_data values(2,  2,  '女',       '1',       'sys_user_sex',        '',   '',        'N', '0', 'admin', sysdate(), '', null, '性别女');
insert into sys_dict_data values(3,  3,  '未知',     '2',       'sys_user_sex',        '',   '',        'N', '0', 'admin', sysdate(), '', null, '性别未知');
insert into sys_dict_data values(4,  1,  '显示',     '0',       'sys_show_hide',       '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '显示菜单');
insert into sys_dict_data values(5,  2,  '隐藏',     '1',       'sys_show_hide',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '隐藏菜单');
insert into sys_dict_data values(6,  1,  '正常',     '0',       'sys_normal_disable',  '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data values(7,  2,  '停用',     '1',       'sys_normal_disable',  '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '停用状态');
insert into sys_dict_data values(8,  1,  '正常',     '0',       'sys_job_status',      '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data values(9,  2,  '暂停',     '1',       'sys_job_status',      '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '停用状态');
insert into sys_dict_data values(10, 1,  '默认',     'DEFAULT', 'sys_job_group',       '',   '',        'Y', '0', 'admin', sysdate(), '', null, '默认分组');
insert into sys_dict_data values(11, 2,  '系统',     'SYSTEM',  'sys_job_group',       '',   '',        'N', '0', 'admin', sysdate(), '', null, '系统分组');
insert into sys_dict_data values(12, 1,  '是',       'Y',       'sys_yes_no',          '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '系统默认是');
insert into sys_dict_data values(13, 2,  '否',       'N',       'sys_yes_no',          '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '系统默认否');
insert into sys_dict_data values(14, 1,  '通知',     '1',       'sys_notice_type',     '',   'warning', 'Y', '0', 'admin', sysdate(), '', null, '通知');
insert into sys_dict_data values(15, 2,  '公告',     '2',       'sys_notice_type',     '',   'success', 'N', '0', 'admin', sysdate(), '', null, '公告');
insert into sys_dict_data values(16, 1,  '正常',     '0',       'sys_notice_status',   '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data values(17, 2,  '关闭',     '1',       'sys_notice_status',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '关闭状态');
insert into sys_dict_data values(18, 99, '其他',     '0',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', sysdate(), '', null, '其他操作');
insert into sys_dict_data values(19, 1,  '新增',     '1',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', sysdate(), '', null, '新增操作');
insert into sys_dict_data values(20, 2,  '修改',     '2',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', sysdate(), '', null, '修改操作');
insert into sys_dict_data values(21, 3,  '删除',     '3',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '删除操作');
insert into sys_dict_data values(22, 4,  '授权',     '4',       'sys_oper_type',       '',   'primary', 'N', '0', 'admin', sysdate(), '', null, '授权操作');
insert into sys_dict_data values(23, 5,  '导出',     '5',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', sysdate(), '', null, '导出操作');
insert into sys_dict_data values(24, 6,  '导入',     '6',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', sysdate(), '', null, '导入操作');
insert into sys_dict_data values(25, 7,  '强退',     '7',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '强退操作');
insert into sys_dict_data values(26, 8,  '生成代码', '8',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', sysdate(), '', null, '生成操作');
insert into sys_dict_data values(27, 9,  '清空数据', '9',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '清空操作');
insert into sys_dict_data values(28, 1,  '成功',     '0',       'sys_common_status',   '',   'primary', 'N', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data values(29, 2,  '失败',     '1',       'sys_common_status',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '停用状态');


-- ----------------------------
-- 13、参数配置表
-- ----------------------------
drop table if exists sys_config;
create table sys_config (
  config_id         int(5)          not null auto_increment    comment '参数主键',
  config_name       varchar(100)    default ''                 comment '参数名称',
  config_key        varchar(100)    default ''                 comment '参数键名',
  config_value      varchar(500)    default ''                 comment '参数键值',
  config_type       char(1)         default 'N'                comment '系统内置（Y是 N否）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (config_id)
) engine=innodb auto_increment=100 comment = '参数配置表';

insert into sys_config values(1, '主框架页-默认皮肤样式名称',     'sys.index.skinName',               'skin-blue',     'Y', 'admin', sysdate(), '', null, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow' );
insert into sys_config values(2, '用户管理-账号初始密码',         'sys.user.initPassword',            '123456',        'Y', 'admin', sysdate(), '', null, '初始化密码 123456' );
insert into sys_config values(3, '主框架页-侧边栏主题',           'sys.index.sideTheme',              'theme-dark',    'Y', 'admin', sysdate(), '', null, '深色主题theme-dark，浅色主题theme-light' );
insert into sys_config values(4, '账号自助-验证码开关',           'sys.account.captchaEnabled',       'true',          'Y', 'admin', sysdate(), '', null, '是否开启验证码功能（true开启，false关闭）');
insert into sys_config values(5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser',         'false',         'Y', 'admin', sysdate(), '', null, '是否开启注册用户功能（true开启，false关闭）');
insert into sys_config values(6, '用户登录-黑名单列表',           'sys.login.blackIPList',            '',              'Y', 'admin', sysdate(), '', null, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');
insert into sys_config values(7, '用户管理-初始密码修改策略',     'sys.account.initPasswordModify',   '1',             'Y', 'admin', sysdate(), '', null, '0：初始密码修改策略关闭，没有任何提示，1：提醒用户，如果未修改初始密码，则在登录时就会提醒修改密码对话框');
insert into sys_config values(8, '用户管理-账号密码更新周期',     'sys.account.passwordValidateDays', '0',             'Y', 'admin', sysdate(), '', null, '密码更新周期（填写数字，数据初始化值为0不限制，若修改必须为大于0小于365的正整数），如果超过这个周期登录系统时，则在登录时就会提醒修改密码对话框');
insert into sys_config values(9, '用户管理-密码字符范围',         'sys.account.chrtype',              '0',             'Y', 'admin', sysdate(), '', null, '默认任意字符范围，0任意（密码可以输入任意字符），1数字（密码只能为0-9数字），2英文字母（密码只能为a-z和A-Z字母），3字母和数字（密码必须包含字母，数字）,4字母数字和特殊字符（目前支持的特殊字符包括：~!@#$%^&*()-=_+）');


-- ----------------------------
-- 14、系统访问记录
-- ----------------------------
drop table if exists sys_logininfor;
create table sys_logininfor (
  info_id        bigint(20)     not null auto_increment   comment '访问ID',
  user_name      varchar(50)    default ''                comment '用户账号',
  ipaddr         varchar(128)   default ''                comment '登录IP地址',
  login_location varchar(255)   default ''                comment '登录地点',
  browser        varchar(50)    default ''                comment '浏览器类型',
  os             varchar(50)    default ''                comment '操作系统',
  status         char(1)        default '0'               comment '登录状态（0成功 1失败）',
  msg            varchar(255)   default ''                comment '提示消息',
  login_time     datetime                                 comment '访问时间',
  primary key (info_id),
  key idx_sys_logininfor_s  (status),
  key idx_sys_logininfor_lt (login_time)
) engine=innodb auto_increment=100 comment = '系统访问记录';


-- ----------------------------
-- 15、定时任务调度表
-- ----------------------------
drop table if exists sys_job;
create table sys_job (
  job_id              bigint(20)    not null auto_increment    comment '任务ID',
  job_name            varchar(64)   default ''                 comment '任务名称',
  job_group           varchar(64)   default 'DEFAULT'          comment '任务组名',
  invoke_target       varchar(500)  not null                   comment '调用目标字符串',
  cron_expression     varchar(255)  default ''                 comment 'cron执行表达式',
  misfire_policy      varchar(20)   default '3'                comment '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  concurrent          char(1)       default '1'                comment '是否并发执行（0允许 1禁止）',
  status              char(1)       default '0'                comment '状态（0正常 1暂停）',
  create_by           varchar(64)   default ''                 comment '创建者',
  create_time         datetime                                 comment '创建时间',
  update_by           varchar(64)   default ''                 comment '更新者',
  update_time         datetime                                 comment '更新时间',
  remark              varchar(500)  default ''                 comment '备注信息',
  primary key (job_id, job_name, job_group)
) engine=innodb auto_increment=100 comment = '定时任务调度表';

insert into sys_job values(1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams',        '0/10 * * * * ?', '3', '1', '1', 'admin', sysdate(), '', null, '');
insert into sys_job values(2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')',  '0/15 * * * * ?', '3', '1', '1', 'admin', sysdate(), '', null, '');
insert into sys_job values(3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)',  '0/20 * * * * ?', '3', '1', '1', 'admin', sysdate(), '', null, '');


-- ----------------------------
-- 16、定时任务调度日志表
-- ----------------------------
drop table if exists sys_job_log;
create table sys_job_log (
  job_log_id          bigint(20)     not null auto_increment    comment '任务日志ID',
  job_name            varchar(64)    not null                   comment '任务名称',
  job_group           varchar(64)    not null                   comment '任务组名',
  invoke_target       varchar(500)   not null                   comment '调用目标字符串',
  job_message         varchar(500)                              comment '日志信息',
  status              char(1)        default '0'                comment '执行状态（0正常 1失败）',
  exception_info      varchar(2000)  default ''                 comment '异常信息',
  start_time          datetime                                  comment '执行开始时间',
  end_time            datetime                                  comment '执行结束时间',
  create_time         datetime                                  comment '创建时间',
  primary key (job_log_id)
) engine=innodb comment = '定时任务调度日志表';


-- ----------------------------
-- 17、通知公告表
-- ----------------------------
drop table if exists sys_notice;
create table sys_notice (
  notice_id         int(4)          not null auto_increment    comment '公告ID',
  notice_title      varchar(50)     not null                   comment '公告标题',
  notice_type       char(1)         not null                   comment '公告类型（1通知 2公告）',
  notice_content    longblob        default null               comment '公告内容',
  status            char(1)         default '0'                comment '公告状态（0正常 1关闭）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(255)    default null               comment '备注',
  primary key (notice_id)
) engine=innodb auto_increment=10 comment = '通知公告表';

-- ----------------------------
-- 初始化-公告信息表数据
-- ----------------------------
insert into sys_notice values('1', '温馨提醒：2018-07-01 若依新版本发布啦', '2', '新版本内容', '0', 'admin', sysdate(), '', null, '管理员');
insert into sys_notice values('2', '维护通知：2018-07-01 若依系统凌晨维护', '1', '维护内容',   '0', 'admin', sysdate(), '', null, '管理员');
insert into sys_notice values('3', '若依开源框架介绍', '1', '<p><span style=\"color: rgb(230, 0, 0);\">项目介绍</span></p><p><font color=\"#333333\">RuoYi开源项目是为企业用户定制的后台脚手架框架，为企业打造的一站式解决方案，降低企业开发成本，提升开发效率。主要包括用户管理、角色管理、部门管理、菜单管理、参数管理、字典管理、</font><span style=\"color: rgb(51, 51, 51);\">岗位管理</span><span style=\"color: rgb(51, 51, 51);\">、定时任务</span><span style=\"color: rgb(51, 51, 51);\">、</span><span style=\"color: rgb(51, 51, 51);\">服务监控、登录日志、操作日志、代码生成等功能。其中，还支持多数据源、数据权限、国际化、Redis缓存、Docker部署、滑动验证码、第三方认证登录、分布式事务、</span><font color=\"#333333\">分布式文件存储</font><span style=\"color: rgb(51, 51, 51);\">、分库分表处理等技术特点。</span></p><p><img src=\"https://foruda.gitee.com/images/1773931848342439032/a4d22313_1815095.png\" style=\"width: 64px;\"><br></p><p><span style=\"color: rgb(230, 0, 0);\">官网及演示</span></p><p><span style=\"color: rgb(51, 51, 51);\">若依官网地址：&nbsp;</span><a href=\"http://ruoyi.vip\" target=\"_blank\">http://ruoyi.vip</a><a href=\"http://ruoyi.vip\" target=\"_blank\"></a></p><p><span style=\"color: rgb(51, 51, 51);\">若依文档地址：&nbsp;</span><a href=\"http://doc.ruoyi.vip\" target=\"_blank\">http://doc.ruoyi.vip</a><br></p><p><span style=\"color: rgb(51, 51, 51);\">演示地址【不分离版】：&nbsp;</span><a href=\"http://demo.ruoyi.vip\" target=\"_blank\">http://demo.ruoyi.vip</a></p><p><span style=\"color: rgb(51, 51, 51);\">演示地址【分离版本】：&nbsp;</span><a href=\"http://vue.ruoyi.vip\" target=\"_blank\">http://vue.ruoyi.vip</a></p><p><span style=\"color: rgb(51, 51, 51);\">演示地址【微服务版】：&nbsp;</span><a href=\"http://cloud.ruoyi.vip\" target=\"_blank\">http://cloud.ruoyi.vip</a></p><p><span style=\"color: rgb(51, 51, 51);\">演示地址【移动端版】：&nbsp;</span><a href=\"http://h5.ruoyi.vip\" target=\"_blank\">http://h5.ruoyi.vip</a></p><p><br style=\"color: rgb(48, 49, 51); font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif; font-size: 12px;\"></p>', '0', 'admin', sysdate(), '', null, '管理员');


-- ----------------------------
-- 18、公告已读记录表
-- ----------------------------
drop table if exists sys_notice_read;
create table sys_notice_read (
  read_id          bigint(20)       not null auto_increment    comment '已读主键',
  notice_id        int(4)           not null                   comment '公告id',
  user_id          bigint(20)       not null                   comment '用户id',
  read_time        datetime         not null                   comment '阅读时间',
  primary key (read_id),
  unique key uk_user_notice (user_id, notice_id)   comment '同一用户同一公告只记录一次'
) engine=innodb auto_increment=1 comment='公告已读记录表';


-- ----------------------------
-- 19、代码生成业务表
-- ----------------------------
drop table if exists gen_table;
create table gen_table (
  table_id          bigint(20)      not null auto_increment    comment '编号',
  table_name        varchar(200)    default ''                 comment '表名称',
  table_comment     varchar(500)    default ''                 comment '表描述',
  sub_table_name    varchar(64)     default null               comment '关联子表的表名',
  sub_table_fk_name varchar(64)     default null               comment '子表关联的外键名',
  class_name        varchar(100)    default ''                 comment '实体类名称',
  tpl_category      varchar(200)    default 'crud'             comment '使用的模板（crud单表操作 tree树表操作）',
  tpl_web_type      varchar(30)     default ''                 comment '前端模板类型（element-ui模版 element-plus模版）',
  package_name      varchar(100)                               comment '生成包路径',
  module_name       varchar(30)                                comment '生成模块名',
  business_name     varchar(30)                                comment '生成业务名',
  function_name     varchar(50)                                comment '生成功能名',
  function_author   varchar(50)                                comment '生成功能作者',
  form_col_num      int(1)          default 1                  comment '表单布局（单列 双列 三列）',
  gen_type          char(1)         default '0'                comment '生成代码方式（0zip压缩包 1自定义路径）',
  gen_path          varchar(200)    default '/'                comment '生成路径（不填默认项目路径）',
  options           varchar(1000)                              comment '其它生成选项',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (table_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表';


-- ----------------------------
-- 20、代码生成业务表字段
-- ----------------------------
drop table if exists gen_table_column;
create table gen_table_column (
  column_id         bigint(20)      not null auto_increment    comment '编号',
  table_id          bigint(20)                                 comment '归属表编号',
  column_name       varchar(200)                               comment '列名称',
  column_comment    varchar(500)                               comment '列描述',
  column_type       varchar(100)                               comment '列类型',
  java_type         varchar(500)                               comment 'JAVA类型',
  java_field        varchar(200)                               comment 'JAVA字段名',
  is_pk             char(1)                                    comment '是否主键（1是）',
  is_increment      char(1)                                    comment '是否自增（1是）',
  is_required       char(1)                                    comment '是否必填（1是）',
  is_insert         char(1)                                    comment '是否为插入字段（1是）',
  is_edit           char(1)                                    comment '是否编辑字段（1是）',
  is_list           char(1)                                    comment '是否列表字段（1是）',
  is_query          char(1)                                    comment '是否查询字段（1是）',
  query_type        varchar(200)    default 'EQ'               comment '查询方式（等于、不等于、大于、小于、范围）',
  html_type         varchar(200)                               comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  dict_type         varchar(200)    default ''                 comment '字典类型',
  sort              int                                        comment '排序',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (column_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表字段';


-- ----------------------------
-- 21、虚拟仿真实训业务表
-- ----------------------------
drop table if exists vt_lab;
create table vt_lab (
  lab_id           bigint(20)      not null auto_increment    comment '实验室ID',
  lab_name         varchar(100)    not null                   comment '实验室名称',
  college_name     varchar(100)    default ''                 comment '所属院校或院系',
  location         varchar(200)    default ''                 comment '地点',
  capacity         int(11)         default 0                  comment '容量',
  open_status      char(1)         default '0'                comment '开放状态（0开放 1关闭 2维护）',
  manager_name     varchar(50)     default ''                 comment '负责人',
  contact_phone    varchar(30)     default ''                 comment '联系方式',
  introduction     varchar(1000)   default ''                 comment '简介',
  create_by        varchar(64)     default '',
  create_time      datetime,
  update_by        varchar(64)     default '',
  update_time      datetime,
  remark           varchar(500)    default '',
  primary key (lab_id)
) engine=innodb auto_increment=100 comment='虚拟仿真实验室';

drop table if exists vt_device;
create table vt_device (
  device_id        bigint(20)      not null auto_increment    comment '设备ID',
  device_name      varchar(100)    not null                   comment '设备名称',
  device_code      varchar(64)     not null                   comment '设备编号',
  lab_id           bigint(20)      default null               comment '所属实验室ID',
  device_type      varchar(50)     default ''                 comment '设备类型',
  run_status       char(1)         default '0'                comment '运行状态（0正常 1维护 2故障）',
  online_status    char(1)         default '0'                comment '在线状态（0在线 1离线）',
  last_check_time  datetime                                   comment '最近检测时间',
  create_by        varchar(64)     default '',
  create_time      datetime,
  update_by        varchar(64)     default '',
  update_time      datetime,
  remark           varchar(500)    default '',
  primary key (device_id),
  unique key uk_vt_device_code (device_code),
  key idx_vt_device_lab (lab_id)
) engine=innodb auto_increment=100 comment='虚拟仿真设备';

drop table if exists vt_resource;
create table vt_resource (
  resource_id      bigint(20)      not null auto_increment    comment '资源ID',
  resource_name    varchar(120)    not null                   comment '资源名称',
  resource_type    char(1)         default '0'                comment '资源类型（0虚拟仿真 1视频 2音频 3文档）',
  major_name       varchar(100)    default ''                 comment '所属专业',
  course_name      varchar(100)    default ''                 comment '适用课程',
  cover_url        varchar(500)    default ''                 comment '封面地址',
  file_url         varchar(500)    default ''                 comment '附件地址',
  share_status     char(1)         default '0'                comment '共享状态（0开放 1校内 2停用）',
  view_count       int(11)         default 0                  comment '浏览量',
  collect_count    int(11)         default 0                  comment '收藏量',
  introduction     varchar(1000)   default ''                 comment '简介',
  create_by        varchar(64)     default '',
  create_time      datetime,
  update_by        varchar(64)     default '',
  update_time      datetime,
  remark           varchar(500)    default '',
  primary key (resource_id)
) engine=innodb auto_increment=100 comment='虚拟仿真实训资源';

drop table if exists vt_share_apply;
create table vt_share_apply (
  apply_id         bigint(20)      not null auto_increment    comment '申请ID',
  applicant_name   varchar(50)     not null                   comment '申请人',
  phone            varchar(30)     default ''                 comment '手机号',
  organization     varchar(120)    default ''                 comment '单位',
  apply_type       char(1)         default '0'                comment '申请类型（0资源 1实验室）',
  target_id        bigint(20)      default null               comment '关联资源或实验室ID',
  target_name      varchar(120)    default ''                 comment '关联对象名称',
  reserve_time     datetime                                   comment '预约时间',
  user_count       int(11)         default 1                  comment '使用人数',
  apply_status     char(1)         default '0'                comment '申请状态（0待审核 1通过 2驳回）',
  audit_opinion    varchar(500)    default ''                 comment '审核意见',
  create_by        varchar(64)     default '',
  create_time      datetime,
  update_by        varchar(64)     default '',
  update_time      datetime,
  remark           varchar(500)    default '',
  primary key (apply_id)
) engine=innodb auto_increment=100 comment='共享开放申请';

drop table if exists vt_course;
create table vt_course (
  course_id        bigint(20)      not null auto_increment    comment '课程ID',
  course_name      varchar(100)    not null                   comment '课程名称',
  major_direction  varchar(100)    default ''                 comment '专业方向',
  class_hours      int(11)         default 0                  comment '课时',
  teacher_name     varchar(50)     default ''                 comment '授课教师',
  course_status    char(1)         default '0'                comment '课程状态（0启用 1停用）',
  introduction     varchar(1000)   default ''                 comment '课程简介',
  create_by        varchar(64)     default '',
  create_time      datetime,
  update_by        varchar(64)     default '',
  update_time      datetime,
  remark           varchar(500)    default '',
  primary key (course_id)
) engine=innodb auto_increment=100 comment='实训课程';

drop table if exists vt_experiment;
create table vt_experiment (
  experiment_id    bigint(20)      not null auto_increment    comment '实验ID',
  experiment_name  varchar(120)    not null                   comment '实验名称',
  course_id        bigint(20)      default null               comment '课程ID',
  resource_id      bigint(20)      default null               comment '资源ID',
  difficulty       char(1)         default '1'                comment '难度（1初级 2中级 3高级）',
  duration_minutes int(11)         default 45                 comment '预计时长分钟',
  open_status      char(1)         default '0'                comment '开放状态（0开放 1关闭）',
  introduction     varchar(1000)   default ''                 comment '实验简介',
  create_by        varchar(64)     default '',
  create_time      datetime,
  update_by        varchar(64)     default '',
  update_time      datetime,
  remark           varchar(500)    default '',
  primary key (experiment_id),
  key idx_vt_exp_course (course_id),
  key idx_vt_exp_resource (resource_id)
) engine=innodb auto_increment=100 comment='实训实验';

drop table if exists vt_teaching_plan;
create table vt_teaching_plan (
  plan_id          bigint(20)      not null auto_increment    comment '计划ID',
  plan_name        varchar(120)    not null                   comment '计划名称',
  course_id        bigint(20)      default null               comment '课程ID',
  experiment_id    bigint(20)      default null               comment '实验ID',
  class_target     varchar(100)    default ''                 comment '班级或对象',
  start_time       datetime                                   comment '计划开始时间',
  end_time         datetime                                   comment '计划结束时间',
  plan_status      char(1)         default '0'                comment '计划状态（0未开始 1进行中 2已结束）',
  manager_name     varchar(50)     default ''                 comment '负责人',
  create_by        varchar(64)     default '',
  create_time      datetime,
  update_by        varchar(64)     default '',
  update_time      datetime,
  remark           varchar(500)    default '',
  primary key (plan_id),
  key idx_vt_plan_course (course_id),
  key idx_vt_plan_exp (experiment_id)
) engine=innodb auto_increment=100 comment='教学计划';

drop table if exists vt_training_record;
create table vt_training_record (
  record_id        bigint(20)      not null auto_increment    comment '记录ID',
  plan_id          bigint(20)      default null               comment '计划ID',
  student_name     varchar(50)     not null                   comment '学员姓名',
  student_no       varchar(50)     default ''                 comment '学号或编号',
  start_time       datetime                                   comment '开始时间',
  end_time         datetime                                   comment '结束时间',
  complete_status  char(1)         default '0'                comment '完成状态（0未完成 1已完成 2异常）',
  score            decimal(5,2)    default 0                  comment '得分',
  duration_minutes int(11)         default 0                  comment '用时分钟',
  evaluation       varchar(500)    default ''                 comment '评价',
  create_by        varchar(64)     default '',
  create_time      datetime,
  update_by        varchar(64)     default '',
  update_time      datetime,
  remark           varchar(500)    default '',
  primary key (record_id),
  key idx_vt_record_plan (plan_id)
) engine=innodb auto_increment=100 comment='实训过程结果';


-- ----------------------------
-- 初始化-虚拟仿真实训字典数据
-- ----------------------------
insert into sys_dict_type values(100, '实验室开放状态', 'vt_open_status', '0', 'admin', sysdate(), '', null, '实验室开放状态');
insert into sys_dict_type values(101, '设备运行状态', 'vt_run_status', '0', 'admin', sysdate(), '', null, '设备运行状态');
insert into sys_dict_type values(102, '设备在线状态', 'vt_online_status', '0', 'admin', sysdate(), '', null, '设备在线状态');
insert into sys_dict_type values(103, '资源类型', 'vt_resource_type', '0', 'admin', sysdate(), '', null, '资源类型');
insert into sys_dict_type values(104, '共享状态', 'vt_share_status', '0', 'admin', sysdate(), '', null, '共享状态');
insert into sys_dict_type values(105, '申请类型', 'vt_apply_type', '0', 'admin', sysdate(), '', null, '申请类型');
insert into sys_dict_type values(106, '申请状态', 'vt_apply_status', '0', 'admin', sysdate(), '', null, '申请状态');
insert into sys_dict_type values(107, '实验难度', 'vt_difficulty', '0', 'admin', sysdate(), '', null, '实验难度');
insert into sys_dict_type values(108, '课程状态', 'vt_course_status', '0', 'admin', sysdate(), '', null, '课程状态');
insert into sys_dict_type values(109, '教学计划状态', 'vt_plan_status', '0', 'admin', sysdate(), '', null, '教学计划状态');
insert into sys_dict_type values(110, '完成状态', 'vt_complete_status', '0', 'admin', sysdate(), '', null, '完成状态');

insert into sys_dict_data values(100, 1, '开放', '0', 'vt_open_status', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '开放');
insert into sys_dict_data values(101, 2, '关闭', '1', 'vt_open_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '关闭');
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
insert into sys_dict_data values(123, 1, '启用', '0', 'vt_course_status', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '启用');
insert into sys_dict_data values(124, 2, '停用', '1', 'vt_course_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '停用');
insert into sys_dict_data values(125, 1, '未开始', '0', 'vt_plan_status', '', 'info', 'Y', '0', 'admin', sysdate(), '', null, '未开始');
insert into sys_dict_data values(126, 2, '进行中', '1', 'vt_plan_status', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '进行中');
insert into sys_dict_data values(127, 3, '已结束', '2', 'vt_plan_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '已结束');
insert into sys_dict_data values(128, 1, '未完成', '0', 'vt_complete_status', '', 'warning', 'Y', '0', 'admin', sysdate(), '', null, '未完成');
insert into sys_dict_data values(129, 2, '已完成', '1', 'vt_complete_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '已完成');
insert into sys_dict_data values(130, 3, '异常', '2', 'vt_complete_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '异常');


-- ----------------------------
-- 初始化-虚拟仿真实训菜单和权限
-- ----------------------------
insert into sys_menu values('2000', '资源管理', '0', '4', 'virtualResource', null, '', '', 1, 0, 'M', '0', '0', '', 'education', 'admin', sysdate(), '', null, '虚拟仿真资源管理目录');
insert into sys_menu values('2001', '实训管理', '0', '5', 'virtualTraining', null, '', '', 1, 0, 'M', '0', '0', '', 'skill', 'admin', sysdate(), '', null, '虚拟仿真实训管理目录');
insert into sys_menu values('2002', '数据概览', '0', '6', 'virtualData', null, '', '', 1, 0, 'M', '0', '0', '', 'dashboard', 'admin', sysdate(), '', null, '虚拟仿真数据概览目录');
insert into sys_menu values('2003', '监控管理', '0', '7', 'virtualMonitor', null, '', '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', sysdate(), '', null, '虚拟仿真监控目录');
insert into sys_menu values('2004', '效能管理', '0', '8', 'virtualEffect', null, '', '', 1, 0, 'M', '0', '0', '', 'chart', 'admin', sysdate(), '', null, '虚拟仿真效能目录');
insert into sys_menu values('2010', '实验室管理', '2000', '1', 'lab', 'virtual/lab/index', '', '', 1, 0, 'C', '0', '0', 'virtual:lab:list', 'education', 'admin', sysdate(), '', null, '实验室管理菜单');
insert into sys_menu values('2011', '仿真设备', '2000', '2', 'device', 'virtual/device/index', '', '', 1, 0, 'C', '0', '0', 'virtual:device:list', 'server', 'admin', sysdate(), '', null, '仿真设备菜单');
insert into sys_menu values('2012', '实训资源', '2000', '3', 'resource', 'virtual/resource/index', '', '', 1, 0, 'C', '0', '0', 'virtual:resource:list', 'documentation', 'admin', sysdate(), '', null, '实训资源菜单');
insert into sys_menu values('2013', '共享申请', '2000', '4', 'shareApply', 'virtual/shareApply/index', '', '', 1, 0, 'C', '0', '0', 'virtual:shareApply:list', 'message', 'admin', sysdate(), '', null, '共享申请菜单');
insert into sys_menu values('2014', '课程管理', '2001', '1', 'course', 'virtual/course/index', '', '', 1, 0, 'C', '0', '0', 'virtual:course:list', 'guide', 'admin', sysdate(), '', null, '课程管理菜单');
insert into sys_menu values('2015', '实训实验', '2001', '2', 'experiment', 'virtual/experiment/index', '', '', 1, 0, 'C', '0', '0', 'virtual:experiment:list', 'example', 'admin', sysdate(), '', null, '实训实验菜单');
insert into sys_menu values('2016', '教学计划', '2001', '3', 'plan', 'virtual/plan/index', '', '', 1, 0, 'C', '0', '0', 'virtual:plan:list', 'date', 'admin', sysdate(), '', null, '教学计划菜单');
insert into sys_menu values('2017', '过程结果', '2001', '4', 'record', 'virtual/record/index', '', '', 1, 0, 'C', '0', '0', 'virtual:record:list', 'form', 'admin', sysdate(), '', null, '过程结果菜单');
insert into sys_menu values('2018', '概览统计', '2002', '1', 'dashboard', 'virtual/dashboard/index', '', '', 1, 0, 'C', '0', '0', 'virtual:dashboard:list', 'dashboard', 'admin', sysdate(), '', null, '概览统计菜单');
insert into sys_menu values('2019', '实训分析', '2002', '2', 'trainingAnalysis', 'virtual/effect/experiment', '', '', 1, 0, 'C', '0', '0', 'virtual:dashboard:list', 'chart', 'admin', sysdate(), '', null, '实训分析菜单');
insert into sys_menu values('2020', '数据导出', '2002', '3', 'dataExport', 'virtual/effect/share', '', '', 1, 0, 'C', '0', '0', 'virtual:dashboard:list', 'excel', 'admin', sysdate(), '', null, '数据导出菜单');
insert into sys_menu values('2021', '设备监控', '2003', '1', 'deviceMonitor', 'virtual/monitor/device', '', '', 1, 0, 'C', '0', '0', 'virtual:dashboard:list', 'monitor', 'admin', sysdate(), '', null, '设备监控菜单');
insert into sys_menu values('2022', '资源监控', '2003', '2', 'resourceMonitor', 'virtual/monitor/resource', '', '', 1, 0, 'C', '0', '0', 'virtual:dashboard:list', 'redis-list', 'admin', sysdate(), '', null, '资源监控菜单');
insert into sys_menu values('2023', '教学监控', '2003', '3', 'teachingMonitor', 'virtual/monitor/teaching', '', '', 1, 0, 'C', '0', '0', 'virtual:dashboard:list', 'online', 'admin', sysdate(), '', null, '教学监控菜单');
insert into sys_menu values('2024', '资源利用率', '2004', '1', 'resourceEffect', 'virtual/effect/resource', '', '', 1, 0, 'C', '0', '0', 'virtual:dashboard:list', 'rate', 'admin', sysdate(), '', null, '资源利用率菜单');
insert into sys_menu values('2025', '实验完成率', '2004', '2', 'experimentEffect', 'virtual/effect/experiment', '', '', 1, 0, 'C', '0', '0', 'virtual:dashboard:list', 'skill', 'admin', sysdate(), '', null, '实验完成率菜单');
insert into sys_menu values('2026', '开放共享成效', '2004', '3', 'shareEffect', 'virtual/effect/share', '', '', 1, 0, 'C', '0', '0', 'virtual:dashboard:list', 'international', 'admin', sysdate(), '', null, '开放共享成效菜单');

insert into sys_menu values('2100', '实验室查询', '2010', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:lab:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2101', '实验室新增', '2010', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:lab:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2102', '实验室修改', '2010', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:lab:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2103', '实验室删除', '2010', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:lab:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2104', '实验室导出', '2010', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:lab:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2105', '设备查询', '2011', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:device:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2106', '设备新增', '2011', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:device:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2107', '设备修改', '2011', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:device:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2108', '设备删除', '2011', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:device:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2109', '设备导出', '2011', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:device:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2110', '资源查询', '2012', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:resource:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2111', '资源新增', '2012', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:resource:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2112', '资源修改', '2012', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:resource:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2113', '资源删除', '2012', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:resource:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2114', '资源导出', '2012', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:resource:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2115', '申请查询', '2013', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:shareApply:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2116', '申请新增', '2013', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:shareApply:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2117', '申请修改', '2013', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:shareApply:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2118', '申请删除', '2013', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:shareApply:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2119', '申请导出', '2013', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:shareApply:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2120', '申请审核', '2013', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:shareApply:audit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2121', '课程查询', '2014', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:course:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2122', '课程新增', '2014', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:course:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2123', '课程修改', '2014', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:course:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2124', '课程删除', '2014', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:course:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2125', '课程导出', '2014', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:course:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2126', '实验查询', '2015', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:experiment:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2127', '实验新增', '2015', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:experiment:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2128', '实验修改', '2015', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:experiment:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2129', '实验删除', '2015', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:experiment:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2130', '实验导出', '2015', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:experiment:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2131', '计划查询', '2016', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:plan:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2132', '计划新增', '2016', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:plan:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2133', '计划修改', '2016', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:plan:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2134', '计划删除', '2016', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:plan:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2135', '计划导出', '2016', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:plan:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2136', '结果查询', '2017', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:record:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2137', '结果新增', '2017', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:record:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2138', '结果修改', '2017', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:record:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2139', '结果删除', '2017', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:record:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2140', '结果导出', '2017', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'virtual:record:export', '#', 'admin', sysdate(), '', null, '');

insert into sys_role_menu values ('2', '2000');
insert into sys_role_menu values ('2', '2001');
insert into sys_role_menu values ('2', '2002');
insert into sys_role_menu values ('2', '2003');
insert into sys_role_menu values ('2', '2004');
insert into sys_role_menu values ('2', '2010');
insert into sys_role_menu values ('2', '2011');
insert into sys_role_menu values ('2', '2012');
insert into sys_role_menu values ('2', '2013');
insert into sys_role_menu values ('2', '2014');
insert into sys_role_menu values ('2', '2015');
insert into sys_role_menu values ('2', '2016');
insert into sys_role_menu values ('2', '2017');
insert into sys_role_menu values ('2', '2018');
insert into sys_role_menu values ('2', '2019');
insert into sys_role_menu values ('2', '2020');
insert into sys_role_menu values ('2', '2021');
insert into sys_role_menu values ('2', '2022');
insert into sys_role_menu values ('2', '2023');
insert into sys_role_menu values ('2', '2024');
insert into sys_role_menu values ('2', '2025');
insert into sys_role_menu values ('2', '2026');
insert into sys_role_menu values ('2', '2100');
insert into sys_role_menu values ('2', '2101');
insert into sys_role_menu values ('2', '2102');
insert into sys_role_menu values ('2', '2103');
insert into sys_role_menu values ('2', '2104');
insert into sys_role_menu values ('2', '2105');
insert into sys_role_menu values ('2', '2106');
insert into sys_role_menu values ('2', '2107');
insert into sys_role_menu values ('2', '2108');
insert into sys_role_menu values ('2', '2109');
insert into sys_role_menu values ('2', '2110');
insert into sys_role_menu values ('2', '2111');
insert into sys_role_menu values ('2', '2112');
insert into sys_role_menu values ('2', '2113');
insert into sys_role_menu values ('2', '2114');
insert into sys_role_menu values ('2', '2115');
insert into sys_role_menu values ('2', '2116');
insert into sys_role_menu values ('2', '2117');
insert into sys_role_menu values ('2', '2118');
insert into sys_role_menu values ('2', '2119');
insert into sys_role_menu values ('2', '2120');
insert into sys_role_menu values ('2', '2121');
insert into sys_role_menu values ('2', '2122');
insert into sys_role_menu values ('2', '2123');
insert into sys_role_menu values ('2', '2124');
insert into sys_role_menu values ('2', '2125');
insert into sys_role_menu values ('2', '2126');
insert into sys_role_menu values ('2', '2127');
insert into sys_role_menu values ('2', '2128');
insert into sys_role_menu values ('2', '2129');
insert into sys_role_menu values ('2', '2130');
insert into sys_role_menu values ('2', '2131');
insert into sys_role_menu values ('2', '2132');
insert into sys_role_menu values ('2', '2133');
insert into sys_role_menu values ('2', '2134');
insert into sys_role_menu values ('2', '2135');
insert into sys_role_menu values ('2', '2136');
insert into sys_role_menu values ('2', '2137');
insert into sys_role_menu values ('2', '2138');
insert into sys_role_menu values ('2', '2139');
insert into sys_role_menu values ('2', '2140');


-- ----------------------------
-- 初始化-虚拟仿真实训演示数据
-- ----------------------------
insert into vt_lab values(100, '智能制造虚拟仿真实训室', '数字工程学院', '双高校区A座301', 48, '0', '周老师', '13800000001', '面向智能制造、工业机器人和数字孪生方向开放的综合实训室。', 'admin', sysdate(), '', null, '核心实训场所');
insert into vt_lab values(101, '护理急救虚拟仿真实训室', '医护健康学院', '双高校区B座205', 36, '0', '李老师', '13800000002', '提供急救护理、临床处置和团队协同训练环境。', 'admin', sysdate(), '', null, '');
insert into vt_lab values(102, '新能源汽车虚拟仿真实训室', '交通工程学院', '产教融合中心2层', 42, '2', '王老师', '13800000003', '用于新能源汽车诊断、动力电池安全和维修流程仿真。', 'admin', sysdate(), '', null, '');

insert into vt_device values(100, 'VR一体化训练终端', 'VT-VR-2026-001', 100, 'VR终端', '0', '0', sysdate(), 'admin', sysdate(), '', null, '资源中心展示设备');
insert into vt_device values(101, '工业机器人仿真工作站', 'VT-ROBOT-2026-002', 100, '仿真工作站', '0', '0', sysdate(), 'admin', sysdate(), '', null, '');
insert into vt_device values(102, '急救流程交互模拟台', 'VT-MED-2026-003', 101, '交互模拟台', '0', '1', sysdate(), 'admin', sysdate(), '', null, '');
insert into vt_device values(103, '动力电池安全仿真台', 'VT-NEV-2026-004', 102, '仿真台', '1', '1', sysdate(), 'admin', sysdate(), '', null, '维护中');

insert into vt_resource values(100, '工业机器人拆装虚拟仿真实训资源', '0', '智能制造', '工业机器人技术', '', '/profile/upload/robot-vr.zip', '0', 1268, 96, '覆盖工业机器人结构认知、拆装步骤和故障诊断。', 'admin', sysdate(), '', null, '门户推荐资源');
insert into vt_resource values(101, '心肺复苏标准流程视频课程', '1', '护理', '急救护理', '', '/profile/upload/cpr.mp4', '0', 842, 73, '演示心肺复苏标准动作、评分点和常见错误。', 'admin', sysdate(), '', null, '');
insert into vt_resource values(102, '新能源汽车动力电池安全操作文档', '3', '新能源汽车', '动力电池维护', '', '/profile/upload/battery-guide.pdf', '1', 536, 42, '面向动力电池检测、绝缘防护和安全处置的操作说明。', 'admin', sysdate(), '', null, '');
insert into vt_resource values(103, '跨境电商仓储调度虚拟仿真', '0', '现代商贸', '智慧仓储管理', '', '/profile/upload/warehouse-vr.zip', '0', 710, 58, '模拟跨境仓储、订单拣选和物流调度。', 'admin', sysdate(), '', null, '');

insert into vt_course values(100, '工业机器人综合实训', '智能制造', 64, '陈老师', '0', '围绕机器人认知、编程、调试和故障诊断组织实训。', 'admin', sysdate(), '', null, '');
insert into vt_course values(101, '急救护理虚拟实训', '医护健康', 48, '赵老师', '0', '以场景化仿真提升急救护理流程掌握程度。', 'admin', sysdate(), '', null, '');
insert into vt_course values(102, '新能源汽车检测实训', '交通工程', 56, '孙老师', '0', '面向新能源汽车三电系统检测与安全操作。', 'admin', sysdate(), '', null, '');

insert into vt_experiment values(100, '工业机器人末端执行器拆装实验', 100, 100, '2', 90, '0', '在虚拟环境中完成末端执行器识别、拆装和调试。', 'admin', sysdate(), '', null, '');
insert into vt_experiment values(101, '心肺复苏急救流程实验', 101, 101, '1', 45, '0', '通过视频和交互流程完成心肺复苏训练。', 'admin', sysdate(), '', null, '');
insert into vt_experiment values(102, '动力电池高压安全检测实验', 102, 102, '3', 80, '1', '模拟动力电池高压检测与异常处置流程。', 'admin', sysdate(), '', null, '');

insert into vt_teaching_plan values(100, '2026春季智能制造综合实训计划', 100, 100, '智能制造2301班', date_add(sysdate(), interval -5 day), date_add(sysdate(), interval 10 day), '1', '周老师', 'admin', sysdate(), '', null, '');
insert into vt_teaching_plan values(101, '护理急救开放共享训练计划', 101, 101, '社会培训班A组', date_add(sysdate(), interval -12 day), date_add(sysdate(), interval -2 day), '2', '李老师', 'admin', sysdate(), '', null, '');
insert into vt_teaching_plan values(102, '新能源汽车检测预备计划', 102, 102, '新能源2402班', date_add(sysdate(), interval 3 day), date_add(sysdate(), interval 20 day), '0', '王老师', 'admin', sysdate(), '', null, '');

insert into vt_training_record values(100, 100, '张明', '20230001', date_add(sysdate(), interval -4 day), date_add(sysdate(), interval -4 day), '1', 92.50, 78, '操作步骤完整，诊断结果准确。', 'admin', sysdate(), '', null, '');
insert into vt_training_record values(101, 100, '刘佳', '20230002', date_add(sysdate(), interval -3 day), date_add(sysdate(), interval -3 day), '1', 88.00, 82, '拆装过程规范，调试记录较完整。', 'admin', sysdate(), '', null, '');
insert into vt_training_record values(102, 101, '王磊', 'OPEN0001', date_add(sysdate(), interval -8 day), date_add(sysdate(), interval -8 day), '1', 95.00, 38, '急救流程熟练，节奏控制较好。', 'admin', sysdate(), '', null, '');
insert into vt_training_record values(103, 101, '陈晨', 'OPEN0002', date_add(sysdate(), interval -7 day), null, '0', 0, 0, '训练未完成。', 'admin', sysdate(), '', null, '');

insert into vt_share_apply values(100, '黄先生', '13900000001', '某职业技术学院', '0', 100, '工业机器人拆装虚拟仿真实训资源', date_add(sysdate(), interval 2 day), 12, '0', '', 'admin', sysdate(), '', null, '门户提交演示');
insert into vt_share_apply values(101, '林女士', '13900000002', '区域产教联合体', '1', 101, '护理急救虚拟仿真实训室', date_add(sysdate(), interval -3 day), 20, '1', '同意开放半天实训时段。', 'admin', sysdate(), '', null, '');
insert into vt_share_apply values(102, '郑老师', '13900000003', '兄弟院校', '0', 102, '新能源汽车动力电池安全操作文档', date_add(sysdate(), interval -1 day), 8, '2', '资源当前仅校内开放，请补充合作证明。', 'admin', sysdate(), '', null, '');
