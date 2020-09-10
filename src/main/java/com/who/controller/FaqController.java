package com.who.controller;

import com.who.dto.FaqDto;
import com.who.service.FaqService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class FaqController {
    private FaqService faqService;

    @GetMapping("/faq")
    public String list(Model model) {
        List<FaqDto> faqList = faqService.getFaqlist();

        model.addAttribute("faqList", faqList);
        return "faq/list";
    }

    @GetMapping("/post")
    public String write() {
        return "faq/write";
    }

    @PostMapping("/post")
    public String write(FaqDto faqDto) {
        faqService.savePost(faqDto);

        return "redirect:/faq";
    }

    @GetMapping("/post/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        FaqDto faqDto = faqService.getPost(no);

        model.addAttribute("faqDto", faqDto);
        return "faq/detail";
    }

    @GetMapping("/post/edit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        FaqDto faqDto = faqService.getPost(no);

        model.addAttribute(("faqDto"), faqDto);
        return "faq/update";
    }

    @PutMapping("/post/edit/{no}")
    public String update(FaqDto faqDto) {
        faqService.savePost(faqDto);

        return "redirect:/faq";
    }

    @DeleteMapping("/post/{no}")
    public String delete(@PathVariable("no") Long no) {
        faqService.deletePost(no);

        return "redirect:/faq";
    }

    @GetMapping("/faq/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model) {
        List<FaqDto> faqDtoList = faqService.searchPosts(keyword);

        model.addAttribute("faqList", faqDtoList);
        return "faq/list";
    }
}
