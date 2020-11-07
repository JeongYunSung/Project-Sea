package com.yunseong.project.sagaparticipants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchFinishedProjectCommand extends ProjectCommand {

    private List<Long> projectIds;

    public BatchFinishedProjectCommand(List<Long> projectIds) {
        super(-1L);
        this.projectIds = projectIds;
    }
}
