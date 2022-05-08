package com.kusitms.forpet.controller;

import com.kusitms.forpet.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member/join")
public class JoinController {
    private final TokenProvider tokenProvider;

    @GetMapping("/terms")
    public String index(@RequestParam String email) {
        return "ss";
    }
}
