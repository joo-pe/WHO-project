package com.who.service;

import com.who.domain.entity.FileEntity;
import com.who.domain.entity.SportsEntity;
import com.who.domain.repository.SportsRepository;
import com.who.dto.FileDto;
import com.who.dto.SportsDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SportsService {
    private SportsRepository sportsRepository;
    private FileService fileService;

    @Transactional
    public Long saveProduct(SportsDto sportsDto) {
        return sportsRepository.save(sportsDto.toEntity()).getId();
    }

    @Transactional
    public List<SportsDto> getSportsList() {
        List<SportsEntity> sportsEntities = sportsRepository.findAll();
        List<SportsDto> sportsDtoList = new ArrayList<>();

        for (SportsEntity sportsEntity : sportsEntities) {
            sportsDtoList.add(this.convertEntityToDto(sportsEntity));
        }
        return sportsDtoList;
    }

    @Transactional
    public SportsDto getSports(Long id) {
        Optional<SportsEntity> sportsEntityWraper = sportsRepository.findById(id);
        SportsEntity sportsEntity = sportsEntityWraper.get();

        return this.convertEntityToDto(sportsEntity);
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

    @Transactional
    public Long getFileBySports(Long id) {
        return sportsRepository.getFileBySports(id);
    }

    public SportsDto convertEntityToDto(SportsEntity sportsEntity) {
        FileEntity fileEntity = sportsEntity.getFileEntity();
        FileDto fileDto = fileService.convertEntityToDto(fileEntity);

        return SportsDto.builder()
                .id(sportsEntity.getId())
                .category(sportsEntity.getCategory())
                .title(sportsEntity.getTitle())
                .detail(sportsEntity.getDetail())
                .fileDto(fileDto)
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
}