package com.who.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "performance")
public class PerformanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String category;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String detail;

    @Column
    private Long fileId;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(length = 20, nullable = false)
    private String city;

    @Column(length = 20, nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDateTime ticketOpen;

    @Column(nullable = false)
    private LocalDateTime ticketClose;

    @Column(nullable = false)
    private String ticketMax;

    @Column(length = 20, nullable = false)
    private String host;

    @Column(nullable = false)
    private String rating;

    @Column(nullable = false)
    private String duration;

    @Column
    private LocalDateTime pStart;

    @Column
    private LocalDateTime pEnd;

    @Builder
    public PerformanceEntity(Long id, String category, String title, String detail,
                             Long fileId, LocalDateTime dateTime, String city, String location,
                             LocalDateTime ticketOpen, LocalDateTime ticketClose, String ticketMax,
                             String host, String rating, String duration,
                             LocalDateTime pStart, LocalDateTime pEnd) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.detail = detail;
        this.fileId = fileId;
        this.dateTime = dateTime;
        this.city = city;
        this.location = location;
        this.ticketOpen = ticketOpen;
        this.ticketClose = ticketClose;
        this.ticketMax = ticketMax;
        this.host = host;
        this.rating = rating;
        this.duration = duration;
        this.pStart = pStart;
        this.pEnd = pEnd;
    }
}
