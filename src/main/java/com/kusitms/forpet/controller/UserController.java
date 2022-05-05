package com.kusitms.forpet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class UserController {
    @GetMapping("/oauth2/callback/kakao")
    public void index(@RequestParam String access_token) {
        System.out.println("redirect : " + access_token);
    }
}
