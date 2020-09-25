package com.who.controller;

<<<<<<< HEAD

=======
>>>>>>> ebd1cc9297af551fc239848def478b953e92d21d
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
<<<<<<< HEAD
        noticeService.savePost1(noticeDto);
=======
        noticeService.savePost(noticeDto);
>>>>>>> ebd1cc9297af551fc239848def478b953e92d21d

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
<<<<<<< HEAD
        noticeService.savePost1(noticeDto);
=======
        noticeService.savePost(noticeDto);

        return "redirect:/notice";
    }
    
    @DeleteMapping("/post1/{no}")
    public String delete(@PathVariable("no") Long no) {
        noticeService.deletePost(no);
>>>>>>> ebd1cc9297af551fc239848def478b953e92d21d

        return "redirect:/notice";
    }

    @GetMapping("/notice/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model) {
        List<NoticeDto> noticeDtoList = noticeService.searchPosts(keyword);

        model.addAttribute("noticeList", noticeDtoList);
        return "notice/list";
    }
}