package com.yunseong.project.sagaparticipants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchUndoProjectCommand extends ProjectCommand {

    private List<Long> projectIds;

    public BatchUndoProjectCommand(List<Long> projectIds) {
        super(-1L);
        this.projectIds = projectIds;
    }
}
