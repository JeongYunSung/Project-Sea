package com.yunseong.board.messagehandlers;

import com.yunseong.board.api.BoardDetail;
import com.yunseong.board.api.BoardServiceChannels;
import com.yunseong.board.api.command.CreateBoardCommand;
import com.yunseong.board.api.command.CreateBoardReply;
import com.yunseong.board.api.command.DeleteBoardCommand;
import com.yunseong.board.api.command.ReviseBoardCommand;
import com.yunseong.board.controller.BoardCreateRequest;
import com.yunseong.board.domain.Board;
import com.yunseong.board.domain.BoardRevision;
import com.yunseong.board.service.BoardService;
import com.yunseong.board.service.FileService;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;
import static io.eventuate.tram.sagas.participant.SagaReplyMessageBuilder.withLock;

public class BoardCommandHandlers {

    @Autowired
    private BoardService boardService;
    @Autowired
    private FileService fileService;

    public CommandHandlers commandHandler() {
        return SagaCommandHandlersBuilder
                .fromChannel(BoardServiceChannels.boardServiceChannel)
                .onMessage(CreateBoardCommand.class, this::createBoard)
                .onMessage(ReviseBoardCommand.class, this::reviseBoard)
                .onMessage(DeleteBoardCommand.class, this::deleteBoard)
                .build();
    }

    private Message reviseBoard(CommandMessage<ReviseBoardCommand> cm) {
        ReviseBoardCommand command = cm.getCommand();
        BoardDetail boardDetail = cm.getCommand().getBoardDetail();
        try {
            Board board = this.boardService.reviseBoard(command.getBoardId(), boardDetail.getWriter(), new BoardRevision(boardDetail.getSubject(), boardDetail.getContent()));
            if(board == null) {
                return withFailure();
            }
            return withSuccess();
        } catch (Exception e) {
            return withFailure();
        }
    }

    private Message deleteBoard(CommandMessage<DeleteBoardCommand> cm) {
        this.boardService.delete(cm.getCommand().getBoardId(), cm.getCommand().getUsername());
        return withSuccess();
    }

    private Message createBoard(CommandMessage<CreateBoardCommand> cm) {
        CreateBoardCommand command = cm.getCommand();
        try {
            Board board = this.boardService.createBoard(command.getWriter(), new BoardCreateRequest(command.getSubject(), command.getContent(), command.getBoardCategory()), true);
            if(command.getImages() != null && command.getImages().length > 0) this.fileService.save(board.getId(), command.getImages());
            if(board != null){
                return withLock(Board.class, board.getId()).withSuccess(new CreateBoardReply(board.getId()));
            }
            return withFailure();
        } catch (Exception e) {
            return withFailure();
        }
    }
}
