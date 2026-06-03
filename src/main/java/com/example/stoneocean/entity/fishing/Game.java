package com.example.stoneocean.entity.fishing;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author warmstone
 * @since 2025-11-06
 */
@Data
@ToString
@TableName("t_fishing_game")
@ApiModel(value = "Game对象", description = "")
public class Game implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String prompt;

    private GameType type;

    /**
     * 游戏内容
     */
    @ApiModelProperty("游戏内容")
    private String content;

    /**
     * 游戏答案
     */

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty("游戏答案")
    private String answer;

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
