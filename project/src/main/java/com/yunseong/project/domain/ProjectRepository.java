package com.yunseong.project.domain;

import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectQueryRepository {

    @Query("select p.id from Project p where p.projectState = 'POSTED' and p.lastDate <= :localDate")
    List<Long> findTargetBatch(LocalDate localDate);

    @Modifying
    @Query("update Project p set p.projectState = 'BATCH_PENDING' where p.id in(:ids)")
    void batchPendingUpdate(List<Long> ids);

    @Modifying
    @Query("update Project p set p.projectState = 'POSTED' where p.id in(:ids)")
    void batchUndoUpdate(List<Long> ids);

    @Modifying
    @Query("update Project p set p.projectState = 'CANCELLED' where p.id in(:ids)")
    void batchUpdate(List<Long> ids);
}
