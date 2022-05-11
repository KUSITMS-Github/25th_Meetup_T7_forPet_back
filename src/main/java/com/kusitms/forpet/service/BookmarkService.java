package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.Bookmark;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.domain.placeInfo;
import com.kusitms.forpet.repository.APIRepository;
import com.kusitms.forpet.repository.BookmarkRepository;
import com.kusitms.forpet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final APIRepository apiRepository;
    private final UserRepository userRepository;

    /**
     * 북마크 생성
     */
    @Transactional
    public Long createBookMark(Long placeid, Long userid) {
        placeInfo placeInfo = apiRepository.findById(placeid).get();
        User user = userRepository.findById(userid).get();

        //북마크 생성
        Bookmark bookmark = new Bookmark();
        bookmark.setPlaceInfo(placeInfo);
        bookmark.setUser(user);

        Bookmark save = bookmarkRepository.save(bookmark);

        return save.getId();
    }
}
