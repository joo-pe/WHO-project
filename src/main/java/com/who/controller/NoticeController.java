package com.who.controller;


import com.who.dto.NoticeDto;
import com.who.service.NoticeService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class NoticeController {
    private NoticeService noticeService;

    @GetMapping("/notice")
    public String list(Model model) {
        List<NoticeDto> noticeList = noticeService.getNoticelist();

        model.addAttribute("noticeList", noticeList);
        return "notice/list";
    }

    @GetMapping("/post1")
    public String write() {
        return "notice/write";
    }

    @PostMapping("/post1")
    public String write(NoticeDto noticeDto) {
        noticeService.savePost1(noticeDto);

        return "redirect:/notice";
    }

    @GetMapping("/post1/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        NoticeDto noticeDto = noticeService.getPost(no);

        model.addAttribute("noticeDto", noticeDto);
        return "notice/detail";
    }

    @GetMapping("/post1/edit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        NoticeDto noticeDto = noticeService.getPost(no);

        model.addAttribute(("noticeDto"), noticeDto);
        return "notice/update";
    }

    @PutMapping("/post1/edit/{no}")
    public String update(NoticeDto noticeDto) {
        noticeService.savePost1(noticeDto);

        return "redirect:/notice";
    }

    @GetMapping("/notice/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model) {
        List<NoticeDto> noticeDtoList = noticeService.searchPosts(keyword);

        model.addAttribute("noticeList", noticeDtoList);
        return "notice/list";
    }
}
