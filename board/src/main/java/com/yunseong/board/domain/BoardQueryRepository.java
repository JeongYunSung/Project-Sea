package com.yunseong.board.domain;

import com.yunseong.board.controller.BoardSearchCondition;
import com.yunseong.board.controller.BoardSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardQueryRepository {

    Page<BoardSearchResponse> findPageByQuery(BoardSearchCondition condition, Pageable pageable);
}
