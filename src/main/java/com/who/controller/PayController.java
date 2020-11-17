package com.who.controller;

import com.who.dto.BookingDto;
import com.who.dto.SeatDto;
import com.who.dto.SportsDto;
import com.who.dto.TicketDto;
import com.who.service.BookingService;
import com.who.service.SeatService;
import com.who.service.SportsService;
import com.who.service.TicketService;
import lombok.AllArgsConstructor;
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

    @GetMapping("/pay/{no}")
    public String Pay(@PathVariable("no") Long no, Model model) {
        BookingDto bookingDto = bookingService.getBooking(no);
        SportsDto sportsDto = bookingDto.getSportsDto();
        Long seatNo = ticketService.getSeatByBooking(no);
        SeatDto seatDto = seatService.getSeat(seatNo);

        model.addAttribute("bookingDto", bookingDto);
        model.addAttribute("sportsDto", sportsDto);
        model.addAttribute("seatDto", seatDto);
        return "payment/pay";
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
