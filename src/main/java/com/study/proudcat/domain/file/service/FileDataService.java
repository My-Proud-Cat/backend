package com.study.proudcat.domain.file.service;

import com.study.proudcat.domain.file.entity.FileData;
import com.study.proudcat.domain.file.repository.FileDataRepository;
import com.study.proudcat.infra.exception.ErrorCode;
import com.study.proudcat.infra.exception.RestApiException;
import com.study.proudcat.infra.utils.FileUtils;
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
public class FileDataService {

    private final FileDataRepository fileDataRepository;

    public String uploadImage(MultipartFile file) throws IOException {
        log.info("upload file : {}", file.getOriginalFilename());

        FileData fileData = FileUtils.parseFileInfo(file);

        if (fileData == null) {
            throw new RestApiException(ErrorCode.EMPTY_FILE);
        }
        fileDataRepository.save(fileData);

        return "file uploaded successfully! filePath : " + fileData.getFilePath();
    }

    public byte[] downloadImage(Long fileId) throws IOException {
        FileData fileData = fileDataRepository.findById(fileId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NO_TARGET));

        String filePath = fileData.getFilePath();

        log.info("download fileName : {}", fileData.getOrigFileName());
        log.info("download filePath: {}", fileData.getFilePath());

        return Files.readAllBytes(new File(filePath).toPath());
    }
}
