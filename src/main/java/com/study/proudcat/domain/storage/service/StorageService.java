package com.study.proudcat.domain.storage.service;

import com.study.proudcat.domain.storage.entity.ImageData;
import com.study.proudcat.domain.storage.repository.StorageRepository;
import com.study.proudcat.infra.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private final StorageRepository storageRepository;

    public String uploadImage(MultipartFile file) throws IOException {
        log.info("upload file : {}", file);
        ImageData imageData = storageRepository.save(
                ImageData.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .imageData(ImageUtils.compressImage(file.getBytes()))
                        .build());
        log.info("imageData: {}", imageData);
        return "file uploaded successfully : " + file.getOriginalFilename();
    }

    public byte[] downloadImage(String fileName) {
        ImageData imageData = storageRepository.findByName(fileName)
                .orElseThrow(RuntimeException::new);

        log.info("download imageData : {}", imageData);

        return ImageUtils.decompressImage(imageData.getImageData());
    }
}
