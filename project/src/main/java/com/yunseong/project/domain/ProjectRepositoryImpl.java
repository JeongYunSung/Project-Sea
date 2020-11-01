package com.yunseong.project.domain;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yunseong.project.api.event.ProjectState;
import com.yunseong.project.api.event.ProjectTheme;
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
            from.where(project.isPublic.isTrue(), eqProjectState(projectSearchCondition), containsSubject(projectSearchCondition.getSubject()), equalsTheme(projectSearchCondition.getProjectTheme()));
        }else {
            from.where(eqProjectState(projectSearchCondition), usernames.in(projectSearchCondition.getUsername()), containsSubject(projectSearchCondition.getSubject()), equalsTheme(projectSearchCondition.getProjectTheme()));
        }
        QueryResults<Project> result = from
                .orderBy(project.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression eqProjectState(ProjectSearchCondition projectSearchCondition) {
        return projectSearchCondition.getProjectState() != null ? project.projectState.eq(projectSearchCondition.getProjectState()) : null;
    }

    private BooleanExpression equalsTheme(ProjectTheme projectTheme) {
        return projectTheme != null ? project.projectTheme.eq(projectTheme) : null;
    }

    private BooleanExpression containsSubject(String subject) {
        return StringUtils.hasText(subject) && !subject.equalsIgnoreCase("null") ? project.subject.contains(subject) : null;
    }
}
