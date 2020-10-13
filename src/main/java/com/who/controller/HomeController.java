package com.who.controller;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@NoArgsConstructor
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "index2.html";
    }

}