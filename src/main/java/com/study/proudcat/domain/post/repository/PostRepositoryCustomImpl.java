package com.study.proudcat.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.proudcat.domain.post.entity.Post;

import java.util.List;

import static com.study.proudcat.domain.post.entity.QHeart.heart;
import static com.study.proudcat.domain.post.entity.QPost.post;

public class PostRepositoryCustomImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<Post> findAllByHearts() {
        return queryFactory
                .selectFrom(post)
                .leftJoin(post.hearts, heart)
                .orderBy(post.hearts.size().desc())
                .fetch();
    }


}
