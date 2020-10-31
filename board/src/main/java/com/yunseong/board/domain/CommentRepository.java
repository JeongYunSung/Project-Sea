package com.yunseong.board.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentQueryRepository {

    @Query(value = "select c from Comment c join c.board b left join c.originalComment oc where c.isDelete = false and b.id = :id " +
            "order by coalesce(oc.id, c.id) asc, c.id asc", countQuery = "select c from Comment c join c.board b where c.isDelete = false and b.id = :id")
    Page<Comment> findPage2(long id, Pageable pageable);

    @Query("select c from Comment c left join fetch c.originalComment where c.isDelete = false and c.id = :id")
    Optional<Comment> findFetchById(long id);
}
