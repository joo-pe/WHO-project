package com.who.dto;

import com.who.domain.entity.FileEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileDto {
    private Long id;
    private String originFileName;
    private String fileName;
    private String filePath;

    public FileEntity toEntity() {
        FileEntity fileEntity = FileEntity.builder()
                .id(id)
                .originFileName(originFileName)
                .fileName(fileName)
                .filePath(filePath)
                .build();
        return fileEntity;
    }

    @Builder
    public FileDto(Long id, String originFileName, String fileName, String filePath) {
        this.id = id;
        this.originFileName = originFileName;
        this.fileName = fileName;
        this.filePath = filePath;
    }
}