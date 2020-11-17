package com.who.dto;

import com.who.domain.entity.PaymentEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PaymentDto {
    private Long id;
    private String method;
    private int totalPrice;
    private LocalDateTime payDate;
    private BookingDto bookingDto;

    public PaymentEntity toEntity() {
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .id(id)
                .method(method)
                .totalPrice(totalPrice)
                .payDate(payDate)
                .bookingEntity(bookingDto.toEntity())
                .build();
        return paymentEntity;
    }

    @Builder
    public PaymentDto(Long id, String method, int totalPrice, LocalDateTime payDate, BookingDto bookingDto) {
        this.id = id;
        this.method = method;
        this.totalPrice = totalPrice;
        this.payDate = payDate;
        this.bookingDto = BookingDto.builder().build();
    }
}