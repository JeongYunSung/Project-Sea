package com.yunseong.team.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query(value = "select t.id from Team t inner join t.teamMembers tm where tm.teamMemberDetail.username = :username order by t.id desc",
    countQuery = "select t.id from Team t inner join t.teamMembers tm where tm.teamMemberDetail.username = :username")
    Page<Long> findByUsername(String username, Pageable pageable);

    @Query("select distinct t from Team t join fetch t.teamMembers tm where t.id = :id")
    Optional<Team> findFetchByTeamId(long id);

    @Modifying
    @Query("update Team t set t.teamState = 'RECRUIT_PENDING' where t.id in(:ids)")
    void batchUndoUpdate(List<Long> ids);

    @Modifying
    @Query("update Team t set t.teamState = 'CANCELLED' where t.id in(:ids)")
    void batchUpdate(List<Long> ids);
}
