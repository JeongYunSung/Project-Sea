package com.yunseong.project.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectQueryRepository {

    @Query("select distinct p from Project p inner join fetch p.board b left join fetch b.recommendStatistics brs where p.board.id = :id")
    Optional<Project> findByBoardId(long id);

    @Query("select p from Project p where p.projectState = 'POSTED' and p.lastDate < :localDate")
    List<Project> findTargetBatch(Date localDate);

    @Query(value = "select distinct p from Project p inner join fetch p.members pm inner join fetch p.board pb left join fetch pb.recommendStatistics pbr where pm in(:username)",
        countQuery = "select count(p) from Project p inner join p.members pm where pm in(:username)")
    Page<Project> findMyProjects(String username, Pageable pageable);

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
