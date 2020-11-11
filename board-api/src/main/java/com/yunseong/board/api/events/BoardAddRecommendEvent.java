package com.yunseong.board.api.events;

import io.eventuate.tram.events.common.DomainEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BoardAddRecommendEvent implements DomainEvent {

    private long boardId;
    private Date recommendTime;
    private long value;
}
