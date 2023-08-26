package com.study.proudcat.domain.post.controller;

import com.study.proudcat.domain.post.dto.request.ModifyPostRequest;
import com.study.proudcat.domain.post.dto.request.WritePostRequest;
import com.study.proudcat.domain.post.dto.response.FindPostResponse;
import com.study.proudcat.domain.post.dto.response.PostDetails;
import com.study.proudcat.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/picture")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시물 작성 테스트용")
    @PostMapping("/test")
    public ResponseEntity<Void> writePost(@RequestBody WritePostRequest request) {
        postService.writePostTest(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시물 작성(이미지 포함)", description = "게시물 작성 메서드입니다. 이미지 첨부를 포함합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> writePost(
            @RequestPart(value = "request") @Parameter(schema =@Schema(type = "string", format = "binary")) WritePostRequest request,
            @RequestPart(value = "image") MultipartFile image) throws IOException {
        postService.writePost(request, image);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시물 작성(이미지 DB에 저장. Frontend는 아직 안해도됨)", description = "이미지를 포함한 게시물 작성 메서드. 아직 FE는 신경 안써도됨요")
    @PostMapping(value = "/writeWithImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> writePostImageDB(
            @RequestPart(value = "request") @Parameter(schema =@Schema(type = "string", format = "binary")) WritePostRequest request,
            @RequestPart(value = "image") MultipartFile image) throws IOException {
        postService.writePostStoreImageDB(request, image);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시물 리스트 조회(DB에 저장된 이미지 포함. Frontend는 아직 안해도됨)", description = "전체 게시물 조회 메서드입니다.")
    @GetMapping("/listWithimage")
    public ResponseEntity<?> getPostsSearchListImage(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "sort", defaultValue = "createdAt", required = false) String sort) {
        Pageable pageable = PageRequest.of(0, 4, Sort.by(sort));
        return ResponseEntity.ok(postService.getPostsSearchListImage(title, pageable));
    }

//    @Operation(summary = "게시물 리스트 조회", description = "전체 게시물 조회 메서드입니다.")
//    @GetMapping
//    public ResponseEntity<?> getAllPosts() {
//        return ResponseEntity.ok(postService.getAllPosts());
//    }

    @Operation(summary = "게시물 전체 조회(페이징)", description = "제목으로 검색, 추천순/최신순 정렬 가능")
    @GetMapping("/list/paging")
    public ResponseEntity<?> getPostListPaging(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "sort", defaultValue = "createdAt", required = false) String sort) {
        Pageable pageable = PageRequest.of(0, 20, Sort.by(sort));
        return ResponseEntity.ok(postService.getPostsSearchList(title, pageable));
    }

    @Operation(summary = "게시물 상세 조회", description = "게시물 상세 조회 메서드입니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<FindPostResponse> getPostById(@PathVariable("postId") Long postId) throws IOException {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @Operation(summary = "게시물 상세조회(댓글, 좋아요)", description = "게시물 상세 조회 메서드입니다. 댓글과 좋아요 수를 포함합니다..")
    @GetMapping("/{postId}/comments")
    public ResponseEntity<PostDetails> getPostWithCommentsByPostId(@PathVariable(name = "postId") Long postId) throws IOException {
        postService.updatePostView(postId);
        return ResponseEntity.ok(postService.getPostWithCommentsById(postId));
    }

    @Operation(summary = "게시물 수정", description = "게시물 수정 메소드입니다.")
    @PutMapping("/{postId}")
    public ResponseEntity<Void> modifyPost(@PathVariable("postId") Long postId, @RequestBody ModifyPostRequest request) {
        postService.modifyPost(postId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시물 삭제", description = "게시물 삭제 메소드입니다.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시물 삭제(patchmapping. 추후 도전)", description = "게시물 삭제 메소드입니다.")
    @PatchMapping("/{postId}/patch")
    public ResponseEntity<Void> deletePostPatch(@PathVariable("postId") Long postId) {
        postService.deletePostPatch(postId);
        return ResponseEntity.noContent().build();
    }
}
