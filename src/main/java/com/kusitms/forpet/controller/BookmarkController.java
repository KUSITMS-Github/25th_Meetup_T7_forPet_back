package com.kusitms.forpet.controller;

import com.kusitms.forpet.domain.Bookmark;
import com.kusitms.forpet.dto.BookmarkByCategoryDto;
import com.kusitms.forpet.dto.BookmarkByUserIdDto;
import com.kusitms.forpet.repository.BookmarkRepository;
import com.kusitms.forpet.service.BookmarkService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final BookmarkRepository bookmarkRepository;

    //북마크 생성
    @PostMapping("/offline-map/{placeid}/{userid}/bookmark")
    public Long createBookMark(@PathVariable("placeid") Long placeid,
                               @PathVariable("userid") Long userid) {

        Long id = bookmarkService.createBookMark(placeid, userid);

        return id;

    }



    //카테고리별 북마크
    @GetMapping("/offline-map/bookmark/category")
    public Result showBookmarkByCategory(@RequestParam String category) {
        List<Bookmark> bookmarkList = bookmarkRepository.find(category);


        //entity -> dto 변환
        List<BookmarkByCategoryDto> collect = bookmarkList.stream().map(m -> new BookmarkByCategoryDto(m.getId(), m.getPlaceInfo().getId(), m.getPlaceInfo().getName(),
                        m.getPlaceInfo().getAddress(),m.getPlaceInfo().getLongitude(), m.getPlaceInfo().getLatitude()))
                .collect(Collectors.toList());

        return new Result(category, collect);
    }

    //리턴값
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private String category;
        private T data;
    }



    //회원별 북마크 리스트
    @GetMapping("/offline-map/{userid}/bookmark")
    public List<BookmarkByUserIdDto> showBookMark(@PathVariable("userid") Long userid) {
        List<Bookmark> bookmarkList = bookmarkRepository.findByUserId(userid);

        //entity -> dto 변환
        List<BookmarkByUserIdDto> collect = bookmarkList.stream().map(m -> new BookmarkByUserIdDto(m.getId(), m.getUser().getUserId(), m.getPlaceInfo().getId(),
                        m.getPlaceInfo().getCategory() ,m.getPlaceInfo().getName(), m.getPlaceInfo().getAddress(), m.getPlaceInfo().getLongitude(), m.getPlaceInfo().getLatitude()))
                .collect(Collectors.toList());
        return collect;
    }




}
