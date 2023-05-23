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
@RequestMapping("/proudcat/image")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @Operation(summary = "이미지 업로드", description = "multipart/form-data 형식의 이미지 리스트를 input으로 받습니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = storageService.uploadImage(file);
        return ResponseEntity.ok(uploadImage);
    }

    @Operation(summary = "이미지 다운로드", description = "이미지 fileNmae을 input으로 받습니다.")
    @GetMapping("{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable(name = "fileName") String fileName) {
        byte[] downloadImage = storageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(downloadImage);
    }
}
