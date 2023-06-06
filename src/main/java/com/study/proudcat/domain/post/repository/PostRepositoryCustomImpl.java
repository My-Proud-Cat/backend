package com.study.proudcat.domain.post.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.proudcat.domain.post.dto.request.FindPostRequest;
import com.study.proudcat.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.study.proudcat.domain.post.entity.QHeart.heart;
import static com.study.proudcat.domain.post.entity.QPost.post;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {

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

    @Override
    public Page<Post> findAllPostsPage(FindPostRequest request, Pageable pageable) {

        List<Post> content = queryFactory
                .selectFrom(post)
                .leftJoin(post.hearts, heart)
                .where(
                        titleEq(request.getTitle())
                )
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(titleEq(request.getTitle()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

    }

    private BooleanExpression titleEq(String title) {
        return StringUtils.hasText(title) ? post.title.contains(title) : null;
    }


}
