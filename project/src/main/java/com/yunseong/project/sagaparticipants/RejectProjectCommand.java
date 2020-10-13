package com.yunseong.project.sagaparticipants;

public class RejectProjectCommand extends ProjectCommand {

    public RejectProjectCommand() {
    }

    public RejectProjectCommand(long projectId) {
        super(projectId);
    }
}
