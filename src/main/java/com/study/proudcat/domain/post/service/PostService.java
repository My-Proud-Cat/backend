package com.study.proudcat.domain.post.service;

import com.study.proudcat.domain.file.entity.FileData;
import com.study.proudcat.domain.file.repository.FileDataRepository;
import com.study.proudcat.domain.post.dto.request.FindPostRequest;
import com.study.proudcat.domain.post.dto.request.ModifyPostRequest;
import com.study.proudcat.domain.post.dto.request.WritePostRequest;
import com.study.proudcat.domain.post.dto.response.FindPostResponse;
import com.study.proudcat.domain.post.dto.response.PostDetail;
import com.study.proudcat.domain.post.dto.response.PostListResponse;
import com.study.proudcat.domain.post.entity.Post;
import com.study.proudcat.domain.post.repository.PostRepository;
import com.study.proudcat.infra.exception.ErrorCode;
import com.study.proudcat.infra.exception.RestApiException;
import com.study.proudcat.infra.utils.FileUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final FileDataRepository fileDataRepository;


    @Transactional
    public void writePostTest(WritePostRequest request) {
        postRepository.save(request.toEntity());

    }

    @Transactional
    public void writePost(WritePostRequest request, MultipartFile image) throws IOException {
        log.info("PostService writePost run..");
        log.info("upload file : {}", image.getOriginalFilename());

        FileData fileData = FileUtils.parseFileInfo(image);
        if (fileData == null) {
            throw new RestApiException(ErrorCode.EMPTY_FILE);
        }
        fileDataRepository.save(fileData);

        Post post = request.toEntity();
        post.setFilePath(fileData.getFilePath());

        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<FindPostResponse> getAllPosts() {
        log.info("PostService getAllPosts run..");
        List<Post> posts = postRepository.findAll();
        List<FindPostResponse> responses = new ArrayList<>();
        posts.forEach(post -> {
            try {
                byte[] byteFile = getByteFile(post.getFilePath());
                responses.add(FindPostResponse.from(post, byteFile));
            } catch (IOException e) {
                log.error("전체 게시판 목록 조회 에러");
            }
        });
        return responses;
    }

    @Transactional(readOnly = true)
    public Page<PostListResponse> getPostsSearchList(FindPostRequest request, Pageable pageable) {
        log.info("PostService getPostsSearchList run..");
        Page<Post> postPage = postRepository.findAllPostsPage(request, pageable);
        return postPage.map(PostListResponse::from);
    }

    @Transactional(readOnly = true)
    public FindPostResponse getPostById(Long postId) throws IOException {
        log.info("PostService getPostById run..");
        Post post = getPostEntity(postId);

        return FindPostResponse.from(post, getByteFile(post.getFilePath()));
    }

    @Transactional(readOnly = true)
    public PostDetail getPostWithCommentsById(Long postId) {
        log.info("PostService getPostWithCommentsById run..");
        Post post = getPostEntity(postId);

        return PostDetail.from(post);
    }

    @Transactional
    public void updatePostView(Long postId) {
        log.info("PostService updatePostView run..");
        postRepository.updateView(postId);
    }

    @Transactional
    public void modifyPost(Long postId, ModifyPostRequest request) {
        log.info("PostService modifyPost run..");
        Post post = getPostEntity(postId);
        post.modify(request.getTitle(), request.getDescribe());
    }

    @Transactional
    public void deletePost(Long postId) {
        log.info("PostService deletePost run..");
        Post post = getPostEntity(postId);
        post.delete();
    }

    private Post getPostEntity(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NO_TARGET));
    }

    private byte[] getByteFile(String filePath) throws IOException {
        byte[] byteFile = null;
        try {
            byteFile = Files.readAllBytes(new File(filePath).toPath());
        } catch (IOException e) {
            System.out.println("File to byte[] 변환 에러");
        }
        return byteFile;
    }
}
