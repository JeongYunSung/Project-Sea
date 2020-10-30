package com.yunseong.board.domain;

import com.yunseong.board.service.CannotReviseBoardIfWriterNotWereException;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_name")
    private Board board;

    private String writer;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_comment_name")
    private Comment originalComment;

    @Enumerated(EnumType.STRING)
    private CommentState commentState;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    private boolean isDelete;

    public Comment(@NonNull Board board, @NonNull String writer, @NonNull String content, Comment originalComment, CommentState commentState) {
        this.board = board;
        this.writer = writer;
        this.content = content;
        this.originalComment = originalComment;
        this.commentState = commentState;
        this.isDelete = false;
    }

    public void revise(String writer, String content) {
        if(!this.writer.equals(writer)) throw new CannotReviseBoardIfWriterNotWereException("작성자가 아니면 수정할 수 없습니다");
        this.content = content;
    }

    public void delete(String writer) {
        if(!this.writer.equals(writer)) throw new CannotReviseBoardIfWriterNotWereException("작성자가 아니면 삭제할 수 없습니다");
        this.isDelete = true;
    }
}
