package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.Terms;
import com.kusitms.forpet.domain.TermsRecord;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.dto.TermsRecordDto;
import com.kusitms.forpet.repository.TermsRecordRepository;
import com.kusitms.forpet.repository.TermsRepository;
import com.kusitms.forpet.repository.UserRepository;
import com.kusitms.forpet.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final TermsRepository termsRepository;
    private final TermsRecordRepository termsRecordRepository;
    private final UserRepository userRepository;

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
}
