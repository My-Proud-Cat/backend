package com.study.proudcat.domain.post.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.proudcat.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.study.proudcat.domain.post.entity.QHeart.heart;
import static com.study.proudcat.domain.post.entity.QPost.post;
import static org.springframework.util.ObjectUtils.isEmpty;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Post> findAllPostsPage(String title, Pageable pageable) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<Post> content = queryFactory
                .selectFrom(post)
                .leftJoin(post.hearts, heart)
                .where(
                        titleEq(title)
                )
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(titleEq(title));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression titleEq(String title) {
        return StringUtils.hasText(title) ? post.title.contains(title) : null;
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = Order.DESC;

                switch (order.getProperty()) {
                    case "createdAt":
                        ORDERS.add(new OrderSpecifier(direction, post.createdAt));
                        break;
                    case "hearts":
                        ORDERS.add(new OrderSpecifier(direction, post.hearts.size()));
                    default:
                        break;
                }
            }
        }
        return ORDERS;
    }
}
