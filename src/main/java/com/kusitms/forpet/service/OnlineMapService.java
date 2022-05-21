package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.MapClickCount;
import com.kusitms.forpet.repository.MapClickCountRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OnlineMapService {
    private final MapClickCountRep mapClickCountRepository;

    public List<MapClickCount> getAllClickCnt() {
        return mapClickCountRepository.findAll();
    }

    public Long click(Long id) {
        long cnt = 0;
        Optional<MapClickCount> data = mapClickCountRepository.findById(id);
        if(data.isPresent()) {
            MapClickCount newCnt = data.get();
            cnt = newCnt.plusCnt();
            mapClickCountRepository.save(newCnt);
        }
        return cnt;
    }
}
