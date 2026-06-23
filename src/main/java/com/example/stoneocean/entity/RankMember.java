package com.example.stoneocean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author warmstone
 * @since 2025-07-31
 */
@Data
@TableName("t_vote4fun_rank_member")
@ApiModel(value = "RankMember对象", description = "")
public class RankMember implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long rankListId;

    private Long parentId;

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

    private Long creator;
    private LocalDateTime createdTime;
    private Long modifier;

    private LocalDateTime updatedTime;

    /**
     * 删除时间，NULL 表示未删除
     */
    @ApiModelProperty("删除时间，NULL 表示未删除")
    private LocalDateTime deletedTime;

    @TableField(exist = false)
    private List<RankMember> children;

}
