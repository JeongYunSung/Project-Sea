package com.yunseong.board.messagehandlers;

import com.yunseong.board.api.BoardDetail;
import com.yunseong.board.api.BoardServiceChannels;
import com.yunseong.board.api.command.*;
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
                .onMessage(BatchBoardCommand.class, this::batchBoard)
                .onMessage(BatchUndoBoardCommend.class, this::batchUndoBoard)
                .build();
    }

    private Message batchBoard(CommandMessage<BatchBoardCommand> batchBoardCommandCommandMessage) {
        if(this.boardService.batchBoard(batchBoardCommandCommandMessage.getCommand().getBoardIds())) return withSuccess();
        return withFailure();
    }

    private Message batchUndoBoard(CommandMessage<BatchUndoBoardCommend> batchUndoBoardCommendCommandMessage) {
        this.boardService.batchUndoBoard(batchUndoBoardCommendCommandMessage.getCommand().getBoardIds());
        return withSuccess();
    }

    private Message reviseBoard(CommandMessage<ReviseBoardCommand> cm) {
        ReviseBoardCommand command = cm.getCommand();
        BoardDetail boardDetail = cm.getCommand().getBoardDetail();
        try {
            Board board = this.boardService.reviseBoard(command.getBoardId(), boardDetail.getWriter(), new BoardRevision(boardDetail.getSubject(), boardDetail.getContent()));
            this.fileService.save(board.getId(), boardDetail.getImages());
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
        BoardDetail boardDetail = cm.getCommand().getBoardDetail();
        try {
            Board board = this.boardService.createBoard(boardDetail.getWriter(), new BoardCreateRequest(boardDetail.getSubject(), boardDetail.getContent(), boardDetail.getBoardCategory()), true);
            this.fileService.save(board.getId(), boardDetail.getImages());
            return withLock(Board.class, board.getId()).withSuccess(new CreateBoardReply(board.getId()));
        } catch (Exception e) {
            return withFailure();
        }
    }
}
