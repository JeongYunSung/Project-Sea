package com.yunseong.board.domain;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yunseong.board.controller.BoardSearchCondition;
import com.yunseong.board.controller.BoardSearchResponse;
import com.yunseong.board.controller.QBoardSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.yunseong.board.domain.QBoard.board;

@Repository
public class BoardRepositoryImpl implements BoardQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public BoardRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<BoardSearchResponse> findPageByQuery(BoardSearchCondition condition, Pageable pageable) {
        List<BoardSearchResponse> content = this.jpaQueryFactory
                .select(new QBoardSearchResponse(board.id, board.subject, board.writer, board.boardCategory, board.createdTime, board.recommend.size().longValue())).distinct()
                .from(board)
                .where(eqCategory(condition), containsSubject(condition), containsWriter(condition), board.isDelete.isFalse())
                .leftJoin(board.recommend)
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return PageableExecutionUtils.getPage(content, pageable,
                this.jpaQueryFactory
                        .select(board)
                        .from(board)
                        .where(eqCategory(condition), containsSubject(condition), containsWriter(condition), board.isDelete.isFalse())::fetchCount);
    }

    private BooleanExpression containsWriter(BoardSearchCondition condition) {
        return (condition.getWriter() != null && StringUtils.hasText(condition.getWriter())) ? board.writer.contains(condition.getWriter()) : null;
    }

    private BooleanExpression containsSubject(BoardSearchCondition condition) {
        return (condition.getSubject() != null && StringUtils.hasText(condition.getSubject())) ? board.subject.contains(condition.getSubject()) : null;
    }

    private BooleanExpression eqCategory(BoardSearchCondition condition) {
        return condition.getCategory() != null ? board.boardCategory.eq(condition.getCategory()) : null;
    }
}
