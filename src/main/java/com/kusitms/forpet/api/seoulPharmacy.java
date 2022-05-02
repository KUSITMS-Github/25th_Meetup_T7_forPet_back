package com.kusitms.forpet.api;

import com.kusitms.forpet.domain.placeInfo;
import com.kusitms.forpet.repository.APIRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

@Component
public class seoulPharmacy {
    @Autowired
    private APIRepository apiRepository;

    @Autowired
    private geocoding geocoding;

    // 2~1000/ 1001~2000/2001~2877
    public void load() {
        String result = "";
        long cnt = 81;
        int array[] = {2, 1001, 2000};

        for (int page : array) {
            try {
                URL url = new URL("http://openapi.seoul.go.kr:8088/6a704c4757757575373945764a5071/json/LOCALDATA_020302/"
                        + String.format("%d", page)
                        + String.format("/%d/", page + 998));
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println(url.toString());
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader bf;
                bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                result = bf.readLine();

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
                JSONObject infoArr = (JSONObject) jsonObject.get("LOCALDATA_020302");
                JSONArray rowArr = (JSONArray) infoArr.get("row");


                for (int i = 0; i < rowArr.size(); i++) {
                    JSONObject data = (JSONObject) rowArr.get(i);
                    if (data.get("TRDSTATENM").equals("폐업") || (String) data.get("RDNWHLADDR") == "") {
                        continue;
                    }
                    System.out.println(data.get("BPLCNM"));
                    Map<String, String> geo = geocoding.getGeoDataByAddress((String) data.get("RDNWHLADDR"));
                    if (geo != null) {
                        String lng = geo.get("lng");
                        String lag = geo.get("lat");
                        placeInfo infoObj = new placeInfo(cnt, "동물약국",
                                (String) data.get("BPLCNM"), (String) data.get("RDNWHLADDR"), lng, lag);
                        apiRepository.save(infoObj);
                        System.out.println(cnt);
                        System.out.println("---------------------------------------------------------");
                        cnt++;

                    }
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
