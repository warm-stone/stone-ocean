package com.example.stoneocean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
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
    private LocalDateTime expiresTime;

    private Long creator;
    private LocalDateTime createdTime;
    private Long modifier;
    private LocalDateTime updatedTime;
    @ApiModelProperty("删除时间，NULL 表示未删除")
    private LocalDateTime deletedTime;

}
