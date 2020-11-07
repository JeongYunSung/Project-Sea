package com.yunseong.project.sagaparticipants;

import com.yunseong.board.api.BoardDetail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisterBoardCommand extends ProjectCommand {

    private long boardId;
    private BoardDetail boardDetail;

    public RegisterBoardCommand(long projectId, long boardId, BoardDetail boardDetail) {
        super(projectId);
        this.boardId = boardId;
        this.boardDetail = boardDetail;
    }
}
