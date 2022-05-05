package com.kusitms.forpet.api;

import com.kusitms.forpet.domain.placeInfo;
import com.kusitms.forpet.repository.APIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class extraData {

    @Autowired
    private APIRepository apiRepository;

    @Autowired
    private geocoding geocoding;

    public void save_salon() {
        long cnt = 394;
        Map<String, String> map = new HashMap<String, String>();
        map.put("서울특별시 강남구 도곡동 422-1 번지 지상 2층", "N24BEAUTYLAB N24뷰티랩 도곡동점");
        map.put("서울특별시 강남구 학동로6길 29", "강아지 삼총사");
        map.put("서울특별시 강남구 삼성동 36-3", "개스타일");
        map.put("서울특별시 강남구 188-16 101호", "오드리애견미용실 -논현본점-");
        map.put("서울특별시 강남구 일원동 718", "별애견샵");
        map.put("서울특별시 강남구 개포동 12", "미라클펫살롱");
        map.put("잠원동 24-18번지 1층 서초구 서울특별시 KR", "윤살롱");
        map.put("논현동 125-17번지 1층 강남구 서울특별시 KR", "달이네애견미용실");
        map.put("논현동 162-13번지 1층 강남구 서울특별시 KR", "오드리펫샵");
        map.put("논현동 181-8번지 1층 강남구 서울특별시 KR", "논현애견샵");
        map.put("서울특별시 강남구 논현동 272-26", "머멍펫살롱(MuMung)");

        for(String key : map.keySet()) {
            Map<String, String> geo = geocoding.getGeoDataByAddress(key);
            System.out.println();
            System.out.println();
            placeInfo infoObj = new placeInfo(cnt, "미용실",
                    map.get(key), key, geo.get("lng"), geo.get("lat"), 0, 0, null);
            apiRepository.save(infoObj);
            cnt++;
        }
    }

    public void save_center() {
        long cnt = 405;
        Map<String, String> map = new HashMap<String, String>();
        map.put("서울특별시 강남구 역삼동 830-42", "도그마루 역삼점");
        map.put("동남로4길 13 KR 서울특별시 송파구 문정동 30", "몽타운");

        for(String key : map.keySet()) {
            Map<String, String> geo = geocoding.getGeoDataByAddress(key);
            placeInfo infoObj = new placeInfo(cnt, "보호소",
                    map.get(key), key, geo.get("lng"), geo.get("lat"), 0, 0, null);
            apiRepository.save(infoObj);
            cnt++;
        }
    }

    public void save_school() {
        long cnt = 407;
        Map<String, String> map = new HashMap<String, String>();
        map.put("서울특별시 서초구 서초4동 서초대로73길 12", "스타몽");
        map.put("서울특별시 강남구 도곡로7길 8 송현빌딩 본관 2층", "꿈에개린");

        for(String key : map.keySet()) {
            Map<String, String> geo = geocoding.getGeoDataByAddress(key);
            placeInfo infoObj = new placeInfo(cnt, "유치원",
                    map.get(key), key, geo.get("lng"), geo.get("lat"), 0, 0, null);
            apiRepository.save(infoObj);
            cnt++;
        }
    }

    public void save_cafe() {
        long cnt = 409;
        Map<String, String> map = new HashMap<String, String>();
        map.put("서울특별시 강남구 역삼동 608-23", "다독인더씨티");
        map.put("서울특별시 강남구 논현동 85-13번지 1층", "반려문화");
        map.put("서울특별시 서초구 서초4동 사평대로56길 4", "개랑놀아주는남자");
        map.put("서울특별시 강남구 역삼로 134", "더왈츠");
        map.put("Jinheung Building 39, 강남대로118길 강남구 서울특별시", "Bong Brothers Dog Cafe 강아지카페");
        map.put("서울특별시 강남구 역삼동 667-10번지 하1층 별관", "두젠틀");
        map.put("서울특별시 강남구 강남대로102길 14 장연빌딩 5층 501호", "히히냥냥");
        map.put("서울특별시 강남구 역삼동 789-7", "페스츄리");
        map.put("서울특별시 강남구 역삼동 강남대로102길 34", "알베르");
        map.put("서울특별시 강남구 논현동 170", "강아지똥");

        for(String key : map.keySet()) {
            Map<String, String> geo = geocoding.getGeoDataByAddress(key);
            placeInfo infoObj = new placeInfo(cnt, "카페",
                    map.get(key), key, geo.get("lng"), geo.get("lat"), 0, 0, null);
            apiRepository.save(infoObj);
            cnt++;
        }
    }

}
