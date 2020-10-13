package com.who.service;

import com.who.domain.entity.FileEntity;
import com.who.domain.repository.FileRepository;
import com.who.dto.FileDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class FileService {
    private FileRepository fileRepository;

    @Transactional
    public Long saveFile(FileDto fileDto) {
        return fileRepository.save(fileDto.toEntity()).getId();
    }

    @Transactional
    public FileDto getFile(Long id) {
        FileEntity fileEntity = fileRepository.findById(id).get();

        FileDto fileDto = FileDto.builder()
                .id(id)
                .originFileName(fileEntity.getFileName())
                .fileName(fileEntity.getFileName())
                .filePath(fileEntity.getFilePath())
                .build();
        return fileDto;
    }
}
