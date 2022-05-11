package com.kusitms.forpet.service;

import com.kusitms.forpet.config.CoolSMSProperties;
import com.kusitms.forpet.domain.Terms;
import com.kusitms.forpet.domain.TermsRecord;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.dto.TermsRecordDto;
import com.kusitms.forpet.repository.TermsRecordRepository;
import com.kusitms.forpet.repository.TermsRepository;
import com.kusitms.forpet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final TermsRepository termsRepository;
    private final TermsRecordRepository termsRecordRepository;
    private final UserRepository userRepository;
    private final CoolSMSProperties coolSMSProperties;

    public List<Terms> findAll() {
        return termsRepository.findAll();
    }

    public Long saveTermsRecord(TermsRecordDto dto) {
        Long userId = Long.valueOf(String.valueOf(dto.getId()));
        List<String> termsRecord = new ArrayList<String>();
        termsRecord.add(dto.getTerms1());
        termsRecord.add(dto.getTerms2());
        termsRecord.add(dto.getTerms3());

        System.out.println(dto.getTerms3() + ":" + termsRecord.get(2));

        for(int i = 0; i < termsRecord.size(); i++) {
            Long termsId = Long.valueOf(i + 1);

            termsRecordRepository.saveAndFlush(TermsRecord.builder()
                    .terms(Terms.builder().termsId(termsId).build())
                    .user(User.builder().userId(userId).build())
                    .is_agree(termsRecord.get(i))
                    .date(LocalDateTime.now())
                    .build());
        }
        return userId;
    }

    public User findByUserId(Long userId) { return userRepository.findByUserId(userId); }

    public boolean findByNickname(String nickname) {
        if(userRepository.findByNickname(nickname) == null) {
            return false;
        }
        return true;
    }

    public void certifiedPhoneNumber(String phoneNumber, String cerNum) {
        Message coolsms = new Message(coolSMSProperties.getApiKey(), coolSMSProperties.getApiSecret());

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);    // 수신전화번호
        params.put("from", coolSMSProperties.getFromNumber());    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "forpet 휴대폰인증 테스트 메시지 : 인증번호는" + "["+cerNum+"]" + "입니다.");
        params.put("app_version", "forpet service 1.0"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }

    }
}
