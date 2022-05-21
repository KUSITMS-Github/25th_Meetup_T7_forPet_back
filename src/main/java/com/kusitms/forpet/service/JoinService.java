package com.kusitms.forpet.service;

import com.kusitms.forpet.config.CoolSMSProperties;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.dto.SignUpDto;
import com.kusitms.forpet.repository.UserRep;
import com.kusitms.forpet.security.Role;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final UserRep userRepository;
    private final CoolSMSProperties coolSMSProperties;
    private final S3Uploader s3Uploader;

    public User findByUserId(Long userId) { return userRepository.findByUserId(userId); }

    public boolean findByNickname(String nickname) {
        if(userRepository.findByNickname(nickname) == null) {
            return false;
        }
        return true;
    }

    public void certifiedPhoneNumber(String phoneNumber, String cerNum) {
        Message coolsms = new Message(coolSMSProperties.getApiKey(), coolSMSProperties.getApiSecret());

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);    // 수신전화번호
        params.put("from", coolSMSProperties.getFromNumber());    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "forpet 휴대폰인증 테스트 메시지 : 인증번호는" + "["+cerNum+"]" + "입니다.");
        params.put("app_version", "forpet service 1.0"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }

    public User createUser(Long id, SignUpDto dto, MultipartFile profileImage) {
        // user update
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            User newUser = user.get();
            if(dto.getAddress() == null) {
                dto.setAddress(new SignUpDto.Address());
            }

            newUser.signupUser(dto.getNickname(), dto.getPhoneNumber(), dto.getAddress().getAddressList());

            //권한 변경 GUEST -> USER
            newUser.updateRole(Role.USER);

            // 프로필 사진
            if(!profileImage.getOriginalFilename().equals("")) {
                // image file -> url
                String profileImageName = s3Uploader.uploadImage(profileImage);
                StringBuilder profileImageUrl = new StringBuilder();
                profileImageUrl.append("https://kusitms-forpet.s3.ap-northeast-2.amazonaws.com/");
                profileImageUrl.append(profileImageName);

                newUser.updateCustomImage(profileImageUrl.toString());
            }

            userRepository.save(newUser);
            return newUser;
        }
        return null;
    }
}
