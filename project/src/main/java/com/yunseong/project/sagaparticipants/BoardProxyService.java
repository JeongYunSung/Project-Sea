package com.yunseong.project.sagaparticipants;

import com.yunseong.board.api.BoardServiceChannels;
import com.yunseong.board.api.command.CreateBoardCommand;
import com.yunseong.board.api.command.CreateBoardReply;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import org.springframework.stereotype.Component;

@Component
public class BoardClassProxyService {

    public final CommandEndpoint<CreateBoardCommand> create = CommandEndpointBuilder
            .forCommand(CreateBoardCommand.class)
            .withChannel(BoardServiceChannels.boardServiceChannel)
            .withReply(CreateBoardReply.class)
            .build();
}
