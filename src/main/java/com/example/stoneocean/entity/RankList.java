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
@TableName("t_vote4fun_rank_list")
@ApiModel(value = "RankList对象", description = "")
public class RankList implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 榜单标题
     */
    @ApiModelProperty("榜单标题")
    private String title;

    /**
     * 榜单描述
     */
    @ApiModelProperty("榜单描述")
    private String description;

    /**
     * 封面图
     */
    @ApiModelProperty("封面图")
    private String coverUrl;

    /**
     * 投票操作的显示名称
     */
    @ApiModelProperty("投票操作的显示名称")
    private String agreeName;

    /**
     * 反对操作的显示名称
     */
    @ApiModelProperty("反对操作的显示名称")
    private String disagreeName;

    /**
     * 创建者ID
     */
    @ApiModelProperty("创建者ID")
    private Long createdBy;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getAgreeName() {
        return agreeName;
    }

    public void setAgreeName(String agreeName) {
        this.agreeName = agreeName;
    }

    public String getDisagreeName() {
        return disagreeName;
    }

    public void setDisagreeName(String disagreeName) {
        this.disagreeName = disagreeName;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
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
        return "RankList{" +
            "id = " + id +
            ", title = " + title +
            ", description = " + description +
            ", coverUrl = " + coverUrl +
            ", agreeName = " + agreeName +
            ", disagreeName = " + disagreeName +
            ", createdBy = " + createdBy +
            ", createdAt = " + createdAt +
            ", updatedAt = " + updatedAt +
            ", deletedAt = " + deletedAt +
            "}";
    }
}
