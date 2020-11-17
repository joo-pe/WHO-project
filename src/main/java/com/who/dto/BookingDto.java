package com.who.dto;

import com.who.domain.entity.BookingEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private MemberDto memberDto;
    private SportsDto sportsDto;
    private boolean canceled;

    public BookingEntity toEntity() {
        BookingEntity bookingEntity = BookingEntity.builder()
                .id(id)
                .memberEntity(memberDto.toEntity())
                .sportsEntity(sportsDto.toEntity())
                .canceled(canceled)
                .build();
        return bookingEntity;
    }

    @Builder
    public BookingDto(Long id, MemberDto memberDto, SportsDto sportsDto, boolean canceled) {
        this.id = id;
        this.memberDto = MemberDto.builder().build();
        this.sportsDto = SportsDto.builder().build();
        this.canceled = canceled;
    }

}