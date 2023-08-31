package com.study.proudcat.domain.post.service;

import com.study.proudcat.domain.file.entity.FileData;
import com.study.proudcat.domain.file.repository.FileDataRepository;
import com.study.proudcat.domain.post.dto.request.ModifyPostRequest;
import com.study.proudcat.domain.post.dto.request.WritePostRequest;
import com.study.proudcat.domain.post.dto.response.FindPostResponse;
import com.study.proudcat.domain.post.dto.response.PostDetails;
import com.study.proudcat.domain.post.entity.Post;
import com.study.proudcat.domain.post.repository.PostRepository;
import com.study.proudcat.domain.storage.repository.StorageRepository;
import com.study.proudcat.domain.user.entity.User;
import com.study.proudcat.domain.user.repository.UserRepository;
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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final FileDataRepository fileDataRepository;
    private final StorageRepository storageRepository;
    private final UserRepository userRepository;

    @Transactional
    public void writePost(WritePostRequest request, MultipartFile image, String email) throws IOException {
        log.info("PostService writePost run..");
        log.info("upload file : {}", image.getOriginalFilename());

        User writer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException(ErrorCode.NO_TARGET));

        FileData fileData = FileUtils.parseFileInfo(image);
        if (fileData == null) {
            throw new RestApiException(ErrorCode.EMPTY_FILE);
        }
        fileDataRepository.save(fileData);

        Post post = request.toEntity(writer);
        post.setFileId(fileData.getId());

        postRepository.save(post);
    }

//    @Transactional
//    public void writePostStoreImageDB(WritePostRequest request, MultipartFile image) throws IOException {
//        log.info("PostService writePostStoreImageDB run..");
//        ImageData imageData = storageRepository.save(
//                ImageData.builder()
//                        .name(image.getOriginalFilename())
//                        .type(image.getContentType())
//                        .imageData(ImageUtils.compressImage(image.getBytes()))
//                        .build());
//
//        Post post = request.toEntity();
//        post.setFilePath(image.getOriginalFilename());
//
//        postRepository.save(post);
//    }


//    @Transactional(readOnly = true)
//    public List<FindPostResponse> getAllPosts() {
//        log.info("PostService getAllPosts run..");
//        List<Post> posts = postRepository.findAll();
//        List<FindPostResponse> responses = new ArrayList<>();
//        posts.forEach(post -> {
//            try {
//                byte[] byteFile = getByteFile(post.getFilePath());
//                responses.add(FindPostResponse.from(post, byteFile));
//            } catch (IOException e) {
//                log.error("전체 게시판 목록 조회 에러");
//            }
//        });
//        return responses;
//    }

    @Transactional(readOnly = true)
    public List<FindPostResponse> getPostsSearchList(String title, Pageable pageable) {
        log.info("PostService getPostsSearchList run..");
        Page<Post> postPage = postRepository.findAllPostsPage(title, pageable);
        return postPage.getContent()
                .stream()
                .map(FindPostResponse::from)
                .collect(Collectors.toList());
    }

//    @Transactional(readOnly = true)
//    public List<FindPostResponse> getPostsSearchListImage(String title, Pageable pageable) {
//        Page<Post> postPage = postRepository.findAllPostsPage(title, pageable);
//        List<FindPostResponse> responses = new ArrayList<>();
//
//        postPage.forEach(post -> {
//            String filename = post.getFilePath();
//            ImageData image = storageRepository.findByName(filename)
//                    .orElseThrow(() -> new RestApiException(ErrorCode.NO_TARGET));
//            responses.add(FindPostResponse.from(post));
//        });
//
//        return responses;
//    }


    @Transactional(readOnly = true)
    public FindPostResponse getPostById(Long postId) {
        log.info("PostService getPostById run..");
        Post post = getPostEntity(postId);

        return FindPostResponse.from(post);
    }

    @Transactional(readOnly = true)
    public PostDetails getPostWithCommentsById(Long postId) {
        log.info("PostService getPostWithCommentsById run..");
        Post post = getPostEntity(postId);
        return PostDetails.from(post);
    }

    @Transactional
    public void updatePostView(Long postId) {
        log.info("PostService updatePostView run..");
        postRepository.updateView(postId);
    }

    @Transactional
    public void modifyPost(Long postId, ModifyPostRequest request, String email) {
        log.info("PostService modifyPost run..");
        Post post = getPostEntity(postId);

        if (!post.isSameWriter(email)) {
            throw new RestApiException(ErrorCode.NOT_PROPER_USER);
        }
        post.modify(request.getTitle(), request.getDescribe());
    }

    @Transactional
    public void deletePost(Long postId, String email) {
        log.info("PostService deletePost run..");
        Post post = getPostEntity(postId);

        if (!post.isSameWriter(email)) {
            throw new RestApiException(ErrorCode.NOT_PROPER_USER);
        }
        postRepository.delete(post);
    }

    @Transactional  //추후 도전
    public void deletePostPatch(Long postId) {
        log.info("PostService deletePostPatch run..");
        Post post = getPostEntity(postId);
        post.delete();
    }

    private Post getPostEntity(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NO_TARGET));
    }
}
