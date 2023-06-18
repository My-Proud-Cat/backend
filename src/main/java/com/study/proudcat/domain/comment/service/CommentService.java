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

        Comment comment = Comment.of(request, post);
        log.info("comment : {}", comment);

        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId) {
        log.info("Comment service deleteComment run..");
        Comment comment = errorCheckComment(postId, commentId);
        commentRepository.delete(comment);
    }

    private Comment errorCheckComment(Long postId, Long commentId) {
        log.info("Comment service errorCheckComment run..");
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new IllegalArgumentException("no target");
        }
        return comment;
    }
}
