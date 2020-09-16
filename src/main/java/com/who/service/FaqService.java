package com.who.service;

import com.who.domain.entity.FaqEntity;
import com.who.domain.repository.FaqRepository;
import com.who.dto.FaqDto;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class FaqService {
    private FaqRepository faqRepository;

    @Transactional
    public Long savePost(FaqDto faqDto) {
        return faqRepository.save(faqDto.toEntity()).getId();
    }

    @Transactional
    public List<FaqDto> getFaqlist() {
        List<FaqEntity> faqEntities = faqRepository.findAll();
        List<FaqDto> faqDtoList = new ArrayList<>();

        for (FaqEntity faqEntity : faqEntities) {
            FaqDto faqDto = FaqDto.builder()
                    .id(faqEntity.getId())
                    .title(faqEntity.getTitle())
                    .content(faqEntity.getContent())
                    .writer(faqEntity.getWriter())
                    .createdDate(faqEntity.getCreatedDate())
                    .build();

            faqDtoList.add(faqDto);
        }
        return faqDtoList;
    }

    @Transactional
    public FaqDto getPost(Long id) {
        Optional<FaqEntity> faqEntityWraper = faqRepository.findById(id);
        FaqEntity faqEntity = faqEntityWraper.get();

        FaqDto faqDto = FaqDto.builder()
                .id(faqEntity.getId())
                .title(faqEntity.getTitle())
                .content(faqEntity.getContent())
                .writer(faqEntity.getWriter())
                .createdDate(faqEntity.getCreatedDate())
                .build();

        return faqDto;
    }

    @Transactional
    public void deletePost(Long id) {
        faqRepository.deleteById(id);
    }

    @Transactional
    public List<FaqDto> searchPosts(String keyword) {
        List<FaqEntity> faqEntities = faqRepository.findByTitleContaining(keyword);
        List<FaqDto> faqDtoList = new ArrayList<>();

        if(faqEntities.isEmpty()) return faqDtoList;

        for(FaqEntity faqEntity : faqEntities) {
            faqDtoList.add(this.convertEntityToDto(faqEntity));
        }

        return faqDtoList;
    }

    private FaqDto convertEntityToDto(FaqEntity faqEntity) {
        return FaqDto.builder()
                .id(faqEntity.getId())
                .title(faqEntity.getTitle())
                .content(faqEntity.getContent())
                .writer(faqEntity.getWriter())
                .createdDate(faqEntity.getCreatedDate())
                .build();
    }
}
