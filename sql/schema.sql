-- =============================================================
-- 微承诺对赌平台 · 数据库初始化脚本（MySQL 8.0）
-- 与《doc/微承诺对赌平台-数据库设计文档.md》第 3 章一一对应
-- 用法：mysql -u root -p --default-character-set=utf8mb4 < sql/schema.sql
-- 注意：必须加 --default-character-set=utf8mb4（中文 Windows 下不加会导致注释乱码）
-- 金额单位为虚拟币整数；资金流水只新增，不提供 UPDATE / DELETE。
-- =============================================================

CREATE DATABASE IF NOT EXISTS bet_platform
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE bet_platform;
SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS sys_user (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  username   VARCHAR(20)  NOT NULL COMMENT '登录名，4~20 位',
  password   VARCHAR(100) NOT NULL COMMENT 'BCrypt 加密密码',
  nickname   VARCHAR(30)  NOT NULL COMMENT '昵称',
  avatar     VARCHAR(255) NULL COMMENT '头像 URL',
  balance    INT NOT NULL DEFAULT 1000 COMMENT '可用余额',
  frozen     INT NOT NULL DEFAULT 0 COMMENT '冻结金额',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 否，1 是',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

CREATE TABLE IF NOT EXISTS bet_contract (
  id                 BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id            BIGINT UNSIGNED NOT NULL COMMENT '创建者 ID',
  title              VARCHAR(50)  NOT NULL COMMENT '目标标题',
  description        VARCHAR(500) NULL COMMENT '目标描述/验收标准',
  category           VARCHAR(20)  NOT NULL DEFAULT 'OTHER' COMMENT 'STUDY/FITNESS/LIFE/OTHER',
  deposit            INT NOT NULL COMMENT '押金',
  status             VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE/VOTING/SUCCESS/FAILED',
  deadline           DATETIME NOT NULL COMMENT '截止时间',
  settled_at         DATETIME NULL COMMENT '结算时间',
  settle_result      VARCHAR(20) NULL COMMENT '结算结果：SUCCESS/FAILED',
  proof_content      VARCHAR(500) NULL COMMENT '履约凭证文字',
  proof_images       JSON NULL COMMENT '凭证图片 URL 数组',
  proof_submitted_at DATETIME NULL COMMENT '凭证提交时间',
  view_count         INT NOT NULL DEFAULT 0 COMMENT '围观数',
  is_deleted         TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 否，1 是',
  created_at         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_user (user_id),
  KEY idx_status_deadline (status, deadline),
  KEY idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='对赌合约表';

CREATE TABLE IF NOT EXISTS bet_contract_witness (
  id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  contract_id BIGINT UNSIGNED NOT NULL COMMENT '合约 ID',
  user_id     BIGINT UNSIGNED NOT NULL COMMENT '见证人用户 ID',
  accepted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '接受邀请时间',
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_contract_user (contract_id, user_id),
  KEY idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='合约见证人表';

CREATE TABLE IF NOT EXISTS bet_vote (
  id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  contract_id BIGINT UNSIGNED NOT NULL COMMENT '合约 ID',
  user_id     BIGINT UNSIGNED NOT NULL COMMENT '投票人用户 ID',
  verdict     VARCHAR(10) NOT NULL COMMENT '投票结果：SUCCESS/FAIL',
  comment     VARCHAR(200) NULL COMMENT '评语',
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '投票时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_contract_user (contract_id, user_id),
  KEY idx_contract (contract_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='见证人投票表';

CREATE TABLE IF NOT EXISTS acc_transaction (
  id            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id       BIGINT UNSIGNED NOT NULL COMMENT '用户 ID',
  contract_id   BIGINT UNSIGNED NULL COMMENT '关联合约，GIFT 类型为 NULL',
  type          VARCHAR(20) NOT NULL COMMENT 'GIFT/FREEZE/UNFREEZE/DEDUCT',
  amount        INT NOT NULL COMMENT '变动金额，存正数，方向由 type 决定',
  balance_after INT NOT NULL COMMENT '变动后可用余额',
  frozen_after  INT NOT NULL COMMENT '变动后冻结金额',
  remark        VARCHAR(100) NULL COMMENT '摘要',
  created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_user_time (user_id, created_at),
  KEY idx_contract (contract_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资金流水表（只增不改）';

CREATE TABLE IF NOT EXISTS sys_notification (
  id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id     BIGINT UNSIGNED NOT NULL COMMENT '接收人用户 ID',
  type        VARCHAR(30)  NOT NULL COMMENT 'WITNESS_INVITE/VERDICT_REQUEST/CONTRACT_SETTLED/WITNESS_ACCEPTED',
  title       VARCHAR(100) NOT NULL COMMENT '通知文案',
  contract_id BIGINT UNSIGNED NULL COMMENT '关联合约',
  is_read     TINYINT NOT NULL DEFAULT 0 COMMENT '是否已读：0 否，1 是',
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_user_read (user_id, is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='站内通知表';
