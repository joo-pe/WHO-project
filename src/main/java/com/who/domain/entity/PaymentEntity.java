package com.who.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "payment")
public class PaymentEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private int totalPrice;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime payDate;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private BookingEntity bookingEntity;

    @Builder
    public PaymentEntity(Long id, String method, int totalPrice,
                         LocalDateTime payDate, BookingEntity bookingEntity) {
        this.id = id;
        this.method = method;
        this.totalPrice = totalPrice;
        this.payDate = payDate;
        this.bookingEntity = bookingEntity;
    }
}
