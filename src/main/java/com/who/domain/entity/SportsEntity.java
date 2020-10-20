package com.who.domain.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="sports")
public class SportsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=20, nullable = false)
    private String category;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String detail;

    @Column
    private Long fileId;

    @Column (nullable = false)
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

    @Column(nullable = false)
    private String team1;

    @Column(nullable = false)
    private String team2;





    @Builder
    public SportsEntity(Long id, String category, String title, String  detail,
                        Long fileId, LocalDateTime dateTime, String city, String location,
                        LocalDateTime ticketOpen, LocalDateTime ticketClose,
                        String ticketMax, String team1, String team2) {
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
        this.team1 = team1;
        this.team2 = team2;

    }

}