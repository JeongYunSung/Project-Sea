package com.yunseong.project.domain;

import com.yunseong.project.controller.ProjectSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ProjectQueryRepository {

    Page<Project> findBySearch(ProjectSearchCondition projectSearchCondition, Pageable pageable);
}
