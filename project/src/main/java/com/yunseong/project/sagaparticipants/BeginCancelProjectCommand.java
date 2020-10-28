package com.yunseong.project.sagaparticipants;

public class BeginCancelProjectCommand extends ProjectCommand {

    public BeginCancelProjectCommand(long projectId) {
        super(projectId);
    }

    protected BeginCancelProjectCommand() {
    }
}
