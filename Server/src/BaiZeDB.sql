DROP DATABASE IF EXISTS BaiZeDB;
create database BaiZeDB;
use BaiZeDB;

-- 创建用户表
DROP TABLE IF EXISTS User;
create table User
(
    UID        int AUTO_INCREMENT COMMENT 'UID',
    username   varchar(20) DEFAULT NULL COMMENT '用户名',
    password   char(64)    DEFAULT NULL COMMENT '密码',
    email      varchar(30) NOT NULL COMMENT '电子邮件',
    phone      varchar(20) DEFAULT NULL COMMENT '手机',
    IDCard     varchar(20) DEFAULT NULL COMMENT '身份证号',
    realName   varchar(20) DEFAULT NULL COMMENT '真实姓名',
    passwdSalt char(16)    DEFAULT NULL COMMENT '密码盐',
    isOpenGame char        DEFAULT '0' COMMENT '是否开通游戏账号',
    isOpenDisk char        DEFAULT '0' COMMENT '是否开通网盘账号',
    createTime datetime    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT PriKeyUid PRIMARY KEY (UID),
    CONSTRAINT UniqueEmail UNIQUE (email)
) COMMENT '用户';
alter table
    User
    AUTO_INCREMENT = 10000000;

-- 创建用户网盘数据表
# 用来记录用户所有网盘相关的数据
DROP TABLE IF EXISTS DiskData;
create table DiskData
(
    DID        int AUTO_INCREMENT COMMENT 'DID',
    UID        int COMMENT '用户ID',
    grade      int      DEFAULT 1 COMMENT '会员等级',
    outOfDate  int      DEFAULT 30 COMMENT '过期时间',
    nowStorage bigint   DEFAULT 0 COMMENT '当前存储',
    maxStorage bigint   DEFAULT 1073741824 COMMENT '最大存储',
    createTime datetime DEFAULT CURRENT_TIMESTAMP COMMENT '开通时间',
    CONSTRAINT PriKeyUid PRIMARY KEY (DID),
    foreign key (UID) references User (UID)
) COMMENT '网盘数据';

-- 创建用户网盘数据表
# 用来记录用户所有与游戏相关的数据
DROP TABLE IF EXISTS GameData;
create table GameData
(
    GID      int AUTO_INCREMENT COMMENT 'GID',
    UID      int COMMENT '用户ID',
    username varchar(20) DEFAULT ' ' NOT NULL COMMENT '游戏id',
    CONSTRAINT PriKeyUid PRIMARY KEY (GID),
    foreign key (UID) references User (UID)
) COMMENT '游戏数据';

-- 创建文件表
DROP TABLE IF EXISTS UserFile;
create table UserFile
(
    UFID       int AUTO_INCREMENT COMMENT 'UFID',
    UID        int COMMENT '用户ID',
    fileDir    varchar(500) DEFAULT ''  NOT NULL COMMENT '文件目录',
    fileName   varchar(50)  DEFAULT ''  NOT NULL COMMENT '文件名字',
    realPath   char(40)     DEFAULT ''  NOT NULL COMMENT '真实路径',
    fileType   char(1)      DEFAULT '-' NOT NULL COMMENT '文件类型',
    fileSize   int          DEFAULT 0   NOT NULL COMMENT '文件大小',
    fileState  char(1)      DEFAULT 'Y' NOT NULL COMMENT '文件状态',
    deleteTime datetime     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '删除时间',
    # TODO: 目前认为只要更新就是改fileState 因此同步修改删除时间

    CONSTRAINT PriKeyFid PRIMARY KEY (UFID),
    CONSTRAINT UniqueName UNIQUE (UID, fileDir, fileName),
    foreign key (UID) references User (UID)
) COMMENT '普通文件';

-- 创建管理员
DROP TABLE IF EXISTS Admin;
create table Admin
(
    AID         int AUTO_INCREMENT COMMENT 'AID',
    username    varchar(20) DEFAULT ' ' NOT NULL COMMENT '用户名',
    password    varchar(30) DEFAULT ' ' NOT NULL COMMENT '密码',
    email       varchar(30) DEFAULT ' ' NOT NULL COMMENT '电子邮件',
    permissions char(1)     DEFAULT '0' NOT NULL COMMENT '权限',
    CONSTRAINT PriKeyAid PRIMARY KEY (AID)
) COMMENT '管理员';
alter table
    Admin
    AUTO_INCREMENT = 10000;

-- 创建共享文件
DROP TABLE IF EXISTS ShareFile;
create table ShareFile
(
    SFID       int AUTO_INCREMENT COMMENT 'SFID',
    fileName   varchar(50) DEFAULT ' ' NOT NULL COMMENT '文件名',
    password   varchar(30) DEFAULT ' ' NOT NULL COMMENT '密码',
    createTime datetime    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT PriKeySFid PRIMARY KEY (SFID)
) COMMENT '共享文件';

-- 创建好友
DROP TABLE IF EXISTS Friend;
create table Friend
(
    NID        int AUTO_INCREMENT COMMENT 'NID',
    UID1       int COMMENT '用户1',
    UID2       int COMMENT '用户2',
    group1     varchar(20) DEFAULT '默认分组' COMMENT '用户1中 用户2的身份',
    group2     varchar(20) DEFAULT '默认分组' COMMENT '用户2中 用户1的身份',
    createTime datetime    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT PriKeyNid PRIMARY KEY (NID),
    foreign key (UID1) references User (UID),
    foreign key (UID2) references User (UID)
) COMMENT '好友';

-- 创建 verify
DROP TABLE IF EXISTS Verify;
create table Verify
(
    VID        int AUTO_INCREMENT COMMENT 'VID',
    email      varchar(30) DEFAULT ' ' NOT NULL COMMENT '电子邮件',
    verifyCode varchar(6)  DEFAULT ' ' NOT NULL COMMENT '验证码',
    createTime datetime    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT PriKeyNid PRIMARY KEY (VID)
) COMMENT '验证码';

-- 创建定时事件 清除过期验证码
create event clearVerifyOver5min on schedule every 60 second
    STARTS '2022-01-01 00:00:00'
    do
    delete
    from Verify
    where createTime < DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 300 second);

# -- 创建一个存储过程
# create procedure setNowStorage(in ID int, in Storage int)
# BEGIN
#     update DiskData set nowStorage = Storage where uid = ID;
# END;
#
# call setNowStorage(10000000, 0);
# commit;

# # 关闭事件  clearVerifyOver5min ---> eventName
# alter event clearVerifyOver5min disable;
# # 开启事件
# alter event clearVerifyOver5min enable;
# # 删除事件
# drop event if exists clearVerifyOver5min;
# # 查看事件
# show events;

# insert into Verify (email, verifyCode) value ('192176794@qq.com', '123456');
# insert into Verify (email, verifyCode) value ('164613454@qq.com', '123456');