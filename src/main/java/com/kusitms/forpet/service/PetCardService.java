package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.PetCard;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.repository.PetCardRepository;
import com.kusitms.forpet.repository.UserRepository;
import com.kusitms.forpet.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetCardService {
    private final S3Uploader s3Uploader;
    private final PetCardRepository petCardRepository;
    private final UserRepository userRepository;

    public PetCard createPetCardByUserId(Long userId, MultipartFile petCardImage, String cardNumber) {
        String petCardImageName = s3Uploader.uploadImage(petCardImage);
        StringBuilder petCardImageUrl = new StringBuilder();
        petCardImageUrl.append("https://kusitms-forpet.s3.ap-northeast-2.amazonaws.com/");
        petCardImageUrl.append(petCardImageName);

        // 동물 등록 카드 등록 -> 권한 변경
        Optional<User> temp = userRepository.findById(userId);
        User user = temp.get();
        user.updateRole(Role.FORPET_USER);

        userRepository.save(user);

        PetCard petCard = PetCard.builder()
                .userId(user)
                .cardNumber(cardNumber)
                .imageUrl(petCardImageUrl.toString())
                .build();
        petCardRepository.save(petCard);

        return petCard;
    }

    public PetCard findByUserId(User userId) {
        return petCardRepository.findByUserId(userId);
    }
}
