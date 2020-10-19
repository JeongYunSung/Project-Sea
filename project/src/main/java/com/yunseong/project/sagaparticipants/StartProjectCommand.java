package com.yunseong.project.sagaparticipants;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StartProjectCommand extends ProjectCommand {

    public StartProjectCommand(long projectId) {
        super(projectId);
    }
}
