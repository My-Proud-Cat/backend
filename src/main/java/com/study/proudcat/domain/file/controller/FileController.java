package com.study.proudcat.domain.file.controller;

import com.study.proudcat.domain.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
        String uploadImage = fileService.uploadFile(file);
        return ResponseEntity.ok(uploadImage);
    }
}
