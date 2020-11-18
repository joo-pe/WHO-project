package com.who.controller;

import com.who.dto.PerformanceDto;
import com.who.dto.SportsDto;
import com.who.service.PerformanceService;
import com.who.service.SportsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class PayController {
    private SportsService sportsService;
    private PerformanceService performanceService;

    @GetMapping("/soccer/post/{no}/pay")
    public String soccerPay(@PathVariable("no") Long no, Model model) {
        SportsDto sportsDto = sportsService.getSports(no);

        model.addAttribute("sportsDto", sportsDto);
        return "sports/pay";
    }
    
    @GetMapping("/pay")
    public String Pay() {
    	return "sports/pay";
    }
    
    @GetMapping("/musical/post/{no}/pay")
    public String musicalPay(@PathVariable("no") Long no, Model model) {
        PerformanceDto performanceDto = performanceService.getPerformance(no);

        model.addAttribute("performanceDto", performanceDto);
        return "peformance/pay";
    }

    @GetMapping("/pay/paySuccess")
    public String paySuccess(Model model) {
        return "pay/paySuccess";
    }

    @GetMapping("/pay/payFail")
    public String payFail(Model model) {
        return "pay/payFail";
    }
}
