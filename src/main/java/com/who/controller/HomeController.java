package com.who.controller;

import com.who.dto.FaqDto;
import com.who.dto.SportsDto;
import com.who.service.FileService;
import com.who.service.SportsService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.model.IModel;

import java.util.List;

@Controller
@AllArgsConstructor
public class HomeController {
    private SportsService sportsService;
    private FileService fileService;

    @GetMapping("/")
    public String home() {
        return "index2.html";
    }
}
