package com.kusitms.forpet.api;

import com.kusitms.forpet.domain.placeInfo;
import com.kusitms.forpet.repository.APIRepository;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Map;
@Component
public class animalHosp {
    @Value("${key1}")
    String KEY;

    @Autowired
    private APIRepository apiRepository;

    @Autowired
    private geocoding geocoding;

    public void load() {
        String result = "";
        long cnt = 26;
        for (int page = 1; page < 9; page++) {
            try {
                URL url = new URL("https://api.odcloud.kr/api/15089965/v1/uddi:86c99769-6ddc-47a3-9f18-f3bd3a96ab1e?"
                        + String.format("page=%d", page)
                        + "&perPage=10&returnType=JSON&serviceKey="
                        + String.format("%s", KEY));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader bf;
                bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                result = bf.readLine();

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
                JSONArray infoArr = (JSONArray) jsonObject.get("data");

                for (int i = 0; i < infoArr.size(); i++) {
                    JSONObject data = (JSONObject) infoArr.get(i);
                    Map<String, String> geo = geocoding.getGeoDataByAddress((String) data.get("소재지 주소(지번)"));
                    String lng = geo.get("lng");
                    String lag = geo.get("lat");
                    placeInfo infoObj = new placeInfo(cnt, "동물병원",
                            (String) data.get("병원명"), (String) data.get("소재지 주소(도로명)"), lng, lag);
                    apiRepository.save(infoObj);
                    cnt++;
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        }
    }

}
