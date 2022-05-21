package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.placeInfo;
import com.kusitms.forpet.repository.APIRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class APIService {

    private final APIRep apiRepository;



    @Transactional(readOnly = true)
    public List<placeInfo> findAll() {
        return apiRepository.findAllByOrderByIdDesc();
    }
}