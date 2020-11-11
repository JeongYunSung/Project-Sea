package com.yunseong.board.domain;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yunseong.board.api.BoardCategory;
import com.yunseong.board.controller.BoardSearchCondition;
import com.yunseong.board.controller.HotBoardSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.yunseong.board.domain.QBoard.board;
import static com.yunseong.board.domain.QRecommendStatistics.recommendStatistics;

@Repository
public class BoardRepositoryImpl implements BoardQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QRecommender recommender = new QRecommender("myRecommender");

    public BoardRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Board> findPageByQuery(BoardSearchCondition condition, Pageable pageable) {
        StringPath sRecommender = Expressions.stringPath("sRecommender");
        List<Board> content = this.jpaQueryFactory
                .select(board).distinct()
                .from(board)
                .where(board.isDelete.isFalse(), board.isDifferent.isFalse(), eqCategory(condition.getCategory()), containsSubject(condition), containsWriter(condition))
                .innerJoin(board.recommender, recommender).fetchJoin()
                .leftJoin(recommender.recommender, sRecommender).fetchJoin()
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return PageableExecutionUtils.getPage(content, pageable,
                this.jpaQueryFactory
                        .select(board.count())
                        .from(board)
                        .where(eqCategory(condition.getCategory()), containsSubject(condition), containsWriter(condition), board.isDelete.isFalse())::fetchCount);
    }

    @Override
    public List<Board> findHotBoards(HotBoardSearchCondition condition) {
        return this.jpaQueryFactory
                .select(board).distinct()
                .from(board)
                .where(this.eqCategory(condition.getCategory()), recommendStatistics.recommendDate.between(condition.getMinDate(), condition.getMaxDate()))
                .innerJoin(board.recommender, recommender).fetchJoin()
                .innerJoin(recommender.recommendStatistics, recommendStatistics).fetchJoin()
                .groupBy(board.id)
                .orderBy(recommendStatistics.value.sum().desc())
                .limit(condition.getSize()).fetch();
    }

    private BooleanExpression containsWriter(BoardSearchCondition condition) {
        return (condition.getWriter() != null && StringUtils.hasText(condition.getWriter())) ? board.writer.contains(condition.getWriter()) : null;
    }

    private BooleanExpression containsSubject(BoardSearchCondition condition) {
        return (condition.getSubject() != null && StringUtils.hasText(condition.getSubject())) ? board.subject.contains(condition.getSubject()) : null;
    }

    private BooleanExpression eqCategory(BoardCategory category) {
        return category != null ? board.boardCategory.eq(category) : null;
    }
}
