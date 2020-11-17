package com.who.service;

import com.who.domain.entity.BookingEntity;
import com.who.domain.entity.MemberEntity;
import com.who.domain.entity.SportsEntity;
import com.who.domain.repository.BookingRepository;
import com.who.dto.BookingDto;
import com.who.dto.FaqDto;
import com.who.dto.MemberDto;
import com.who.dto.SportsDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BookingService {
    private BookingRepository bookingRepository;

    public Long saveBooking(BookingDto bookingDto) {
        return bookingRepository.save(bookingDto.toEntity()).getId();
    }

    @Transactional
    public List<BookingDto> getBookingList() {
        List<BookingEntity> bookingEntities = bookingRepository.findAll();
        List<BookingDto> bookingDtoList = new ArrayList<>();

        for (BookingEntity bookingEntity : bookingEntities) {
            bookingDtoList.add(this.convertEntityToDto(bookingEntity));
        }
        return bookingDtoList;
    }

    @Transactional
    public BookingDto getBooking(Long id) {
        Optional<BookingEntity> bookingEntityWraper = bookingRepository.findById(id);
        BookingEntity bookingEntity = bookingEntityWraper.get();

        return this.convertEntityToDto(bookingEntity);
    }

    public BookingDto convertEntityToDto(BookingEntity bookingEntity) {
        MemberEntity memberEntity = bookingEntity.getMemberEntity();
        MemberDto memberDto = MemberDto.builder()
                .id(memberEntity.getId())
                .email(memberEntity.getEmail())
                .name(memberEntity.getName())
                .phone(memberEntity.getPhone())
                .birthday(memberEntity.getBirthday())
                .createdDate(memberEntity.getCreatedDate())
                .enabled(memberEntity.isEnabled())
//                .roles(memberEntity.getRoles())
                .build();

        SportsEntity sportsEntity= bookingEntity.getSportsEntity();
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

        BookingDto bookingDto = BookingDto.builder()
                .id(bookingEntity.getId())
                .memberDto(memberDto)
                .sportsDto(sportsDto)
                .canceled(bookingEntity.isCanceled())
                .build();

        return bookingDto;
    }
}
