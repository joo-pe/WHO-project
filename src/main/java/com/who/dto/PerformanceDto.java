package com.who.dto;

import com.who.domain.entity.PerformanceEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PerformanceDto {
    private Long id;
    private String category;
    private String title;
    private String detail;
    private Long fileId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateTime;
    private String city;
    private String location;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime ticketOpen;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime ticketClose;
    private String ticketMax;
    private String host;
    private String rating;
    private String duration;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime pStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime pEnd;

    public PerformanceEntity toEntity() {
        PerformanceEntity performanceEntity = PerformanceEntity.builder()
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
                .host(host)
                .rating(rating)
                .duration(duration)
                .pStart(pStart)
                .pEnd(pEnd)
                .build();
        return performanceEntity;
    }

    @Builder
    public PerformanceDto(Long id, String category, String title, String detail,
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
        this. city = city;
        this.location = location;
        this.ticketOpen = ticketOpen;
        this.ticketClose = ticketClose;
        this.ticketMax = ticketMax;
        this.host = host;
        this.rating = rating;
        this.duration =duration;
        this.pStart = pStart;
        this.pEnd = pEnd;
    }
}
