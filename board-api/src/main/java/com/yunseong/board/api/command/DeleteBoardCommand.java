package com.yunseong.board.api.command;

import io.eventuate.tram.commands.common.Command;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DeleteBoardCommand implements Command {

    private long boardId;
    private String username;
}
