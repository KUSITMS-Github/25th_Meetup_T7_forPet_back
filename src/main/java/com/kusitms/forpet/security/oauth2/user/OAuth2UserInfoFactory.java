package com.kusitms.forpet.security.oauth2.user;
import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        System.err.println(attributes);
        return new KakaoOAuth2UserInfo(attributes);
    }
}
