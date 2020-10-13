package com.who.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "file")
public class FileEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String originFileName; //업로드된 실테 파일 명

    @Column(nullable = false)
    private String fileName; //서버에 저장된 파일명

    @Column(nullable = false)
    private String filePath; //파일이 서버에 저장된 위치

    @Builder
    public FileEntity(Long id, String originFileName, String fileName, String filePath) {
        this.id = id;
        this.originFileName = originFileName;
        this.fileName = fileName;
        this.filePath =filePath;
    }
}