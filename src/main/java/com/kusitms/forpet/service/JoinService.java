package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.Terms;
import com.kusitms.forpet.repository.TermsRepository;
import com.kusitms.forpet.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final TermsRepository termsRepository;

    public List<Terms> findAll() {
        return termsRepository.findAll();
    }
}
