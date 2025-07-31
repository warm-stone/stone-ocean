package com.example.stoneocean.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.stoneocean.entity.RankList;
import com.example.stoneocean.entity.RankMember;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author warmstone
 * @since 2025-07-31
 */
public interface RankMemberMapper extends BaseMapper<RankMember> {

    List<RankList> getUserOrderPage1(@Param("userNo") String userNo,
                                     @Param("current") Integer current, @Param("size") Integer size);
}
