package com.yunseong.board.service;

import com.yunseong.board.api.BoardCategory;
import com.yunseong.board.controller.*;
import com.yunseong.board.domain.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

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
        if(board.getRecommend().contains(recommender)) throw new AlreadyRecommendedException("이미 추천했습니다");
        board.addRecommend(recommender);
        return board;
    }

    public Board reviseBoard(long id, String writer, BoardRevision boardRevision) {
        Board board = this.getBoard(id);
        board.revise(writer, boardRevision);
        return board;
    }

    @Transactional(readOnly = true)
    public Page<BoardSearchResponse> findsBoard(BoardSearchCondition condition, Pageable pageable) {
        return this.boardRepository.findPageByQuery(condition, pageable).map(b -> new BoardSearchResponse(b.getId(), b.getSubject(), b.getWriter(), b.getBoardCategory(), b.getReadCount(), b.getRecommend().size(), b.getCreatedTime()));
    }

    public BoardDetailResponse findBoard(long id) {
        Board board = this.boardRepository.findFetchDtoById(id).orElseThrow(() -> new EntityNotFoundException("해당 게시판엔티티는 존재하지않습니다."));
        board.addReadCount();
        return new BoardDetailResponse(board.getId(), board.getWriter(), board.getSubject(), board.getContent(), board.getBoardCategory(), board.getReadCount(), board.getRecommend().size(), board.getCreatedTime());
    }

    @Transactional(readOnly = true)
    public BoardCategory getCategory(long id) {
        return this.getBoard(id).getBoardCategory();
    }

    private Board getBoard(long id) {
        return this.boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 게시판엔티티는 존재하지않습니다."));
    }
}
