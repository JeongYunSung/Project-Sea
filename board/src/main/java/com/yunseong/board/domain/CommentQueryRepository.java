package com.yunseong.board.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentQueryRepository {

    Page<Comment> findPage(long boardId, Pageable pageable);
}
