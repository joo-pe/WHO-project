package com.who.domain.entity;

import jdk.jfr.Enabled;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "KFAticket")
public class TicketEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "booking_id")
    private BookingEntity bookingEntity;

    @ManyToOne(targetEntity = MemberEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @OneToOne
    @JoinColumn(name = "seat_id")
    private SeatEntity seatEntity;

    @Builder
    public TicketEntity(Long id, BookingEntity bookingEntity,
                        MemberEntity memberEntity, SeatEntity seatEntity) {
        this.id = id;
        this.bookingEntity = bookingEntity;
        this.memberEntity = memberEntity;
        this.seatEntity = seatEntity;
    }
}
