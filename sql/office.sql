-- ----------------------------
-- 1、员工信息表
-- ----------------------------
drop table if exists off_staff;
create table off_staff (
                          staf_id           bigint(20)      not null auto_increment    comment '员工ID',
                          realname          varchar(30)     not null                   comment '姓名',
                          nickname          varchar(30)     not null                   comment '昵称',
                          sex               char(1)         default '0'                comment '用户性别（0男 1女 2未知）',
                          birth_date        datetime                                   comment '出生年月',
                          id_no             varchar(18)     default ''                 comment '身份证号',
                          mobile            varchar(11)     default ''                 comment '联系电话',
                          email             varchar(50)     default ''                 comment '电子邮箱',
                          qq_no             varchar(20)     default ''                 comment 'QQ号码',
                          wechat            varchar(20)     default ''                 comment '微信',
                          politics          varchar(2)      default '00'               comment '政治面貌（00群众）',
                          national          varchar(20)     default ''                 comment '民族',
                          home_address      varchar(100)    default ''                 comment '家庭住址',
                          emergency_contact varchar(50)     default ''                 comment '紧急联系人',
                          graduate          varchar(50)     default ''                 comment '毕业院校',
                          educate           varchar(2)      default ''                 comment '学历',
                          work_date         datetime                                   comment '工作年份',
                          induction_date    datetime                                   comment '入职时间',
                          departure_date    datetime                                   comment '离职时间',
                          photo             varchar(100)    default ''                 comment '照片路径',
                          del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
                          create_by         varchar(64)     default ''                 comment '创建者',
                          create_time       datetime                                   comment '创建时间',
                          update_by         varchar(64)     default ''                 comment '更新者',
                          update_time       datetime                                   comment '更新时间',
                          remark            varchar(500)    default null               comment '备注',
                          primary key (staf_id)
) engine=innodb auto_increment=1 comment = '员工信息表';