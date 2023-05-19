package com.study.proudcat.domain.comment.service;

import com.study.proudcat.domain.comment.dto.CommentRequest;
import com.study.proudcat.domain.comment.entity.Comment;
import com.study.proudcat.domain.comment.repository.CommentRepository;
import com.study.proudcat.domain.post.entity.Post;
import com.study.proudcat.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public void writeComment(Long postId, CommentRequest request) {
        log.info("Comment service writeComment run..");
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post not found"));
        Comment parentComment = null;
        if (!ObjectUtils.isEmpty(request.getParentId())) {
            parentComment = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
            log.info("parentComment : {}", parentComment);
        }

        Comment comment = Comment.of(request, post);
        log.info("comment : {}", comment);

        if (parentComment != null) {
            parentComment.addChildComment(comment);
        }
        commentRepository.save(comment);
    }


}
