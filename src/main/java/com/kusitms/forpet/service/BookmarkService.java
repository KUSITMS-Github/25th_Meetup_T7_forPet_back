package com.kusitms.forpet.service;

import com.kusitms.forpet.domain.Bookmark;
import com.kusitms.forpet.domain.placeInfo;
import com.kusitms.forpet.repository.APIRepository;
import com.kusitms.forpet.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final APIRepository apiRepository;

    /**
     * 북마크 생성
     */
    @Transactional
    public Long createBookMark(Long placeid) {
        placeInfo placeInfo = apiRepository.findById(placeid).get();

        //북마크 생성
        Bookmark bookmark = new Bookmark();
        bookmark.setPlaceInfo(placeInfo);

        Bookmark save = bookmarkRepository.save(bookmark);

        return save.getId();
    }
}
