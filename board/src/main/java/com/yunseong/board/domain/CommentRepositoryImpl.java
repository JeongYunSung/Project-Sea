package com.yunseong.board.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.yunseong.board.domain.QBoard.board;
import static com.yunseong.board.domain.QComment.comment;

@Repository
public class CommentRepositoryImpl {

    private final JPAQueryFactory jpaQueryFactory;

    public CommentRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<Comment> findPage(long boardId, Pageable pageable) {
        QComment original = new QComment("original");
        List<Comment> result = this.jpaQueryFactory
                .select(comment)
                .from(comment)
                .join(comment.board, board)
                .leftJoin(comment.originalComment, original)
                .where(comment.isDelete.isFalse(), board.id.eq(boardId))
                .orderBy(original.id.coalesce(comment.id).asc(), comment.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return PageableExecutionUtils.getPage(result, pageable,
                this.jpaQueryFactory
                        .select(comment.count())
                        .from(comment)
                        .join(comment.board, board)
                        .where(comment.isDelete.isFalse(), board.id.eq(boardId))::fetchCount);
    }
}
