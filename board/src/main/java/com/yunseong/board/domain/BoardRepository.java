package com.yunseong.board.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardQueryRepository {

    @Query("select distinct b from Board b left join b.recommend br where b.id = :id and b.isDelete = false")
    Optional<Board> findFetchById(long id);

    @Query("select distinct b from Board b left join fetch b.recommend br where b.id = :id and b.isDelete = false")
    Optional<Board> findFetchDtoById(long id);
}
