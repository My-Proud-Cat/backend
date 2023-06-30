package com.study.proudcat.domain.storage.controller;

import com.study.proudcat.domain.storage.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/proudcat/api/image-db-storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @Operation(summary = "이미지 업로드(db에 저장)", description = "업로드한 이미지 파일을 db에 저장합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = storageService.uploadImage(file);
        return ResponseEntity.ok(uploadImage);
    }

    @Operation(summary = "이미지 다운로드(db에서 다운)", description = "이미지 fileName을 파라미터로 받아 db에 저장된 이미지를 다운로드합니다.")
    @GetMapping("{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable(name = "fileName") String fileName) {
        byte[] downloadImage = storageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(downloadImage);
    }
}
