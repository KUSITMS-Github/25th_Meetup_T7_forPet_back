package com.kusitms.forpet.config;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatWebCustomConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    /*
    RFC 3986에는 영어 문자(a-zA-Z), 숫자(0-9), -. ~4 특수 문자 및 모든 예약 문자만 허용된다.
    해당 에러를 발생시키지 않으려면, 아래와 같이 relaxQueryChars 옵션에 허용할 문자를 추가하거나 톰캣 버전을 다운그레이드 해야한다.
     */
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers((TomcatConnectorCustomizer)
                connector -> connector.setAttribute("relaxedQueryChars", "<>[\\]^`{|}"));
    }
}
