package com.yunseong.member.config;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class IpTokenStore extends InMemoryTokenStore {

    private final Map<OAuth2AccessToken, String> accessTokenMap = new HashMap<>();

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        String ip = getIp();
        OAuth2AccessToken accessToken = super.getAccessToken(authentication);
        if(accessToken != null && this.accessTokenMap.containsKey(accessToken)) {
            if(!this.accessTokenMap.get(accessToken).equals(ip)) {
                removeAccessToken(accessToken);
                removeRefreshToken(accessToken.getRefreshToken());
                return null;
            }
        }
        this.accessTokenMap.put(accessToken, ip);
        return accessToken;
    }

    private String getIp() {
        HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = req.getHeader("X-FORWARDED-FOR");
        if (ip == null)
            ip = req.getRemoteAddr();
        return ip;
    }
}
