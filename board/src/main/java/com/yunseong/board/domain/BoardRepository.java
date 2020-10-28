package com.yunseong.board.domain;

import com.yunseong.board.controller.BoardDetailResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardQueryRepository {

    @Query("select distinct b from Board b left join b.recommend br where b.id = :id and b.isDelete = false")
    Optional<Board> findFetchById(long id);

    @Query("select distinct new com.yunseong.board.controller.BoardDetailResponse(b.writer, b.subject, b.content, b.boardCategory, count(br)) from Board b left join b.recommend br where b.id = :id and b.isDelete = false")
    BoardDetailResponse findFetchDtoById(long id);
}
