package com.study.proudcat.domain.file.controller;

import com.study.proudcat.domain.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/proudcat/api/image-file-path")
public class FileController {

    private final FileService fileService;

    @Operation(summary = "이미지 업로드(filePath를 db에 저장)", description = "filePath를 db에 저장하는 방식입니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = fileService.uploadImage(file);
        return ResponseEntity.ok(uploadImage);
    }

    @Operation(summary = "이미지 다운로드(filePath)", description = "db에 저장된 filePath에서 파일을 다운로드하는 방식입니다.")
    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable("fileName") String fileName) throws IOException{
        byte[] downloadImage = fileService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(downloadImage);
    }
}
