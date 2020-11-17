package com.who.service;

import com.who.domain.entity.BookingEntity;
import com.who.domain.entity.MemberEntity;
import com.who.domain.entity.SeatEntity;
import com.who.domain.entity.TicketEntity;
import com.who.domain.repository.TicketRepository;
import com.who.dto.BookingDto;
import com.who.dto.MemberDto;
import com.who.dto.SeatDto;
import com.who.dto.TicketDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TicketService {
    private TicketRepository ticketRepository;
    private BookingService bookingService;
    private MemberService memberService;
    private SeatService seatService;

    @Transactional
    public List<TicketDto> getTicketList() {
        List<TicketEntity> ticketEntities = ticketRepository.findAll();
        List<TicketDto> ticketDtoList = new ArrayList<>();

        for(TicketEntity ticketEntity : ticketEntities) {
            ticketDtoList.add(this.convertEntityToDto(ticketEntity));
        }
        return ticketDtoList;
    }

    @Transactional
    public TicketDto getTicket(Long id) {
        Optional<TicketEntity> ticketEntityWraper = ticketRepository.findById(id);
        TicketEntity ticketEntity = ticketEntityWraper.get();

        return this.convertEntityToDto(ticketEntity);
    }

    @Transactional
    public Long getSeatByBooking(Long id) {
        return ticketRepository.getSeatByBooking(id);
    }

    private TicketDto convertEntityToDto(TicketEntity ticketEntity) {
        BookingEntity bookingEntity = ticketEntity.getBookingEntity();
        BookingDto bookingDto = bookingService.convertEntityToDto(bookingEntity);

        MemberEntity memberEntity = ticketEntity.getMemberEntity();
        MemberDto memberDto = memberService.convertEntityToDto(memberEntity);

        SeatEntity seatEntity = ticketEntity.getSeatEntity();
        SeatDto seatDto = seatService.convertEntityToDto(seatEntity);

        TicketDto ticketDto = TicketDto.builder()
                .id(ticketEntity.getId())
                .bookingDto(bookingDto)
                .memberDto(memberDto)
                .seatDto(seatDto)
                .build();

        return ticketDto;
    }
}