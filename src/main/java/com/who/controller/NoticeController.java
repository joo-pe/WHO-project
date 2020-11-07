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

    @GetMapping("/admin/notice")
    public String list(Model model) {
        List<NoticeDto> noticeList = noticeService.getNoticelist();

        model.addAttribute("noticeList", noticeList);
        return "admin/notice/list";
    }

    @GetMapping("/admin/post1")
    public String write() {
        return "admin/notice/write";
    }

    @PostMapping("/admin/post1")
    public String write(NoticeDto noticeDto) {
        noticeService.savePost(noticeDto);

        return "redirect:/admin/notice";
    }
    
    @GetMapping("/admin/post1/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        NoticeDto noticeDto = noticeService.getPost(no);

        model.addAttribute("noticeDto", noticeDto);
        return "admin/notice/detail";
    }

    @GetMapping("/admin/post1/edit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        NoticeDto noticeDto = noticeService.getPost(no);

        model.addAttribute(("noticeDto"), noticeDto);
        return "admin/notice/update";
    }

    @PutMapping("/admin/post1/edit/{no}")
    public String update(NoticeDto noticeDto) {
        noticeService.savePost(noticeDto);

        return "redirect:/admin/notice";
    }
    
    @DeleteMapping("/admin/post1/{no}")
    public String delete(@PathVariable("no") Long no) {
        noticeService.deletePost(no);

        return "redirect:/admin/notice";
    }

    @GetMapping("/admin/notice/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model) {
        List<NoticeDto> noticeDtoList = noticeService.searchPosts(keyword);

        model.addAttribute("noticeList", noticeDtoList);
        return "admin/notice/list";
    }

    @GetMapping("/notice")
    public String list1(Model model) {
        List<NoticeDto> noticeList = noticeService.getNoticelist();

        model.addAttribute("noticeList", noticeList);
        return "notice/list";
    }
    @GetMapping("/post1/{no}")
    public String list(@PathVariable("no") Long no, Model model) {
        NoticeDto noticeDto = noticeService.getPost(no);

        model.addAttribute("noticeDto", noticeDto);
        return "notice/list";
    }
}
