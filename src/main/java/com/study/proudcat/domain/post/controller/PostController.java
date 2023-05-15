package com.study.proudcat.domain.post.controller;

import com.study.proudcat.domain.post.dto.request.WritePostReqeust;
import com.study.proudcat.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proudcat")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> writePost(@RequestBody WritePostReqeust request) {
        postService.writePost(request);
        return ResponseEntity.noContent().build();
    }
}
