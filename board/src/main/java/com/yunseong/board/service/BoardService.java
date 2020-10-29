package com.yunseong.board.service;

import com.yunseong.board.controller.BoardCreateRequest;
import com.yunseong.board.controller.BoardDetailResponse;
import com.yunseong.board.controller.BoardSearchCondition;
import com.yunseong.board.controller.BoardSearchResponse;
import com.yunseong.board.domain.Board;
import com.yunseong.board.domain.BoardCategory;
import com.yunseong.board.domain.BoardRepository;
import com.yunseong.board.domain.BoardRevision;
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

    public Board createBoard(String writer, BoardCreateRequest request) {
        return this.boardRepository.save(new Board(writer, request.getSubject(), request.getContent(), request.getBoardCategory()));
    }

    public void delete(long id, String writer) {
        Board board = this.getBoard(id);
        if(!writer.equals(board.getWriter())) throw new CannotReviseBoardIfWriterNotWereException("작성자가 아니면 삭제할 수 없습니다");
        board.delete();
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
        if(!writer.equals(board.getWriter())) throw new CannotReviseBoardIfWriterNotWereException("작성자가 아니면 수정할 수 없습니다");
        board.revise(boardRevision);
        return board;
    }

    @Transactional(readOnly = true)
    public Page<BoardSearchResponse> findsBoard(BoardSearchCondition condition, Pageable pageable) {
        return this.boardRepository.findPageByQuery(condition, pageable);
    }

    @Transactional(readOnly = true)
    public BoardDetailResponse findBoard(long id) {
        BoardDetailResponse response = this.boardRepository.findFetchDtoById(id);
        if(response.getSubject() == null) throw new EntityNotFoundException("해당 게시판엔티티는 존재하지않습니다.");
        return response;
    }

    @Transactional(readOnly = true)
    public BoardCategory getCategory(long id) {
        return this.getBoard(id).getBoardCategory();
    }

    private Board getBoard(long id) {
        return this.boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 게시판엔티티는 존재하지않습니다."));
    }
}
