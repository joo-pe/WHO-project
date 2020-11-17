package com.who.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "sportsBooking")
public class BookingEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(targetEntity = MemberEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "ticket_id")
//    private List<TicketEntity> tickets = new ArrayList<>();

    @ManyToOne(targetEntity = SportsEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "sports_id")
    private SportsEntity sportsEntity;

    @Column(nullable = false)
    private boolean canceled;
    
    @Builder
    public BookingEntity(Long id, MemberEntity memberEntity,
                         SportsEntity sportsEntity, boolean canceled) {
        this.id = id;
        this.memberEntity = memberEntity;
        this.sportsEntity = sportsEntity;
        this.canceled = canceled;
    }
}
