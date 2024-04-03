package com.kopchak.resourceserver.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Map;

@Getter
public class AppUser implements OidcUser {
    private final String oauth2ClientName;
    private final String principalName;
    private final Collection<? extends GrantedAuthority> authorities;

    public AppUser(String oauth2ClientName, String principalName, Collection<? extends GrantedAuthority> authorities) {
        this.oauth2ClientName = oauth2ClientName;
        this.principalName = principalName;
        this.authorities = authorities;
    }

    @Override
    public String getName() {
        return principalName;
    }

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }
}
