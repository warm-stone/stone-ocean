
############################################
# 用户

CREATE TABLE t_user
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    account       VARCHAR(50) UNIQUE NOT NULL COMMENT '登录账号',
    password_hash VARCHAR(255) COMMENT '密码哈希',
    email         VARCHAR(100) UNIQUE,
    phone         VARCHAR(32),
    nickname      VARCHAR(50) UNIQUE NOT NULL DEFAULT '' COMMENT '用户显示名',
    sex           VARCHAR(16),
    des           varchar(128),
    avatar_url    VARCHAR(255),
    created_time  DATETIME                    DEFAULT CURRENT_TIMESTAMP,
    updated_time  DATETIME                    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_time  DATETIME COMMENT '删除时间，NULL 表示未删除',
    token_version INT DEFAULT 0 COMMENT 'Token版号，用于令牌撤销'
);

# 三方账号
CREATE TABLE t_third_party_account
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id      BIGINT,
    third_id     VARCHAR(64),
    account_type VARCHAR(24),
    info         JSON,
    FOREIGN KEY idx_third_party_account_user_id (user_id) REFERENCES t_user (id),
    INDEX idx_third_party_account_third_id (third_id)

);
############################################
# 简历
CREATE TABLE t_experience
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT, -- 主键
    biographicId INT,
    title        VARCHAR(32),
    exp          VARCHAR(1024),
    ord          INT,

    creator      BIGINT NOT NULL COMMENT '创建者ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    modifier     BIGINT,
    updated_time DATETIME,
    deleted_time DATETIME COMMENT '删除时间，NULL 表示未删除'
);

CREATE TABLE t_biographical_notes
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    name            varchar(16),
    birthday        DATE,
    email           varchar(64),
    university      varchar(64),
    graduation_date DATE,
    sex             varchar(8),
    skill           varchar(1024),

    creator         BIGINT NOT NULL COMMENT '创建者ID',
    created_time    DATETIME DEFAULT CURRENT_TIMESTAMP,
    modifier        BIGINT,
    updated_time    DATETIME,
    deleted_time    DATETIME COMMENT '删除时间，NULL 表示未删除'

);

############################################
# 投票
# 榜单表
CREATE TABLE t_vote4fun_rank_list
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    title         VARCHAR(100) NOT NULL COMMENT '榜单标题',
    description   TEXT COMMENT '榜单描述',
    cover_url     VARCHAR(255) COMMENT '封面图',
    agree_name    VARCHAR(12) COMMENT '投票操作的显示名称',
    disagree_name VARCHAR(12) COMMENT '反对操作的显示名称',

    creator       BIGINT       NOT NULL COMMENT '创建者ID',
    created_time  DATETIME DEFAULT CURRENT_TIMESTAMP,
    modifier      BIGINT,
    updated_time  DATETIME,
    deleted_time  DATETIME COMMENT '删除时间，NULL 表示未删除'

);

# 投票项表
CREATE TABLE t_vote4fun_rank_member
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    rank_list_id    BIGINT,
    parent_id       BIGINT,
    score_sum       BIGINT DEFAULT 0 COMMENT   '票数计数-总',
    score_calculate BIGINT DEFAULT 0 COMMENT '票数计数-规则计算',
    name            VARCHAR(1024) COMMENT '投票项目名称',
    description     VARCHAR(1024),
    cover_url       VARCHAR(255) COMMENT '封面图',

    creator         BIGINT NOT NULL COMMENT '创建者ID',
    created_time    DATETIME DEFAULT CURRENT_TIMESTAMP,
    modifier        BIGINT,
    updated_time    DATETIME,
    deleted_time    DATETIME COMMENT '删除时间，NULL 表示未删除'
);

CREATE INDEX idx_vote4fun_rank_member_rank_list_id ON t_vote4fun_rank_member (rank_list_id);
CREATE INDEX idx_vote4fun_rank_member_parent_id ON t_vote4fun_rank_member (parent_id);
CREATE INDEX idx_vote4fun_rank_member_create_user_id ON t_vote4fun_rank_member (creator);


# 投票记录
CREATE TABLE t_vote4fun_vote_record
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    rank_member_id BIGINT NOT NULL,
    vote_count     INT    NOT NULL,

    creator        BIGINT NOT NULL COMMENT '创建者ID',
    created_time   DATETIME DEFAULT CURRENT_TIMESTAMP,
    modifier       BIGINT,
    updated_time   DATETIME,
    deleted_time   DATETIME COMMENT '删除时间，NULL 表示未删除',
    -- 投票日期：由 created_time 派生，配合下面的唯一约束防止并发首次投票时插入多条当日记录
    vote_date      DATE GENERATED ALWAYS AS (DATE(created_time)) STORED COMMENT '投票日期(派生自created_time)，用于唯一约束防并发重复投票',
    UNIQUE KEY uk_vote4fun_vote_record_member_creator_date (rank_member_id, creator, vote_date)
);
CREATE INDEX idx_vote4fun_rank_member_id ON t_vote4fun_vote_record (rank_member_id);
CREATE INDEX idx_vote4fun_creator ON t_vote4fun_vote_record (creator, created_time);
CREATE INDEX idx_vote4fun_vote_record_member_creator_time ON t_vote4fun_vote_record (rank_member_id, creator, created_time);

# 榜单通告
CREATE TABLE t_vote4fun_announcement
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    rank_id      BIGINT       NOT NULL,
    title        VARCHAR(100) NOT NULL,
    content      TEXT         NOT NULL,
    publisher_id BIGINT       NOT NULL COMMENT '发布者ID（必须是成员）',
    expires_time DATETIME COMMENT '过期时间（可选）',

    creator      BIGINT       NOT NULL COMMENT '创建者ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    modifier     BIGINT,
    updated_time DATETIME,
    deleted_time DATETIME COMMENT '删除时间，NULL 表示未删除'

);

CREATE INDEX idx_vote4fun_announcement_rank_id ON t_vote4fun_announcement (rank_id);




show tables;