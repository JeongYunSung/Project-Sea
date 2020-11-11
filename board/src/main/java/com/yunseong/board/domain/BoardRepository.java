package com.yunseong.board.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardQueryRepository {

    @Query("select distinct b from Board b inner join fetch b.recommender br left join fetch br.recommender brr where b.id = :id and b.isDelete = false")
    Optional<Board> findFetchById(long id);

    @Query(value = "select distinct b from Board b inner join fetch b.recommender br left join fetch br.recommender brr where b.isDelete = false and b.writer = :username and b.isDifferent = false order by b.id desc",
        countQuery = "select count(b) from Board b where b.isDelete = false and b.writer = :username and b.isDifferent = false")
    Page<Board> findMyBoards(String username, Pageable pageable);

    @Modifying
    @Query("update Board b set b.isDelete = false where b.id in(:ids)")
    void batchUndoUpdate(List<Long> ids);

    @Modifying
    @Query("update Board b set b.isDelete = true where b.id in(:ids)")
    void batchUpdate(List<Long> ids);
}
