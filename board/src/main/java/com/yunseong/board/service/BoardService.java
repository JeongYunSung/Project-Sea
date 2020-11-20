package com.yunseong.board.service;

import com.yunseong.board.api.BoardCategory;
import com.yunseong.board.api.events.BoardAddRecommendEvent;
import com.yunseong.board.controller.*;
import com.yunseong.board.domain.*;
import io.eventuate.tram.events.common.DomainEvent;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final DomainEventPublisher domainEventPublisher;

    public Comment createComment(long boardId, String writer, CommentCreateRequest request) {
        Comment originComment = null;
        CommentState commentState = CommentState.ORIGINAL;
        if(request.getCommentId() != null) {
            originComment = this.commentRepository.findFetchById(request.getCommentId()).orElseThrow(() -> new EntityNotFoundException("해당 댓글엔티티는 존재하지않습니다."));
            if(originComment.getOriginalComment() != null) originComment = originComment.getOriginalComment();
            commentState = CommentState.REPLY;
        }
        return this.commentRepository.save(new Comment(this.getBoard(boardId), writer, request.getContent(), originComment, commentState));
    }

    @Transactional(readOnly = true)
    public Page<Board> findMyBoards(String username, Pageable pageable) {
        return this.boardRepository.findMyBoards(username, pageable);
    }

    public boolean batchBoard(List<Long> boardIds) {
        try {
            this.boardRepository.batchUpdate(boardIds);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void batchUndoBoard(List<Long> boardIds) {
        this.boardRepository.batchUndoUpdate(boardIds);
    }

    public Comment reviseComment(long id, String writer, String content) {
        Comment comment = this.getComment(id);
        comment.revise(writer, content);
        return comment;
    }

    public void deleteComment(long id, String writer) {
        Comment comment = this.getComment(id);
        comment.delete(writer);
    }

    @Transactional(readOnly = true)
    public Page<Comment> findCommentByPage(long boardId, Pageable pageable) {
        return this.commentRepository.findPage(boardId, pageable);
    }

    private Comment getComment(long id) {
        return this.commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 댓글엔티티는 존재하지않습니다."));
    }

    public Board createBoard(String writer, BoardCreateRequest request, boolean isDifferent) {
        return this.boardRepository.save(new Board(writer, request.getSubject(), request.getContent(), request.getCategory(), isDifferent));
    }

    public void delete(long id, String writer) {
        Board board = this.getBoard(id);
        board.delete(writer);
    }

    public Board recommendBoard(long id, String recommender) {
        Board board = this.boardRepository.findFetchById(id).orElseThrow(() -> new EntityNotFoundException("해당 게시판엔티티는 존재하지않습니다."));
        if(recommender.equals(board.getWriter())) throw new CannotRecommendByWriterException("작성자가 추천할 수 없습니다");
        if(board.getRecommender().getRecommender().contains(recommender)) throw new AlreadyRecommendedException("이미 추천했습니다");
        try {
            this.domainEventPublisher.publish(Board.class, board.getId(), List.of(board.addRecommend(recommender)));
        } catch (ParseException e) {
            return null;
        }
        return board;
    }

    public Board reviseBoard(long id, String writer, BoardRevision boardRevision) {
        Board board = this.getBoard(id);
        board.revise(writer, boardRevision);
        return board;
    }

    @Transactional(readOnly = true)
    public Page<BoardSearchResponse> findsBoard(BoardSearchCondition condition, Pageable pageable) {
        return this.boardRepository.findPageByQuery(condition, pageable).map(b -> new BoardSearchResponse(b.getId(), b.getSubject(), b.getWriter(), b.getBoardCategory(), b.getReadCount(), b.getRecommender().getRecommender().size(), b.getCreatedTime()));
    }

    @Transactional(readOnly = true)
    public List<HotBoardResponse> findsHotBoard(HotBoardSearchCondition condition) {
        List<Board> hotBoards = this.boardRepository.findHotBoards(condition);
        return hotBoards.stream().map(b ->
                new HotBoardResponse(b.getId(), b.getWriter(), b.getSubject(), b.getBoardCategory(), b.getRecommender().getRecommendStatistics().stream().mapToLong(RecommendStatistics::getValue).sum()))
                .collect(Collectors.toList());
    }

    public BoardDetailResponse findBoard(long id) {
        Board board = this.boardRepository.findFetchById(id).orElseThrow(() -> new EntityNotFoundException("해당 게시판엔티티는 존재하지않습니다."));
        board.addReadCount();
        return new BoardDetailResponse(board.getId(), board.getWriter(), board.getSubject(), board.getContent(), board.getBoardCategory(), board.getReadCount(), board.getRecommender().getRecommender().size(), board.getCreatedTime());
    }

    @Transactional(readOnly = true)
    public BoardCategory getCategory(long id) {
        return this.getBoard(id).getBoardCategory();
    }

    private Board getBoard(long id) {
        return this.boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 게시판엔티티는 존재하지않습니다."));
    }
}
