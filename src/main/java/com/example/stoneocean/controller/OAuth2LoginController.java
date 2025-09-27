package com.example.stoneocean.controller;

import com.example.stoneocean.Util.Tools;
import com.example.stoneocean.entity.ApiResponse;
import com.example.stoneocean.entity.ThirdPartyAccount;
import com.example.stoneocean.entity.User;
import com.example.stoneocean.service.IThirdPartyAccountService;
import com.example.stoneocean.service.ITokenService;
import com.example.stoneocean.service.IUserService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Validated
@RestController
@RequestMapping("/oauth2Login")
public class OAuth2LoginController {
    private final Tools tool;
    private final IUserService userService;
    private final IThirdPartyAccountService thirdPartyAccountService;
    private final ITokenService tokenService;

    private final ClientRegistrationRepository registrationRepository;
    private final WebClient webClient;

    public OAuth2LoginController(Tools tool,
                                 IUserService userService,
                                 IThirdPartyAccountService thirdPartyAccountService,
                                 ITokenService tokenService,
                                 ClientRegistrationRepository registrationRepository,
                                 WebClient webClient) {
        this.tool = tool;
        this.userService = userService;
        this.thirdPartyAccountService = thirdPartyAccountService;
        this.tokenService = tokenService;
        this.registrationRepository = registrationRepository;
        this.webClient = webClient;
    }

    @GetMapping("/{registrationId}/code")
    public ApiResponse<Map<String, Object>> getCode(@PathVariable String registrationId) {
        ClientRegistration registration = getRegistration(registrationId);
        Map<String, Object> ret = new HashMap<>();
        ret.put("clientId", registration.getClientId());
        ret.put("scopes", registration.getScopes());
        ret.put("authorizationUri", registration.getProviderDetails().getAuthorizationUri());
        return ApiResponse.success(ret);
    }

    @PostMapping("/{registrationId}/register")
    @Transactional
    public ApiResponse<Map<String, Object>> getToken(@PathVariable String registrationId, @RequestBody String code
    ,HttpServletRequest request) {
        Assert.notNull(code, "code 不能为空");
        ClientRegistration registration = getRegistration(registrationId);
        ClientRegistration.ProviderDetails providerDetails = registration.getProviderDetails();
        Map<String, Object> data = new HashMap<>();
        data.put("code", code);
        data.put("client_id", registration.getClientId());
        data.put("client_secret", registration.getClientSecret());

        String tokenUri = providerDetails.getTokenUri();
        // 获取 token
        JsonNode tokenNode = this.webClient.post().uri(tokenUri)
                .bodyValue(data)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        // 获取三方用户信息
        // todo: 获取用户信息应当与 github 剥离
        Assert.isTrue(tokenNode != null && tokenNode.has("access_token"), "获取三方 token 失败");
        String accessToken = tokenNode.get("access_token").textValue();
        String thirdAccountJson = this.webClient.get().uri("https://api.github.com/user")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        Assert.isTrue(thirdAccountJson != null, "获取三方账户信息失败");

        // 反序列化为 User 对象
        Map<String, Object> thirdInfoMap = this.tool.tryDeserialize(thirdAccountJson, Map.class);

        // 三方用户检查 存在则直接登录关联账户，否则注册
        ThirdPartyAccount thirdAccount = thirdPartyAccountService.getByThirdId(thirdInfoMap.get("id").toString(), registrationId);
        User user = null;
        // 注册  用户信息存储，三方信息存储
        if (thirdAccount == null) {
            String account = String.join("-", registrationId, thirdInfoMap.get("id").toString());
            user = User.builder()
                    // 系统生成用户允许使用 “-”，为防止与自定义用户名冲突，自定义用户名不允许使用 “-”
                    .account(account)
                    .nickname(String.join(
                            "-",
                            Objects.toString(thirdInfoMap.get("name"), Strings.EMPTY),
                            account)
                    )
                    .email(Objects.toString(thirdInfoMap.get("email"), null))
                    .avatarUrl(Objects.toString(thirdInfoMap.get("avatar_url"), null))
                    .build();
            Assert.isTrue(this.userService.save(user), String.join("存储失败:", user.toString()));
            user = this.userService.getByAccount(account);
            Assert.isTrue(user != null, "预期外的错误，user 不存在");
            thirdAccount = ThirdPartyAccount.builder()
                    .userId(user.getId())
                    .thirdId(thirdInfoMap.get("id").toString())
                    .accountType(registrationId)
                    .info(thirdAccountJson)
                    .build();
            Assert.isTrue(this.thirdPartyAccountService.save(thirdAccount),
                    String.join("存储失败:", thirdAccount.toString()));
        } else {
            user = this.userService.getById(thirdAccount.getUserId());
        }
        // token 颁发
        String token = this.tokenService.token(user);
//        String token = this.tokenService.token(user.getAccount());
        Map<String, Object> ret = new HashMap<>();
        ret.put("token", token);
        return ApiResponse.success(ret);
    }

    private ClientRegistration getRegistration(@NotNull String registrationId) {
        Assert.notNull(registrationId, "registrationId 不能为空");
        return this.registrationRepository.findByRegistrationId(registrationId);
    }


}
