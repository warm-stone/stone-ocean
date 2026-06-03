package com.example.stoneocean.service.impl;

import com.example.stoneocean.entity.User;
import com.example.stoneocean.service.ITokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class TokenService implements ITokenService {

    private final JwtEncoder encoder;
    private final long EXPIRED_DURATION = 252000L;
    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }
    @Override
    public String token(Authentication authentication) {
        Instant now = Instant.now();
        // @formatter:off
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(EXPIRED_DURATION))
                .subject(authentication.getName())
                .claim("scope", scope)
                .claim("user", authentication.getPrincipal())
                .build();
        // @formatter:on
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }


    @Override
    public String token(User user) {
        Instant now = Instant.now();
        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(EXPIRED_DURATION))
                .subject(user.getAccount())
                .claim("userId", user.getId())
                .build();
        // @formatter:on
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
