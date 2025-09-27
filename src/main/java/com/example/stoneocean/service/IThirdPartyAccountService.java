package com.example.stoneocean.service;

import com.example.stoneocean.entity.ThirdPartyAccount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author warmstone
 * @since 2025-08-19
 */
public interface IThirdPartyAccountService extends IService<ThirdPartyAccount> {

    ThirdPartyAccount getByThirdId(String thirdId, String registrationId);

}
