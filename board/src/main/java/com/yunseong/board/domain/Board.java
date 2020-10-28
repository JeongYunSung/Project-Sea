package com.yunseong.board.domain;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Board {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    private String writer;

    private String subject;

    private String content;

    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "recommenders", joinColumns = @JoinColumn(name = "board_id"))
    @Column(name = "recommender")
    private final Set<String> recommend = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    private boolean isDelete;

    public Board(String writer, String subject, String content, BoardCategory boardCategory) {
        this.writer = writer;
        this.subject = subject;
        this.content = content;
        this.boardCategory = boardCategory;
        this.isDelete = false;
    }

    public void addRecommend(String username) {
        this.recommend.add(username);
    }

    public void revise(BoardRevision boardRevision) {
        this.subject = boardRevision.getSubject();
        this.content = boardRevision.getContent();
    }

    public void delete() {
        this.isDelete = true;
    }
}