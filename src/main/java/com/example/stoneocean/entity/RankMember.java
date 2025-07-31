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
@TableName("t_vote4fun_rank_member")
@ApiModel(value = "RankMember对象", description = "")
public class RankMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long rankListId;

    private Long createUserId;

    /**
     * 票数计数-总
     */
    @ApiModelProperty("票数计数-总")
    private Long scoreSum;

    /**
     * 票数计数-规则计算
     */
    @ApiModelProperty("票数计数-规则计算")
    private Long scoreCalculate;

    /**
     * 投票项目名称
     */
    @ApiModelProperty("投票项目名称")
    private String name;

    private String description;

    /**
     * 封面图
     */
    @ApiModelProperty("封面图")
    private String coverUrl;

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

    public Long getRankListId() {
        return rankListId;
    }

    public void setRankListId(Long rankListId) {
        this.rankListId = rankListId;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getScoreSum() {
        return scoreSum;
    }

    public void setScoreSum(Long scoreSum) {
        this.scoreSum = scoreSum;
    }

    public Long getScoreCalculate() {
        return scoreCalculate;
    }

    public void setScoreCalculate(Long scoreCalculate) {
        this.scoreCalculate = scoreCalculate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
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
        return "RankMember{" +
            "id = " + id +
            ", rankListId = " + rankListId +
            ", createUserId = " + createUserId +
            ", scoreSum = " + scoreSum +
            ", scoreCalculate = " + scoreCalculate +
            ", name = " + name +
            ", description = " + description +
            ", coverUrl = " + coverUrl +
            ", createdAt = " + createdAt +
            ", updatedAt = " + updatedAt +
            ", deletedAt = " + deletedAt +
            "}";
    }
}
