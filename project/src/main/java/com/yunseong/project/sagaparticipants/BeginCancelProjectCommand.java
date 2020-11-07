package com.yunseong.project.sagaparticipants;

import lombok.Getter;

@Getter
public class BeginCancelProjectCommand extends ProjectCommand {

    public BeginCancelProjectCommand(long projectId) {
        super(projectId);
    }

    protected BeginCancelProjectCommand() {
    }
}
