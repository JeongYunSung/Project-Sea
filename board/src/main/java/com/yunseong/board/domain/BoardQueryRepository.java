package com.yunseong.board.domain;

import com.yunseong.board.controller.BoardSearchCondition;
import com.yunseong.board.controller.BoardSearchResponse;
import com.yunseong.board.controller.HotBoardSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardQueryRepository {

    Page<Board> findPageByQuery(BoardSearchCondition condition, Pageable pageable);

    List<Board> findHotBoards(HotBoardSearchCondition condition);
}
