package com.example.stoneocean.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("t_vote4fun_vote_record")
public class VoteRecord implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long rankMemberId;
    private Integer voteCount;

    private Long creator;
    private LocalDateTime createdTime;
    private Long modifier;
    private LocalDateTime updatedTime;
    private LocalDateTime deletedTime;
}
