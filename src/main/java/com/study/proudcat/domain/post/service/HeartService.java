package com.study.proudcat.domain.post.service;

import com.study.proudcat.domain.post.entity.Heart;
import com.study.proudcat.domain.post.entity.Post;
import com.study.proudcat.domain.post.repository.HeartRepository;
import com.study.proudcat.domain.post.repository.PostRepository;
import com.study.proudcat.domain.user.entity.User;
import com.study.proudcat.domain.user.repository.UserRepository;
import com.study.proudcat.infra.exception.ErrorCode;
import com.study.proudcat.infra.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean pushHeartBtn(Long postId, Long userId)  {
        log.info("Heart service addHeart run..");
        Post post = getPostById(postId);
        User user = getUserById(userId);
        boolean isPresent = heartRepository.existsByPostAndUser(post, user);

        if (isPresent) {
            Heart heart = heartRepository.findHeartByPostAndUser(post, user)
                    .orElseThrow(() -> new RestApiException(ErrorCode.NO_TARGET));
            heartRepository.delete(heart);
        } else {
            heartRepository.save(Heart.builder()
                    .post(post)
                    .user(user)
                    .build());
            return true;
        }
        return false;
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NO_TARGET));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NO_TARGET));
    }
}
