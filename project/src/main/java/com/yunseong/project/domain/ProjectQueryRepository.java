package com.yunseong.project.domain;

import com.yunseong.project.controller.ProjectSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectQueryRepository {

    Page<Project> findBySearch(ProjectSearchCondition projectSearchCondition, Pageable pageable);
}
