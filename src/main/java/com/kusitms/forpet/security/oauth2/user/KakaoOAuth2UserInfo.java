package com.kusitms.forpet.security.oauth2.user;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo{
    /** 카카오는 Integer 로 받아져서 그런지 (String) 또는 (Long) 으로 cascading 이 되지 않는다...
     그래서 Integer 로 받아준다 */
    private Integer id;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super((Map<String, Object>) attributes.get("kakao_account"));
        //System.out.println(attributes.get("id").getClass());
        //this.id = Integer.parseInt(String.valueOf(attributes.get("id")));
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getName() {
        return (String) ((Map<String, Object>) attributes.get("profile")).get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) ((Map<String, Object>) attributes.get("profile")).get("thumbnail_image_url");
    }
}
