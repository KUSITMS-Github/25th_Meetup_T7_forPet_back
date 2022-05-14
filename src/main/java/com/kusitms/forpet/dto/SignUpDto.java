package com.kusitms.forpet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.StringTokenizer;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {
    private String nickname;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("pet_card_number")
    private String petCardNumber;
    private Address address;

    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String addr1;
        private String addr2;
        private String addr3;

        /**
         * address를 한 줄로 빌딩
         */
        public String getAddressList() {
            StringBuilder address = new StringBuilder();

            if(addr1 != null) {
                address.append(addr1);
                address.append("#");
                if(addr2 != null) {
                    address.append(addr2);
                    address.append("#");
                    if(addr3 != null) {
                        address.append(addr3);
                        address.append("#");
                    }
                }
            }
            return address.toString();
        }

        public void setAddress(String addressList) {
            StringTokenizer st = new StringTokenizer(addressList, "#");

            if(st.hasMoreTokens()) { this.addr1 = st.nextToken(); }
            if(st.hasMoreTokens()) { this.addr2 = st.nextToken(); }
            if(st.hasMoreTokens()) { this.addr3 = st.nextToken(); }

            return;
        }
    }


}
