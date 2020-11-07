package com.yunseong.project.domain;

import com.yunseong.board.api.BoardCategory;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter @Setter
public class Board {

    private String writer;
    private String subject;
    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;
}
