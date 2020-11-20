package com.yunseong.board.domain;


import com.yunseong.board.api.BoardCategory;
import com.yunseong.board.api.events.BoardAddRecommendEvent;
import com.yunseong.common.AlreadyExistedElementException;
import com.yunseong.common.CannotReviseBoardIfWriterNotWereException;
import io.eventuate.tram.events.common.DomainEvent;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Board {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    private String writer;

    private String subject;

    @Column(name = "content", columnDefinition = "MEDIUMTEXT")
    private String content;

    private long readCount;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recommender_name")
    private Recommender recommender;

    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    private boolean isDifferent;

    private boolean isDelete;

    public Board(String writer, String subject, String content, BoardCategory boardCategory, boolean isDifferent) {
        this.writer = writer;
        this.subject = subject;
        this.content = content;
        this.boardCategory = boardCategory;
        this.readCount = 0;
        this.isDifferent = isDifferent;
        this.isDelete = false;
        this.recommender = new Recommender();
    }

    public BoardAddRecommendEvent addRecommend(String username) throws ParseException {
        return this.recommender.addRecommender(username);
    }

    public void revise(String writer, BoardRevision boardRevision) {
        if(!this.writer.equals(writer)) throw new CannotReviseBoardIfWriterNotWereException("작성자가 아니면 수정할 수 없습니다");
        this.subject = boardRevision.getSubject();
        this.content = boardRevision.getContent();
    }

    public void delete(String writer) {
        if(!this.writer.equals(writer)) throw new CannotReviseBoardIfWriterNotWereException("작성자가 아니면 삭제할 수 없습니다");
        this.isDelete = true;
    }

    public void addReadCount() {
        this.readCount+=1;
    }
}
