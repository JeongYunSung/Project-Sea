package com.yunseong.project.domain;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yunseong.board.api.BoardCategory;
import com.yunseong.project.api.event.ProjectState;
import com.yunseong.project.controller.ProjectSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import static com.yunseong.project.domain.QProject.project;

@Repository
public class ProjectRepositoryImpl implements ProjectQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public ProjectRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Project> findBySearch(ProjectSearchCondition projectSearchCondition, Pageable pageable) {
        StringPath usernames = Expressions.stringPath("usernames");
        JPAQuery<Project> from = this.jpaQueryFactory
                .select(project)
                .from(project)
                .innerJoin(project.members, usernames);
        if(projectSearchCondition.getProjectState() != ProjectState.POSTED) {
            from.where(project.isPublic.isTrue(), eqProjectState(projectSearchCondition), containsSubject(projectSearchCondition.getSubject()), equalsTheme(projectSearchCondition.getBoardCategory()));
        }else {
            from.where(eqProjectState(projectSearchCondition), inUsername(projectSearchCondition, usernames), containsSubject(projectSearchCondition.getSubject()), equalsTheme(projectSearchCondition.getBoardCategory()));
        }
        QueryResults<Project> result = from
                .orderBy(project.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression inUsername(ProjectSearchCondition projectSearchCondition, StringPath usernames) {
        return projectSearchCondition.getUsername() != null && StringUtils.hasText(projectSearchCondition.getSubject()) ? usernames.in(projectSearchCondition.getUsername()) : null;
    }

    private BooleanExpression eqProjectState(ProjectSearchCondition projectSearchCondition) {
        return projectSearchCondition.getProjectState() != null ? project.projectState.eq(projectSearchCondition.getProjectState()) : null;
    }

    private BooleanExpression equalsTheme(BoardCategory boardCategory) {
        return boardCategory != null ? project.board.boardCategory.eq(boardCategory) : null;
    }

    private BooleanExpression containsSubject(String subject) {
        return StringUtils.hasText(subject) && !subject.equalsIgnoreCase("null") ? project.board.subject.contains(subject) : null;
    }
}
