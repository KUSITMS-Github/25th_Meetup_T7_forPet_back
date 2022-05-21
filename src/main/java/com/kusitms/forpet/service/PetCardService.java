package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.PetCard;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.repository.PetCardRep;
import com.kusitms.forpet.repository.UserRep;
import com.kusitms.forpet.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetCardService {
    private final S3Uploader s3Uploader;
    private final PetCardRep petCardRepository;
    private final UserRep userRepository;

    public PetCard createPetCardByUserId(Long userId, MultipartFile petCardImage) {
        String petCardImageName = s3Uploader.uploadImage(petCardImage);
        StringBuilder petCardImageUrl = new StringBuilder();
        petCardImageUrl.append("https://kusitms-forpet.s3.ap-northeast-2.amazonaws.com/");
        petCardImageUrl.append(petCardImageName);

        // 동물 등록 카드 등록 -> 권한 변경
        Optional<User> temp = userRepository.findById(userId);
        User user = temp.get();
        user.updateRole(Role.FORPET_USER);

        PetCard petCard = PetCard.builder()
                .user(user)
                .imageUrl(petCardImageUrl.toString())
                .build();

        userRepository.save(user);
        petCardRepository.save(petCard);

        return petCard;
    }

    public PetCard findByUserId(User user) {
        Optional<PetCard> petCardOptional = petCardRepository.findByUser(user);
        if(petCardOptional.isPresent()) {
            return petCardOptional.get();
        }
        return null;
    }
}
