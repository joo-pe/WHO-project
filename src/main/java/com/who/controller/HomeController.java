package com.who.controller;

import com.who.dto.FaqDto;
import com.who.dto.NoticeDto;
import com.who.dto.SportsDto;
import com.who.service.FaqService;
import com.who.service.FileService;
import com.who.service.NoticeService;
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
    private FaqService faqService;
    private SportsService sportsService;
    private FileService fileService;
    private NoticeService noticeService;

    @GetMapping("/")
    public String home(Model model) {
       List<FaqDto> faqList = faqService.getFaqlist();
        model.addAttribute("faqList", faqList);
        List<NoticeDto> noticeList = noticeService.getNoticelist();
        model.addAttribute("noticeList", noticeList);
        return "index2.html";
    }

    @GetMapping("/productdetail")
    public String productDetail() {
        return "sports/detail";
    }

    @GetMapping("/productdetail/pay")
    public String pay() {
        return "pay";
    }
}
