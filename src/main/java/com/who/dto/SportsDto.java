package com.who.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.who.domain.entity.SportsEntity;
import com.who.domain.entity.Sports2Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SportsDto {
    private Long id;
    private String category;
    private String title;
    private String detail;
    private FileDto fileDto;
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
                .fileEntity(fileDto.toEntity())
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
    
    public Sports2Entity to2Entity() {
        Sports2Entity sports2Entity = Sports2Entity.builder()
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
        return sports2Entity;
    }

    @Builder
    public SportsDto(Long id, String category, String title, String detail,
                     FileDto fileDto, LocalDateTime dateTime, String city, String location,
                     LocalDateTime ticketOpen, LocalDateTime ticketClose, String ticketMax,
                     String team1, String team2) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.detail = detail;
        this.fileDto = FileDto.builder().build();
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
