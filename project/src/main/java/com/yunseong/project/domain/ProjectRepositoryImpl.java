package com.yunseong.project.domain;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
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

    private JPAQueryFactory jpaQueryFactory;

    public ProjectRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Project> findBySearch(ProjectSearchCondition projectSearchCondition, Pageable pageable) {
        QueryResults<Project> result = this.jpaQueryFactory
                .select(project)
                .from(project)
                .where(project.projectState.eq(ProjectState.POSTED), containsSubject(projectSearchCondition.getSubject()), equalsTheme(projectSearchCondition.getProjectTheme()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression equalsTheme(ProjectTheme projectTheme) {
        return projectTheme == null ? project.projectTheme.eq(projectTheme) : null;
    }

    private BooleanExpression containsSubject(String subject) {
        return StringUtils.hasText(subject) && !subject.equalsIgnoreCase("null") ? project.subject.contains(subject) : null;
    }
}
