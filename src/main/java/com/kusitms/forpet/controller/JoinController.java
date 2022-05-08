package com.kusitms.forpet.controller;

import com.kusitms.forpet.domain.Terms;
import com.kusitms.forpet.dto.ApiResponse;
import com.kusitms.forpet.dto.TermsDto;
import com.kusitms.forpet.dto.placeDto;
import com.kusitms.forpet.security.TokenProvider;
import com.kusitms.forpet.service.JoinService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member/join")
public class JoinController {
    private final TokenProvider tokenProvider;
    private final JoinService joinService;

    @GetMapping("/terms")
    public Result getTerms(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) {
        List<Terms> TermsList = joinService.findAll();
        List<TermsDto> collect = TermsList.stream().map(m -> new TermsDto(m.getTermsId(), m.getName(), m.getContent(), m.getIs_required()))
                .collect(Collectors.toList());
        return new Result(id, collect);
    }

    //리턴값
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private String id;
        private T data;
    }
}
