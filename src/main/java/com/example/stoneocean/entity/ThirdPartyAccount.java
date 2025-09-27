package com.example.stoneocean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author warmstone
 * @since 2025-08-19
 */
@Data
@Builder
@ToString
@TableName("t_third_party_account")
@ApiModel(value = "ThirdPartyAccount对象", description = "")
public class ThirdPartyAccount implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String thirdId;

    private String accountType;

    private String info;

}
