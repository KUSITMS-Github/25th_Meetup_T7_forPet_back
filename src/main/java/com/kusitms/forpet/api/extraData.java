package com.kusitms.forpet.api;

import com.kusitms.forpet.domain.placeInfo;
import com.kusitms.forpet.repository.APIRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class extraData {

    @Autowired
    private APIRep apiRepository;

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
                    map.get(key), key, geo.get("lng"), geo.get("lat"), 0, 0, null, null);
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
                    map.get(key), key, geo.get("lng"), geo.get("lat"), 0, 0, null, null);
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
                    map.get(key), key, geo.get("lng"), geo.get("lat"), 0, 0, null, null);
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
                    map.get(key), key, geo.get("lng"), geo.get("lat"), 0, 0, null, null);
            apiRepository.save(infoObj);
            cnt++;
        }
    }

    //불광동 데이터 추가
    public void save_bulgwang() {
        long cnt = 50;
        Map<String, String> mapHospital = new HashMap<String, String>();
        mapHospital.put("서울특별시 은평구 대조동 14-240", "쿨펫동물병원");
        mapHospital.put("서울특별시 은평구 갈현동 394-27", "메디펫동물병원");
        mapHospital.put("서울특별시 은평구 불광로 59", "동물병원 움");
        for(String key : mapHospital.keySet()) {
            Map<String, String> geo = geocoding.getGeoDataByAddress(key);
            placeInfo infoObj = new placeInfo(cnt, "동물병원",
                    mapHospital.get(key), key, geo.get("lng"), geo.get("lat"), 0, 0, null, null);
            apiRepository.save(infoObj);
            cnt++;
        }

        Map<String, String> mapPharmacy = new HashMap<String, String>();
        mapPharmacy.put("서울특별시 은평구 불광동 8-4", "우리약국");
        mapPharmacy.put("서울특별시 은평구 불광동 285-27", "불광동우리들약국");
        mapPharmacy.put("서울특별시 은평구 불광동 222-1", "안녕약국");

        for(String key : mapPharmacy.keySet()) {
            Map<String, String> geo = geocoding.getGeoDataByAddress(key);
            placeInfo infoObj = new placeInfo(cnt, "동물약국",
                    mapPharmacy.get(key), key, geo.get("lng"), geo.get("lat"), 0, 0, null, null);
            apiRepository.save(infoObj);
            cnt++;
        }

        Map<String, String> mapSalon = new HashMap<String, String>();
        mapSalon.put("녹번동 131-41번지 102호 은평구 서울특별시 KR", "친절한애견미용실");
        mapSalon.put("서울특별시 은평구 불광2동 290-5", "은평애견");
        for(String key : mapSalon.keySet()) {
            Map<String, String> geo = geocoding.getGeoDataByAddress(key);
            placeInfo infoObj = new placeInfo(cnt, "미용실",
                    mapSalon.get(key), key, geo.get("lng"), geo.get("lat"), 0, 0, null, null);
            apiRepository.save(infoObj);
            cnt++;
        }

        Map<String, String> mapCafe = new HashMap<String, String>();
        mapCafe.put("서울특별시 은평구 불광동 연서로 298", "내생각 애견호텔");
        mapCafe.put("서울특별시 은평구 불광동 연서로 253-7", "하리");
        for(String key : mapCafe.keySet()) {
            Map<String, String> geo = geocoding.getGeoDataByAddress(key);
            placeInfo infoObj = new placeInfo(cnt, "카페",
                    mapCafe.get(key), key, geo.get("lng"), geo.get("lat"), 0, 0, null, null);
            apiRepository.save(infoObj);
            cnt++;
        }





    }

}
