package com.who.controller;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@NoArgsConstructor
public class MyPageController {

    @GetMapping("/mypage")
    public String mypage() {
        return "mypage/mypage.html";
    }
}
