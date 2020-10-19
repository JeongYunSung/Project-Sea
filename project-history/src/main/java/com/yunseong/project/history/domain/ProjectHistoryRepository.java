package com.yunseong.project.history.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectHistoryRepository extends JpaRepository, ProjectHistoryRedisRepository {
}
