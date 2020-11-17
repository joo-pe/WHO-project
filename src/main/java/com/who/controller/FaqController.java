package com.who.controller;

import com.who.dto.FaqDto;
import com.who.dto.NoticeDto;
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

    @GetMapping("/admin/faq")
    public String list(Model model) {
        List<FaqDto> faqList = faqService.getFaqlist();

        model.addAttribute("faqList", faqList);
        return "admin/faq/list";
    }

    @GetMapping("/admin/post")
    public String write() {
        return "admin/faq/write";
    }

    @PostMapping("/admin/post")
    public String write(FaqDto faqDto) {
        faqService.savePost(faqDto);

        return "redirect:/admin/faq";
    }

    @GetMapping("/admin/post/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        FaqDto faqDto = faqService.getPost(no);

        model.addAttribute("faqDto", faqDto);
        return "admin/faq/detail";
    }

    @GetMapping("/admin/post/edit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        FaqDto faqDto = faqService.getPost(no);

        model.addAttribute(("faqDto"), faqDto);
        return "admin/faq/update";
    }

    @PutMapping("/admin/post/edit/{no}")
    public String update(FaqDto faqDto) {
        faqService.savePost(faqDto);

        return "redirect:/admin/faq";
    }

    @DeleteMapping("/admin/post/{no}")
    public String delete(@PathVariable("no") Long no) {
        faqService.deletePost(no);

        return "redirect:/admin/faq";
    }

    @GetMapping("/admin/faq/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model) {
        List<FaqDto> faqDtoList = faqService.searchPosts(keyword);

        model.addAttribute("faqList", faqDtoList);
        return "admin/faq/list";
    }
    
    @GetMapping("/faq")
    public String faq(Model model) {
        List<FaqDto> faqList = faqService.getFaqlist();

        model.addAttribute("faqList", faqList);
        return "faq/list";
    }
    
//    @GetMapping("/faq/search")
//    public String search1(@RequestParam(value = "keyword") String keyword, Model model) {
//        List<FaqDto> faqDtoList = faqService.searchPosts(keyword);
//
//        model.addAttribute("faqList", faqDtoList);
//        return "faq/list";
//    }
    
    @GetMapping("/faq/edit/{no}")
    public String list(@PathVariable("no") Long no, Model model) {
    FaqDto faqDto = faqService.getPost(no);

    model.addAttribute("faqDto", faqDto);  
    return "faq/detail";
    }


}
