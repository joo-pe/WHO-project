package com.who.controller;

import com.who.dto.*;
import com.who.service.*;
import lombok.AllArgsConstructor;
import org.dom4j.rule.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class PayController {
    private SportsService sportsService;
    private BookingService bookingService;
    private TicketService ticketService;
    private SeatService seatService;
    private MemberService memberService;
    private PerformanceService performanceService;

    @GetMapping("/pay/{no}")
    public String Pay(@PathVariable("no") Long no, Model model) {
        BookingDto bookingDto = bookingService.getBooking(no);

        Long sportsNo = bookingService.getSportsByBooking(no);
        SportsDto sportsDto = sportsService.getSports(sportsNo);

        Long seatNo = ticketService.getSeatByBooking(no);
        SeatDto seatDto = seatService.getSeat(seatNo);

        Long memberNo = bookingService.getMemberByBooking(no);
        MemberDto memberDto = memberService.getMember(memberNo);

        model.addAttribute("bookingDto", bookingDto);
        model.addAttribute("sportsDto", sportsDto);
        model.addAttribute("seatDto", seatDto);
        model.addAttribute("memberDto", memberDto);
        return "payment/pay";
    }
    @GetMapping("/musical/post/{no}/pay")
    public String musicalPay(@PathVariable("no") Long no, Model model) {
        PerformanceDto performanceDto = performanceService.getPerformance(no);

        model.addAttribute("performanceDto", performanceDto);
        return "peformance/pay";
    }

    @GetMapping("/pay/Success")
    public String PaySuccess(Model model) {
        return "payment/paySuccess";
    }

    @GetMapping("pay/Fail")
    public String PayFail(Model model) {
        return "payment/payFail";
    }
}
