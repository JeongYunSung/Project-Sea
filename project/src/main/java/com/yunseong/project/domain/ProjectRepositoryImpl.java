package com.yunseong.project.domain;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yunseong.board.api.BoardCategory;
import com.yunseong.project.api.event.ProjectState;
import com.yunseong.project.controller.HotProjectSearchCondition;
import com.yunseong.project.controller.ProjectSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static com.yunseong.project.domain.QProject.project;
import static com.yunseong.project.domain.QProjectBoard.projectBoard;
import static com.yunseong.project.domain.QProjectRecommendStatistics.projectRecommendStatistics;

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
                .innerJoin(project.members, usernames).fetchJoin()
                .innerJoin(project.board, projectBoard).fetchJoin()
                .leftJoin(projectBoard.recommendStatistics, projectRecommendStatistics).fetchJoin();
        JPAQuery<Long> count = this.jpaQueryFactory.select(project.count())
                .from(project)
                .innerJoin(project.members, usernames)
                .innerJoin(project.board, projectBoard);
        if(projectSearchCondition.getProjectState() != ProjectState.POSTED) {
            from.where(project.isPublic.isTrue(), inUsername(projectSearchCondition, usernames), eqProjectState(projectSearchCondition), containsSubject(projectSearchCondition.getSubject()), equalsTheme(projectSearchCondition.getBoardCategory()));
            count.where(project.isPublic.isTrue(), inUsername(projectSearchCondition, usernames), eqProjectState(projectSearchCondition), containsSubject(projectSearchCondition.getSubject()), equalsTheme(projectSearchCondition.getBoardCategory()));
        }else {
            from.where(eqProjectState(projectSearchCondition), inUsername(projectSearchCondition, usernames), containsSubject(projectSearchCondition.getSubject()), equalsTheme(projectSearchCondition.getBoardCategory()));
            count.where(eqProjectState(projectSearchCondition), inUsername(projectSearchCondition, usernames), containsSubject(projectSearchCondition.getSubject()), equalsTheme(projectSearchCondition.getBoardCategory()));
        }
        List<Project> content = from
                .orderBy(project.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return PageableExecutionUtils.getPage(content, pageable, count::fetchCount);
    }

    @Override
    public List<Project> findHotProjects(HotProjectSearchCondition condition) {
        return this.jpaQueryFactory
                .select(project).distinct()
                .from(project)
                .where(this.equalsTheme(condition.getCategory()), projectRecommendStatistics.recommendDate.between(condition.getMinDate(), condition.getMaxDate()))
                .innerJoin(project.board, projectBoard).fetchJoin()
                .innerJoin(projectBoard.recommendStatistics, projectRecommendStatistics).fetchJoin()
                .groupBy(project.id)
                .orderBy(projectRecommendStatistics.value.sum().desc())
                .limit(condition.getSize()).fetch();
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
