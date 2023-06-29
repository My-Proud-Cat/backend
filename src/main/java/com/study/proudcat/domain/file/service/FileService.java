package com.study.proudcat.domain.file.service;

import com.study.proudcat.domain.file.entity.FileData;
import com.study.proudcat.domain.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileRepository fileRepository;
    private final String FOLDER_PATH = "C:\\study\\files\\";


    public String uploadFile(MultipartFile file) throws IOException {
        log.info("upload file : {}", file.getOriginalFilename());
        String filePath = FOLDER_PATH + file.getOriginalFilename();
        FileData fileData = fileRepository.save(
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
}
