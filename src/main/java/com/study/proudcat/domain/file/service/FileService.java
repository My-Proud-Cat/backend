package com.study.proudcat.domain.file.service;

import com.study.proudcat.domain.file.entity.FileData;
import com.study.proudcat.domain.file.repository.FileDataRepository;
import com.study.proudcat.infra.exception.ErrorCode;
import com.study.proudcat.infra.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileDataRepository fileDataRepository;
    private final String FOLDER_PATH = "C:\\study\\files\\";


    public String uploadImage(MultipartFile file) throws IOException {
        log.info("upload file : {}", file.getOriginalFilename());
        String filePath = FOLDER_PATH + file.getOriginalFilename();
        FileData fileData = fileDataRepository.save(
                FileData.builder()
                        .filename(file.getOriginalFilename())
                        .type(file.getContentType())
                        .filePath(filePath)
                        .build()
        );
        //filePath에 파일 저장
        file.transferTo(new File(filePath));

        return "file uploaded successfully! filePath : " + filePath;
    }

    public byte[] downloadImage(String fileName) throws IOException {
        FileData fileData = fileDataRepository.findByFilename(fileName)
                .orElseThrow(() -> new RestApiException(ErrorCode.NO_TARGET));

        String filePath = fileData.getFilePath();

        log.info("download fileName : {}", fileData.getFilename());
        log.info("download filePath: {}", fileData.getFilename());

        return Files.readAllBytes(new File(filePath).toPath());
    }
}
