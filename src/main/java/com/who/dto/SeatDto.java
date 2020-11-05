package com.who.dto;

import com.who.domain.entity.FileEntity;
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
    private Integer total;
    private SportsDto sportsDto;

    public SeatEntity toEntity() {
        SeatEntity seatEntity = SeatEntity.builder()
                .id(id)
                .area(area)
                .price(price)
                .total(total)
                .build();
        return seatEntity;
    }

    @Builder
    public SeatDto(Long id, String area, Integer price, Integer total, Integer available) {
        this.id = id;
        this.area = area;
        this.price = price;
        this.total = total;
    }
}