
/* Drop Tables */

/* Create Tables */

-- PROFILE_BRANCH
CREATE TABLE PROFILE_BRANCH
(
	-- 机构号
	ORG_ID VARCHAR(12) NOT NULL,
	-- 分支编码
	BRANCH_ID VARCHAR(9) NOT NULL,
	-- 上级分支
	SUPERIOR_ID VARCHAR(9),
	-- 分支名
	BRANCH_NAME VARCHAR(100) NOT NULL,
	-- 所属地区码
	ADDR_CODE VARCHAR(100),
	-- 地址
	ADDRESS VARCHAR(120),
	-- 区
	DISTRICT VARCHAR(60),
	-- 城市
	CITY VARCHAR(60),
	-- 联系电话1
	TELEPHONE1 VARCHAR(40),
	-- 联系电话2
	TELEPHONE2 VARCHAR(40),
	-- 乐观锁版本号
	JPA_VERSION INT NOT NULL,
	PRIMARY KEY (ORG_ID, BRANCH_ID)
);


-- 菜单表
CREATE TABLE PROFILE_MENU
(
	-- ID
	ID INT NOT NULL,
	-- 机构号
	ORG_ID VARCHAR(12),
	-- 应用代码 : 接入授权中心对应的client_id
	APP_CD VARCHAR(50),
	-- 菜单代码 : 菜单代码,不同app下可以相同
	MENU_CD VARCHAR(30) NOT NULL,
	-- 菜单名称
	MNAME VARCHAR(100) NOT NULL,
	-- 菜单路径
	PATH_URL VARCHAR(500) NOT NULL,
	-- 上级菜单ID : 0表示顶级菜单
	PARENT_ID INT DEFAULT 0 NOT NULL,
	-- 序号
	SORTN INT NOT NULL,
	-- 图标路径
	ICON VARCHAR(255),
	-- 修改时间
	MTN_TIMESTAMP TIMESTAMP,
	-- 修改用户
	MTN_USER VARCHAR(40),
	-- 乐观锁版本号
	JPA_VERSION INT NOT NULL,
	PRIMARY KEY (ID),
	CONSTRAINT UNI_APP_MENU UNIQUE (APP_CD, MENU_CD)
);


-- 接口表
CREATE TABLE PROFILE_MENU_INTERF
(
	-- ID
	ID INT NOT NULL,
	-- 应用代码 : 接入授权中心对应的client_id
	APP_CD VARCHAR(50),
	-- 接口代码 : 作为Auth的权限标识，以及前端的接口标识，不同app下可以相同
	INTERF_CD VARCHAR(50) NOT NULL,
	-- 接口名称
	INAME VARCHAR(100) NOT NULL,
	-- 菜单ID : 接口代码所属菜单ID
	MENU_ID INT NOT NULL,
	-- 修改时间
	MTN_TIMESTAMP TIMESTAMP,
	-- 修改用户
	MTN_USER VARCHAR(40),
	-- 乐观锁版本号
	JPA_VERSION INT NOT NULL,
	PRIMARY KEY (ID),
	CONSTRAINT UNI_APP_INTERF UNIQUE (APP_CD, INTERF_CD)
);


-- 密码维护历史表 : 记录每个用户的密码历史，以便判断密码重复。
CREATE TABLE PROFILE_PWD_HIST
(
	-- ID
	ID INT NOT NULL,
	-- PU_ID : ###uuid2###
	PU_ID VARCHAR(64) NOT NULL,
	-- 密码
	PASSWORD VARCHAR(300) NOT NULL,
	-- 密码建立时间
	PWD_CRE_TIME TIMESTAMP DEFAULT NOW() NOT NULL,
	-- 乐观锁版本号
	JPA_VERSION INT NOT NULL,
	PRIMARY KEY (ID)
);


-- 角色定义表
CREATE TABLE PROFILE_ROLE
(
	-- 角色ID
	ROLE_ID VARCHAR(20) NOT NULL,
	-- 机构号
	ORG_ID VARCHAR(12) NOT NULL,
	-- 应用代码 : 接入授权中心对应的client_id
	CLIENT_ID VARCHAR(50),
	-- 应用代码 : 接入授权中心对应的client_id
	APP_CD VARCHAR(64),
	-- 分支编码
	BRANCH_ID VARCHAR(9) NOT NULL,
	-- 角色名
	ROLE_NAME VARCHAR(200) NOT NULL,
	-- 乐观锁版本号
	JPA_VERSION INT NOT NULL,
	PRIMARY KEY (ROLE_ID)
);


-- 权限表
CREATE TABLE PROFILE_ROLE_AUTH
(
	-- 角色ID
	ROLE_ID VARCHAR(20) NOT NULL,
	-- 权限标识
	AUTHORITY VARCHAR(100) NOT NULL,
	-- 权限URI
	AUTU_URI VARCHAR(500) NOT NULL,
	PRIMARY KEY (ROLE_ID, AUTHORITY)
);


-- 用户安全操作日志
CREATE TABLE PROFILE_SECOPER_LOG
(
	-- 日志序号
	LOG_ID INT NOT NULL,
	-- PU_ID : ###uuid2###
	PU_ID VARCHAR(64) NOT NULL,
	-- 被操作用户ID
	BEOPERATED_ID VARCHAR(64),
	-- 操作业务类型 : ///
	-- @net.engining.profile.enums.OperationType
	OPER_TYPE VARCHAR(2) NOT NULL,
	-- IP地址
	OPER_IP VARCHAR(30) NOT NULL,
	-- 操作时间
	OPER_TIME TIMESTAMP DEFAULT NOW() NOT NULL,
	-- 乐观锁版本号
	JPA_VERSION INT NOT NULL,
	PRIMARY KEY (LOG_ID)
);


-- 用户信息表
CREATE TABLE PROFILE_USER
(
	-- PU_ID : ###uuid2###
	PU_ID VARCHAR(64) NOT NULL,
	-- 机构号
	ORG_ID VARCHAR(12) NOT NULL,
	-- 分支编码
	BRANCH_ID VARCHAR(9),
	-- 登陆ID
	USER_ID VARCHAR(40) NOT NULL UNIQUE,
	-- 姓名
	NAME VARCHAR(40) NOT NULL,
	-- 密码
	PASSWORD VARCHAR(300) NOT NULL,
	-- 状态 : ///
	-- N|新增
	-- A|活动
	-- L|锁定
	STATUS VARCHAR(1) NOT NULL,
	-- EMAIL
	EMAIL VARCHAR(128),
	-- 密码过期日期
	PWD_EXP_DATE DATE,
	-- 密码错误次数
	PWD_TRIES INT NOT NULL,
	-- 修改时间
	MTN_TIMESTAMP TIMESTAMP DEFAULT NOW(),
	-- 修改用户
	MTN_USER VARCHAR(40),
	-- 乐观锁版本号
	JPA_VERSION INT NOT NULL,
	PRIMARY KEY (PU_ID)
);


-- 用户角色表
CREATE TABLE PROFILE_USER_ROLE
(
	-- ID
	ID INT NOT NULL,
	-- 角色ID
	ROLE_ID VARCHAR(20) NOT NULL,
	-- PU_ID : ###uuid2###
	PU_ID VARCHAR(64) NOT NULL,
	PRIMARY KEY (ID)
);



