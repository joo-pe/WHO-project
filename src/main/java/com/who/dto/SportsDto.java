package com.who.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.who.domain.entity.SportsEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;
import org.apache.tomcat.jni.Local;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SportsDto {
    private Long id;
    private String category;
    private String title;
    private String detail;
    private Long fileId;
    @DateTimeFormat(pattern ="yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateTime;
    private String city;
    private String location;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime ticketOpen;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime ticketClose;
    private String ticketMax;
    private String team1;
    private String team2;

    public SportsEntity toEntity() {
        SportsEntity sportsEntity = SportsEntity.builder()
                .id(id)
                .category(category)
                .title(title)
                .detail(detail)
                .fileId(fileId)
                .dateTime(dateTime)
                .city(city)
                .location(location)
                .ticketOpen(ticketOpen)
                .ticketClose(ticketClose)
                .ticketMax(ticketMax)
                .team1(team1)
                .team2(team2)
                .build();
        return sportsEntity;
    }

    @Builder
    public SportsDto(Long id, String category, String title, String detail,
                     Long fileId, LocalDateTime dateTime, String city, String location,
                     LocalDateTime ticketOpen, LocalDateTime ticketClose, String ticketMax,
                     String team1, String team2) {
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