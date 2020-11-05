package com.who.controller;

import lombok.AllArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.who.domain.MemberDetail;


@Controller
@AllArgsConstructor
public class MusicController {
   
    
    @GetMapping("/musical")
    public String product(Model model) {
        
        return "musical/music_index";
    }
    
    @GetMapping("/musical/mu")
    public ModelAndView districtCheck(@RequestParam String districtId, Model model) {

    	ModelAndView mav = new ModelAndView("musical/mu");
    	mav.addObject("district", districtId);
    	mav.addObject("price",60000);
    	
    	return mav;
    }
    
    @GetMapping("/musical/mu1")
    public String mu1() {
        
        return "musical/mu1.html";
    }
    @GetMapping("/musical/mu2")
    public String mu2() {
        
        return "musical/mu2.html";
    }
    @GetMapping("/musical/mu3")
    public String mu3() {
        
        return "/musical/mu3.html";
    }
    
   
    @GetMapping("/theater")
    public String theater(Model model) {
        
        return "/theater/theater.html";
    } 
    
    @GetMapping("/theater/th")
    public ModelAndView districtName(@RequestParam String districtth, Model model) {

    	ModelAndView ma = new ModelAndView("theater/th");
    	ma.addObject("district", districtth);
    	ma.addObject("price",60000);
    	
    	return ma;
    } 
   
}
