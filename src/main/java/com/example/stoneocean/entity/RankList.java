package com.example.stoneocean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Setter;

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
@Data
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
    @NotNull
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
    private Long creator;

    private LocalDateTime createdTime;

    private Long modifier;
    private LocalDateTime updatedTime;

    /**
     * 删除时间，NULL 表示未删除
     */
    @ApiModelProperty("删除时间，NULL 表示未删除")
    private LocalDateTime deletedTime;

}
