package com.kusitms.forpet.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("coolsms")
@Getter
@Setter
public class CoolSMSProperties {
    private String apiKey;
    private String apiSecret;
    private String fromNumber;
}
