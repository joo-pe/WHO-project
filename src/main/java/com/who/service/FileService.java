package com.who.service;

import com.who.domain.entity.FileEntity;
import com.who.domain.repository.FileRepository;
import com.who.dto.FileDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

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
        Optional<FileEntity> fileEntityWraper = fileRepository.findById(id);
        FileEntity fileEntity = fileEntityWraper.get();

        return this.convertEntityToDto(fileEntity);
    }

    public FileDto convertEntityToDto(FileEntity fileEntity) {
        FileDto fileDto = FileDto.builder()
                .id(fileEntity.getId())
                .originFileName(fileEntity.getOriginFileName())
                .fileName(fileEntity.getFileName())
                .filePath(fileEntity.getFilePath())
                .build();

        return fileDto;
    }
}
