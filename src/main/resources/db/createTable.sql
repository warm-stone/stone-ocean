CREATE DATABASE stone_ocean;
use stone_ocean;

############################################
# 用户
DROP TABLE IF EXISTS `t_user`;

CREATE TABLE t_user
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_account  VARCHAR(50) UNIQUE NOT NULL COMMENT '登录账号',
    password_hash VARCHAR(255)       NOT NULL COMMENT '密码哈希',
    email         VARCHAR(100) UNIQUE,
    phone         VARCHAR(32),
    nickname      VARCHAR(50) UNIQUE NOT NULL DEFAULT '' COMMENT '用户显示名',
    sex           VARCHAR(16),
    des           varchar(128),
    avatar_url    VARCHAR(255),
    created_at    DATETIME                    DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME                    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at    DATETIME COMMENT '删除时间，NULL 表示未删除'
);
############################################
# 简历
CREATE TABLE t_experience
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT, -- 主键
    biographicId INT,
    title        VARCHAR(32),
    exp          VARCHAR(1024),
    ord          INT
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
    skill           varchar(1024)

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
    created_by    BIGINT       NOT NULL COMMENT '创建者ID',

    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at    DATETIME COMMENT '删除时间，NULL 表示未删除'

);

# 投票项表
CREATE TABLE t_vote4fun_rank_member
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    rank_list_id    BIGINT NOT NULL,
    create_user_id  BIGINT NOT NULL,
    score_sum       BIGINT COMMENT '票数计数-总',
    score_calculate BIGINT COMMENT '票数计数-规则计算',
    name            VARCHAR(1024) COMMENT '投票项目名称',
    description     VARCHAR(1024),
    cover_url       VARCHAR(255) COMMENT '封面图',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at      DATETIME COMMENT '删除时间，NULL 表示未删除'

);

CREATE INDEX idx_vote4fun_rank_list_id ON t_vote4fun_rank_member (rank_list_id);
CREATE INDEX idx_vote4fun_create_user_id ON t_vote4fun_rank_member (create_user_id);
-- 为 score_sum 创建降序索引（默认是升序，但排序方向对性能影响不大）
CREATE INDEX idx_vote4fun_score_sum ON t_vote4fun_rank_member (score_sum DESC);

-- 为 score_calculate 创建降序索引（常用）
CREATE INDEX idx_vote4fun_score_calculate ON t_vote4fun_rank_member (score_calculate DESC);

# 榜单通告
CREATE TABLE t_vote4fun_announcement
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    rank_id      BIGINT       NOT NULL,
    title        VARCHAR(100) NOT NULL,
    content      TEXT         NOT NULL,
    publisher_id BIGINT       NOT NULL COMMENT '发布者ID（必须是成员）',
    expires_at   DATETIME COMMENT '过期时间（可选）',

    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at      DATETIME COMMENT '删除时间，NULL 表示未删除'

);


show tables;