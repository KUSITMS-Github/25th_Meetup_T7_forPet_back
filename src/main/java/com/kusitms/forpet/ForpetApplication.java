package com.kusitms.forpet;

import com.kusitms.forpet.api.animalHosp;
import com.kusitms.forpet.api.seoulPharmacy;
import com.kusitms.forpet.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class) // 구성 속성 활성화
public class ForpetApplication {

	@Autowired
	private final ApplicationContext context;

	public static void main(String[] args) {
		SpringApplication.run(ForpetApplication.class, args);
		/**
		 * Bean 등록 후 API 데이터 DB 저장
		 * 처음 한번만 실행
		 */
		//BeanContext.get(animalHosp.class).load();
		//BeanContext.get(seoulPharmacy.class).load();
	}

	@PostConstruct
	public void init() {
		BeanContext.init(context);
	}
}
