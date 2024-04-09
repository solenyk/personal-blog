package com.kopchak.resourceserver.config.auth.converter;

import com.kopchak.resourceserver.config.auth.principal.AuthPrincipal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtToOAuth2Converter implements Converter<Jwt, OAuth2AuthenticationToken> {


    @Override
    public OAuth2AuthenticationToken convert(@NonNull Jwt jwt) {
        AuthPrincipal user = new AuthPrincipal(getClientName(jwt), getName(jwt), getAuthorities(jwt));
        return new OAuth2AuthenticationToken(user, user.authorities(), user.oauth2ClientName());
    }

    public String getClientName(Jwt jwt) {
        return jwt.getClaimAsString("aud");
    }

    public Collection<? extends GrantedAuthority> getAuthorities(Jwt jwt) {
        List<String> roles = jwt.getClaim("authorities");
        if (roles == null || roles.isEmpty()) {
            return new ArrayList<>();
        }
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String getName(Jwt jwt) {
        return jwt.getSubject();
    }
}
