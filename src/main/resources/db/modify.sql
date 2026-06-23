-- modify.sql: 增量 schema 变更脚本
-- 从原始 createTable.sql 升级到当前 schema 所需的 ALTER 语句
-- 执行前请备份数据库。按顺序执行。

############################################
# t_user: 令牌撤销 + RBAC（S2/S3）

ALTER TABLE t_user ADD COLUMN token_version INT DEFAULT 0 COMMENT 'Token版号，用于令牌撤销';
ALTER TABLE t_user ADD COLUMN role VARCHAR(32) DEFAULT 'USER' COMMENT '角色，默认 USER';

############################################
# t_vote4fun_vote_record: 防并发重复投票（S1）
# vote_date 为基于 created_time 的派生列(存储)，配合唯一约束防止并发首次投票时插入多条当日记录。
# 注意：添加 STORED 生成列前，若已有历史数据需保证 created_time 非空。

ALTER TABLE t_vote4fun_vote_record ADD COLUMN vote_date DATE GENERATED ALWAYS AS (DATE(created_time)) STORED COMMENT '投票日期(派生自created_time)，用于唯一约束防并发重复投票';
ALTER TABLE t_vote4fun_vote_record ADD UNIQUE KEY uk_vote4fun_vote_record_member_creator_date (rank_member_id, creator, vote_date);

############################################
# t_experience: 列名规范化（biographicId -> biographic_id）
# 与 MyBatis-Plus 默认下划线转驼峰映射保持一致。

ALTER TABLE t_experience CHANGE COLUMN biographicId biographic_id INT;
