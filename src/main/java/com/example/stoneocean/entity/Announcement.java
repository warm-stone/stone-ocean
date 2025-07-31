package com.example.stoneocean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author warmstone
 * @since 2025-07-31
 */
@TableName("t_vote4fun_announcement")
@ApiModel(value = "Announcement对象", description = "")
public class Announcement implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long rankId;

    private String title;

    private String content;

    /**
     * 发布者ID（必须是成员）
     */
    @ApiModelProperty("发布者ID（必须是成员）")
    private Long publisherId;

    /**
     * 过期时间（可选）
     */
    @ApiModelProperty("过期时间（可选）")
    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * 删除时间，NULL 表示未删除
     */
    @ApiModelProperty("删除时间，NULL 表示未删除")
    private LocalDateTime deletedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRankId() {
        return rankId;
    }

    public void setRankId(Long rankId) {
        this.rankId = rankId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public String toString() {
        return "Announcement{" +
            "id = " + id +
            ", rankId = " + rankId +
            ", title = " + title +
            ", content = " + content +
            ", publisherId = " + publisherId +
            ", expiresAt = " + expiresAt +
            ", createdAt = " + createdAt +
            ", updatedAt = " + updatedAt +
            ", deletedAt = " + deletedAt +
            "}";
    }
}
