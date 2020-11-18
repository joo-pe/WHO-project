package com.who.service;

import com.who.domain.entity.SportsEntity;
import com.who.domain.entity.Sports2Entity;
import com.who.domain.repository.Sports2Repository;
import com.who.domain.repository.SportsRepository;
import com.who.dto.SportsDto;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SportsService {
    private SportsRepository sportsRepository;
    private Sports2Repository sports2Repository;

    @Transactional
    public Long saveProduct(SportsDto sportsDto) {
        return sportsRepository.save(sportsDto.toEntity()).getId();
    }

    @Transactional
    public List<SportsDto> getSportsList() {
        List<SportsEntity> sportsEntities = sportsRepository.findAll();
        List<SportsDto> sportsDtoList = new ArrayList<>();

        for (SportsEntity sportsEntity : sportsEntities) {
            SportsDto sportsDto = SportsDto.builder()
                    .id(sportsEntity.getId())
                    .category(sportsEntity.getCategory())
                    .title(sportsEntity.getTitle())
                    .detail(sportsEntity.getDetail())
                    .fileId(sportsEntity.getFileId())
                    .dateTime(sportsEntity.getDateTime())
                    .city(sportsEntity.getCity())
                    .location(sportsEntity.getLocation())
                    .ticketOpen(sportsEntity.getTicketOpen())
                    .ticketClose(sportsEntity.getTicketClose())
                    .ticketMax(sportsEntity.getTicketMax())
                    .team1(sportsEntity.getTeam1())
                    .team2(sportsEntity.getTeam2())
                    .build();

            sportsDtoList.add(sportsDto);
        }
        return sportsDtoList;
    }

    @Transactional
    public SportsDto getSports(Long id) {
        Optional<SportsEntity> sportsEntityWraper = sportsRepository.findById(id);
        SportsEntity sportsEntity = sportsEntityWraper.get();

        SportsDto sportsDto = SportsDto.builder()
                .id(sportsEntity.getId())
                .category(sportsEntity.getCategory())
                .title(sportsEntity.getTitle())
                .detail(sportsEntity.getDetail())
                .fileId(sportsEntity.getFileId())
                .dateTime(sportsEntity.getDateTime())
                .city(sportsEntity.getCity())
                .location(sportsEntity.getLocation())
                .ticketOpen(sportsEntity.getTicketOpen())
                .ticketClose(sportsEntity.getTicketClose())
                .ticketMax(sportsEntity.getTicketMax())
                .team1(sportsEntity.getTeam1())
                .team2(sportsEntity.getTeam2())
                .build();

        return sportsDto;
    }

    @Transactional
    public void deleteSports(Long id) {
        sportsRepository.deleteById(id);
    }

    @Transactional
    public List<SportsDto> searchSports(String keyword) {
        List<SportsEntity> sportsEntities = sportsRepository.findByTitleContaining(keyword);
        List<SportsDto> sportsDtoList = new ArrayList<>();

        if(sportsEntities.isEmpty()) return sportsDtoList;

        for(SportsEntity sportsEntity : sportsEntities) {
            sportsDtoList.add(this.convertEntityToDto(sportsEntity));
        }

        return sportsDtoList;
    }

    private SportsDto convertEntityToDto(SportsEntity sportsEntity) {
        return SportsDto.builder()
                .id(sportsEntity.getId())
                .category(sportsEntity.getCategory())
                .title(sportsEntity.getTitle())
                .detail(sportsEntity.getDetail())
                .fileId(sportsEntity.getFileId())
                .dateTime(sportsEntity.getDateTime())
                .city(sportsEntity.getCity())
                .location(sportsEntity.getLocation())
                .ticketOpen(sportsEntity.getTicketOpen())
                .ticketClose(sportsEntity.getTicketClose())
                .ticketMax(sportsEntity.getTicketMax())
                .team1(sportsEntity.getTeam1())
                .team2(sportsEntity.getTeam2())
                .build();
    }
    
	/*---------*/   
    
    @Transactional
    public Long saveProduct2(SportsDto sports2Dto) {
        return sports2Repository.save(sports2Dto.to2Entity()).getId();
    }

    @Transactional
    public List<SportsDto> getSportsList2() {
        List<Sports2Entity> sportsEntities = sports2Repository.findAll();
        List<SportsDto> sportsDtoList = new ArrayList<>();

        for (Sports2Entity sports2Entity : sportsEntities) {
            SportsDto sportsDto = SportsDto.builder()
                    .id(sports2Entity.getId())
                    .category(sports2Entity.getCategory())
                    .title(sports2Entity.getTitle())
                    .detail(sports2Entity.getDetail())
                    .fileId(sports2Entity.getFileId())
                    .dateTime(sports2Entity.getDateTime())
                    .city(sports2Entity.getCity())
                    .location(sports2Entity.getLocation())
                    .ticketOpen(sports2Entity.getTicketOpen())
                    .ticketClose(sports2Entity.getTicketClose())
                    .ticketMax(sports2Entity.getTicketMax())
                    .team1(sports2Entity.getTeam1())
                    .team2(sports2Entity.getTeam2())
                    .build();

            sportsDtoList.add(sportsDto);
        }
        return sportsDtoList;
    }

    @Transactional
    public SportsDto getSports2(Long id) {
        Optional<Sports2Entity> sportsEntityWraper = sports2Repository.findById(id);
        Sports2Entity sports2Entity = sportsEntityWraper.get();

        SportsDto sportsDto = SportsDto.builder()
                .id(sports2Entity.getId())
                .category(sports2Entity.getCategory())
                .title(sports2Entity.getTitle())
                .detail(sports2Entity.getDetail())
                .fileId(sports2Entity.getFileId())
                .dateTime(sports2Entity.getDateTime())
                .city(sports2Entity.getCity())
                .location(sports2Entity.getLocation())
                .ticketOpen(sports2Entity.getTicketOpen())
                .ticketClose(sports2Entity.getTicketClose())
                .ticketMax(sports2Entity.getTicketMax())
                .team1(sports2Entity.getTeam1())
                .team2(sports2Entity.getTeam2())
                .build();

        return sportsDto;
    }

    @Transactional
    public void deleteSports2(Long id) {
        sportsRepository.deleteById(id);
    }

    @Transactional
    public List<SportsDto> searchSports2(String keyword) {
        List<Sports2Entity> sports2Entities = sports2Repository.findByTitleContaining(keyword);
        List<SportsDto> sportsDtoList = new ArrayList<>();

        if(sports2Entities.isEmpty()) return sportsDtoList;

        for(Sports2Entity sports2Entity : sports2Entities) {
            sportsDtoList.add(this.convertEntityToDto2(sports2Entity));
        }

        return sportsDtoList;
    }

    private SportsDto convertEntityToDto2(Sports2Entity sports2Entity) {
        return SportsDto.builder()
                .id(sports2Entity.getId())
                .category(sports2Entity.getCategory())
                .title(sports2Entity.getTitle())
                .detail(sports2Entity.getDetail())
                .fileId(sports2Entity.getFileId())
                .dateTime(sports2Entity.getDateTime())
                .city(sports2Entity.getCity())
                .location(sports2Entity.getLocation())
                .ticketOpen(sports2Entity.getTicketOpen())
                .ticketClose(sports2Entity.getTicketClose())
                .ticketMax(sports2Entity.getTicketMax())
                .team1(sports2Entity.getTeam1())
                .team2(sports2Entity.getTeam2())
                .build();
    }
}
