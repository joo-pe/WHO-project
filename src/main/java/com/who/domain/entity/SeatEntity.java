package com.who.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "KFAseat")
public class SeatEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String area;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private boolean allocated;

    @ManyToOne(targetEntity = SportsEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "sports_id")
    private SportsEntity sportsEntity;

    @Builder
    public SeatEntity(Long id, String area, Integer price,
                      boolean allocated, SportsEntity sportsEntity) {
        this.id = id;
        this.area = area;
        this.price = price;
        this.allocated = allocated;
        this.sportsEntity = sportsEntity;
    }
}
