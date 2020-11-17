package com.who.service;

import com.who.domain.entity.SeatEntity;
import com.who.domain.entity.SportsEntity;
import com.who.domain.repository.SeatRepository;
import com.who.dto.SeatDto;
import com.who.dto.SportsDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.util.Pair;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SeatService {
    private SeatRepository seatRepository;

    @Transactional
    public List<SeatDto> getSeatList() {
        List<SeatEntity> seatEntities = seatRepository.findAll();
        List<SeatDto> seatDtoList = new ArrayList<>();

        for (SeatEntity seatEntity : seatEntities) {
            seatDtoList.add(this.convertEntityToDto(seatEntity));
        }
        return seatDtoList;
    }

    @Transactional
    public SeatDto getSeat(Long id) {
        Optional<SeatEntity> seatEntityWraper = seatRepository.findById(id);
        SeatEntity seatEntity = seatEntityWraper.get();

        return this.convertEntityToDto(seatEntity);
    }

    @Transactional
    public void initialize(Long id) {
        seatRepository.initialize(id);
    }

    @Transactional
    public List<Object[]> countAvailableSeat() {
        return seatRepository.countAvailableSeat();
    }

    public SeatDto convertEntityToDto(SeatEntity seatEntity) {
        SportsEntity sportsEntity= seatEntity.getSportsEntity();
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

        SeatDto seatDto = SeatDto.builder()
                .id(seatEntity.getId())
                .area(seatEntity.getArea())
                .price(seatEntity.getPrice())
                .allocated(seatEntity.isAllocated())
                .sportsDto(sportsDto)
                .build();

        return seatDto;
    }
}
