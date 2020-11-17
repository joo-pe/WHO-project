package com.who.dto;

import com.who.domain.entity.SeatEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SeatDto {
    private Long id;
    private String area;
    private Integer price;
    private boolean allocated;
    private SportsDto sportsDto;

    public SeatEntity toEntity() {
        SeatEntity seatEntity = SeatEntity.builder()
                .id(id)
                .area(area)
                .price(price)
                .allocated(allocated)
                .sportsEntity(sportsDto.toEntity())
                .build();
        return seatEntity;
    }

    @Builder
    public SeatDto(Long id, String area, Integer price, Integer total,
                   boolean allocated, SportsDto sportsDto ) {
        this.id = id;
        this.area = area;
        this.price = price;
        this.allocated = allocated;
        this.sportsDto = SportsDto.builder().build();
    }
}
