package com.yunseong.board.api.command;

import com.yunseong.board.api.BoardDetail;
import io.eventuate.tram.commands.common.Command;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ReviseBoardCommand implements Command {

    private long boardId;
    private BoardDetail boardDetail;
}
