package com.yunseong.project.sagaparticipants;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RejectProjectCommand extends ProjectCommand {

    public RejectProjectCommand(long projectId) {
        super(projectId);
    }
}
