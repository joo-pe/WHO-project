package com.who.service;

import com.who.domain.entity.PerformanceEntity;
import com.who.domain.repository.PerformanceRepository;
import com.who.dto.PerformanceDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PerformanceService {
    private PerformanceRepository performanceRepository;

    @Transactional
    public Long saveProduct(PerformanceDto performanceDto) {
        return performanceRepository.save(performanceDto.toEntity()).getId();
    }

    @Transactional
    public List<PerformanceDto> getPerformaneList() {
        List<PerformanceEntity> performanceEntities = performanceRepository.findAll();
        List<PerformanceDto> performanceDtoList = new ArrayList<>();

        for(PerformanceEntity performanceEntity : performanceEntities) {
            PerformanceDto performanceDto = PerformanceDto.builder()
                    .id(performanceEntity.getId())
                    .category(performanceEntity.getCategory())
                    .title(performanceEntity.getTitle())
                    .detail(performanceEntity.getDetail())
                    .fileId(performanceEntity.getFileId())
                    .dateTime(performanceEntity.getDateTime())
                    .city(performanceEntity.getCity())
                    .location(performanceEntity.getLocation())
                    .ticketOpen(performanceEntity.getTicketOpen())
                    .ticketClose(performanceEntity.getTicketClose())
                    .ticketMax(performanceEntity.getTicketMax())
                    .host(performanceEntity.getHost())
                    .rating(performanceEntity.getRating())
                    .duration(performanceEntity.getDuration())
                    .pStart(performanceEntity.getPStart())
                    .pEnd(performanceEntity.getPEnd())
                    .build();

            performanceDtoList.add(performanceDto);
        }
        return performanceDtoList;
    }

    @Transactional
    public PerformanceDto getPerformance(Long id) {
        Optional<PerformanceEntity> performanceEntityWraper = performanceRepository.findById(id);
        PerformanceEntity performanceEntity = performanceEntityWraper.get();

        PerformanceDto performanceDto = PerformanceDto.builder()
                .id(performanceEntity.getId())
                .category(performanceEntity.getCategory())
                .title(performanceEntity.getTitle())
                .detail(performanceEntity.getDetail())
                .fileId(performanceEntity.getFileId())
                .dateTime(performanceEntity.getDateTime())
                .city(performanceEntity.getCity())
                .location(performanceEntity.getLocation())
                .ticketOpen(performanceEntity.getTicketOpen())
                .ticketClose(performanceEntity.getTicketClose())
                .ticketMax(performanceEntity.getTicketMax())
                .host(performanceEntity.getHost())
                .rating(performanceEntity.getRating())
                .duration(performanceEntity.getDuration())
                .pStart(performanceEntity.getPStart())
                .pEnd(performanceEntity.getPEnd())
                .build();

        return performanceDto;
    }

    @Transactional
    public void deletePerformance(Long id) {
        performanceRepository.deleteById(id);
    }

    @Transactional
    public List<PerformanceDto> searchPerformance(String keyword) {
        List<PerformanceEntity> performanceEntities = performanceRepository.findByTitleContaining(keyword);
        List<PerformanceDto> performanceDtoList = new ArrayList<>();

        if(performanceEntities.isEmpty()) return performanceDtoList;

        for(PerformanceEntity performanceEntity : performanceEntities) {
            performanceDtoList.add(this.convertEntityToDto(performanceEntity));
        }

        return performanceDtoList;
    }

    private PerformanceDto convertEntityToDto(PerformanceEntity performanceEntity) {
        return PerformanceDto.builder()
                .id(performanceEntity.getId())
                .category(performanceEntity.getCategory())
                .title(performanceEntity.getTitle())
                .detail(performanceEntity.getDetail())
                .fileId(performanceEntity.getFileId())
                .dateTime(performanceEntity.getDateTime())
                .city(performanceEntity.getCity())
                .location(performanceEntity.getLocation())
                .ticketOpen(performanceEntity.getTicketOpen())
                .ticketClose(performanceEntity.getTicketClose())
                .ticketMax(performanceEntity.getTicketMax())
                .host(performanceEntity.getHost())
                .rating(performanceEntity.getRating())
                .duration(performanceEntity.getDuration())
                .pStart(performanceEntity.getPStart())
                .pEnd(performanceEntity.getPEnd())
                .build();
    }

}
