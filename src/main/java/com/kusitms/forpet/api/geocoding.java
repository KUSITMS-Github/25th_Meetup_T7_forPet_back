package com.kusitms.forpet.api;

import org.json.JSONArray;
import org.springframework.stereotype.Component;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Component
public class geocoding {

    public static Map<String, String> getGeoDataByAddress(String completeAddress) {
        try {
            String API_KEY = "AIzaSyB2EvWwB3eZtL6WwPe97HJbOtC12pedmxw";
            String surl = "https://maps.googleapis.com/maps/api/geocode/json?address="+URLEncoder.encode(completeAddress, "UTF-8")+"&key="+API_KEY;
            URL url = new URL(surl);
            InputStream is = url.openConnection().getInputStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

            JSONObject jo = new JSONObject(responseStrBuilder.toString());
            JSONArray results = jo.getJSONArray("results");
            String region = null;
            String province = null;
            String zip = null;
            Map<String, String> ret = new HashMap<String, String>();
            if(results.length() > 0) {
                JSONObject jsonObject;
                jsonObject = results.getJSONObject(0);
                Double lat = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                Double lng = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                ret.put("lat", lat.toString());
                ret.put("lng", lng.toString());
                JSONArray ja = jsonObject.getJSONArray("address_components");
                System.out.println(ja.toString());
                for(int l=0; l<ja.length(); l++) {
                    JSONObject curjo = ja.getJSONObject(l);
                    String type = curjo.getJSONArray("types").getString(0);
                    String short_name = curjo.getString("short_name");
                    if(type.equals("postal_code")) {
                        ret.put("zip", short_name);
                    }
                    else if(type.equals("administrative_area_level_3")) {
                        ret.put("city", short_name);
                    }
                    else if(type.equals("administrative_area_level_2")) {
                        ret.put("province", short_name);
                    }
                    else if(type.equals("administrative_area_level_1")) {
                        ret.put("region", short_name);
                    }
                }
                return ret;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
