package com.study.proudcat.domain.file.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String origFileName;
    private String storedFileName;
    private String type;
    private String filePath;
    private Long fileSize;

    @Builder

    public FileData(String origFileName, String storedFileName, String type, String filePath, Long fileSize) {
        this.origFileName = origFileName;
        this.storedFileName = storedFileName;
        this.type = type;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
}
