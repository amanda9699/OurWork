CREATE DATABASE IF NOT EXISTS bet_platform;
USE bet_platform;

CREATE TABLE sys_user (
    id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    username    VARCHAR(20) NOT NULL,
    password    VARCHAR(100) NOT NULL,
    nickname    VARCHAR(30) NOT NULL,
    avatar      VARCHAR(255) NULL,
    balance     INT NOT NULL DEFAULT 1000 COMMENT '可用余额',
    frozen      INT NOT NULL DEFAULT 0 COMMENT '冻结金额',
    is_deleted  TINYINT NOT NULL DEFAULT 0,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB COMMENT='用户表';

CREATE TABLE bet_contract (
    id                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id             BIGINT UNSIGNED NOT NULL,
    title               VARCHAR(50) NOT NULL,
    description         VARCHAR(500) NULL,
    category            VARCHAR(20) NOT NULL DEFAULT 'OTHER',
    deposit             INT NOT NULL COMMENT '押金',
    status              VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
                        COMMENT 'ACTIVE/VOTING/SUCCESS/FAILED',
    deadline            DATETIME NOT NULL,
    settled_at          DATETIME NULL,
    settle_result       VARCHAR(20) NULL,
    proof_content       VARCHAR(500) NULL,
    proof_images        JSON NULL,
    proof_submitted_at  DATETIME NULL,
    view_count          INT NOT NULL DEFAULT 0,
    is_deleted          TINYINT NOT NULL DEFAULT 0,
    created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_user (user_id),
    KEY idx_status_deadline (status, deadline),
    KEY idx_created (created_at)
) ENGINE=InnoDB COMMENT='对赌合约表';

CREATE TABLE bet_contract_witness (
    id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    contract_id BIGINT UNSIGNED NOT NULL,
    user_id     BIGINT UNSIGNED NOT NULL,
    accepted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_contract_user (contract_id, user_id),
    KEY idx_user (user_id)
) ENGINE=InnoDB COMMENT='合约见证人表';

CREATE TABLE bet_vote (
    id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    contract_id BIGINT UNSIGNED NOT NULL,
    user_id     BIGINT UNSIGNED NOT NULL,
    verdict     VARCHAR(10) NOT NULL COMMENT 'SUCCESS/FAIL',
    comment     VARCHAR(200) NULL,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_contract_user (contract_id, user_id),
    KEY idx_contract (contract_id)
) ENGINE=InnoDB COMMENT='见证人投票表';

CREATE TABLE acc_transaction (
    id            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id       BIGINT UNSIGNED NOT NULL,
    contract_id   BIGINT UNSIGNED NULL,
    type          VARCHAR(20) NOT NULL
                  COMMENT 'GIFT/FREEZE/UNFREEZE/DEDUCT',
    amount        INT NOT NULL
                  COMMENT '变动金额，正数，方向由type决定',
    balance_after INT NOT NULL COMMENT '变动后可用余额',
    frozen_after  INT NOT NULL COMMENT '变动后冻结金额',
    remark        VARCHAR(100) NULL,
    created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_user_time (user_id, created_at),
    KEY idx_contract (contract_id)
) ENGINE=InnoDB COMMENT='资金流水表（只增不改）';

CREATE TABLE sys_notification (
    id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id     BIGINT UNSIGNED NOT NULL,
    type        VARCHAR(30) NOT NULL,
    title       VARCHAR(100) NOT NULL,
    contract_id BIGINT UNSIGNED NULL,
    is_read     TINYINT NOT NULL DEFAULT 0,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_user_read (user_id, is_read)
) ENGINE=InnoDB COMMENT='站内通知表';



