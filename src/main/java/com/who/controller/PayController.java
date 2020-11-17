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

    @GetMapping("/pay/Success")
    public String PaySuccess(Model model) {
        return "payment/paySuccess";
    }

    @GetMapping("pay/Fail")
    public String PayFail(Model model) {
        return "payment/payFail";
    }

//
//
//    @GetMapping("/pay/paySuccess")
//    public String paySuccess(Model model) {
//        return "pay/paySuccess";
//    }
//
//    @GetMapping("/pay/payFail")
//    public String payFail(Model model) {
//        return "pay/payFail";
//    }
}
