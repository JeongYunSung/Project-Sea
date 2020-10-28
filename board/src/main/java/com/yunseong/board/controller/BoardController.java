package com.yunseong.board.controller;

import com.yunseong.board.domain.Board;
import com.yunseong.board.domain.BoardRevision;
import com.yunseong.board.service.BoardService;
import com.yunseong.board.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping(value = "/boards")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;

    @GetMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("(isAnonymous() and (returnObject.body.boardCategory.readPermission.name() == 'ANONYMOUS')) or (#oauth2.hasScope('board_read') and hasRole('ROLE_' + returnObject.body.boardCategory.readPermission.name()))")
    public ResponseEntity<BoardDetailResponse> findBoard(@PathVariable long id) {
        return ResponseEntity.ok(this.boardService.findBoard(id));
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedModel<BoardSearchResponse>> findsBoard(@ModelAttribute BoardSearchCondition boardSearchCondition, @PageableDefault Pageable pageable) {
        Page<BoardSearchResponse> page = this.boardService.findsBoard(boardSearchCondition, pageable);
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
        PagedModel<BoardSearchResponse> model = PagedModel.of(page.getContent(), metadata);
        return ResponseEntity.ok(model);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("#oauth2.hasScope('board_write') and hasRole('ROLE_' + #request.boardCategory.writePermission.name())")
    public ResponseEntity<Long> createBoard(@RequestBody BoardCreateRequest request, @RequestPart MultipartFile file, Principal principal) {
        Board board = this.boardService.createBoard(principal.getName(), request);
        this.fileService.save(board.getId(), file);
        return new ResponseEntity<>(board.getId(), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}/recommend", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> recommendBoard(@PathVariable long id, Principal principal) {
        return ResponseEntity.ok(this.boardService.recommendBoard(id, principal.getName()).getId());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> reviseBoard(@PathVariable long id, @RequestBody BoardRevision boardRevision, Principal principal) {
        return ResponseEntity.ok(this.boardService.reviseBoard(id, principal.getName(), boardRevision).getId());
    }

    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteBoard(@PathVariable long id, Principal principal) {
        this.boardService.delete(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
