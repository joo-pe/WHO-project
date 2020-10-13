package com.who.service;

import com.who.domain.entity.NoticeEntity;
import com.who.domain.repository.NoticeRepository;
import com.who.dto.NoticeDto;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class NoticeService {
    private NoticeRepository noticeRepository;

    @Transactional

    public Long savePost(NoticeDto noticeDto) {

        return noticeRepository.save(noticeDto.toEntity()).getId();
    }

    @Transactional
    public List<NoticeDto> getNoticelist() {
        List<NoticeEntity> noticeEntities = noticeRepository.findAll();
        List<NoticeDto> noticeDtoList = new ArrayList<>();

        for (NoticeEntity noticeEntity : noticeEntities) {
            NoticeDto noticeDto = NoticeDto.builder()
                    .id(noticeEntity.getId())
                    .title(noticeEntity.getTitle())
                    .content(noticeEntity.getContent())
                    .writer(noticeEntity.getWriter())
                    .createdDate(noticeEntity.getCreatedDate())
                    .build();

            noticeDtoList.add(noticeDto);
        }
        return noticeDtoList;
    }

    @Transactional
    public NoticeDto getPost(Long id) {
        Optional<NoticeEntity> noticeEntityWraper = noticeRepository.findById(id);
        NoticeEntity noticeEntity = noticeEntityWraper.get();

        NoticeDto noticeDto = NoticeDto.builder()
                .id(noticeEntity.getId())
                .title(noticeEntity.getTitle())
                .content(noticeEntity.getContent())
                .writer(noticeEntity.getWriter())
                .createdDate(noticeEntity.getCreatedDate())
                .build();

        return noticeDto;
    }

    
    @Transactional
    public void deletePost(Long id) {
        noticeRepository.deleteById(id);
    }

    @Transactional
    public List<NoticeDto> searchPosts(String keyword) {
        List<NoticeEntity> noticeEntities = noticeRepository.findByTitleContaining(keyword);
        List<NoticeDto> noticeDtoList = new ArrayList<>();

        if(noticeEntities.isEmpty()) return noticeDtoList;

        for(NoticeEntity noticeEntity : noticeEntities) {
        	noticeDtoList.add(this.convertEntityToDto(noticeEntity));
        }

        return noticeDtoList;
    }

    private NoticeDto convertEntityToDto(NoticeEntity noticeEntity) {
        return NoticeDto.builder()
                .id(noticeEntity.getId())
                .title(noticeEntity.getTitle())
                .content(noticeEntity.getContent())
                .writer(noticeEntity.getWriter())
                .createdDate(noticeEntity.getCreatedDate())
                .build();
    }
}
