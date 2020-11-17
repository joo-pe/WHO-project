package com.who.dto;

import com.who.domain.entity.TicketEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TicketDto {
    private Long id;
    private BookingDto bookingDto;
    private MemberDto memberDto;
    private SeatDto seatDto;

    public TicketEntity toEntity() {
        TicketEntity ticketEntity = TicketEntity.builder()
                .id(id)
                .bookingEntity(bookingDto.toEntity())
                .memberEntity(memberDto.toEntity())
                .seatEntity(seatDto.toEntity())
                .build();
        return ticketEntity;
    }

    @Builder
    public TicketDto(Long id, BookingDto bookingDto, MemberDto memberDto, SeatDto seatDto) {
        this.id = id;
        this.bookingDto = BookingDto.builder().build();
        this.memberDto = MemberDto.builder().build();
        this.seatDto = SeatDto.builder().build();
    }
}