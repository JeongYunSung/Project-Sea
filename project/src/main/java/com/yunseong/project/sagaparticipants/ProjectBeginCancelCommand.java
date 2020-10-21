package com.yunseong.project.sagaparticipants;

public class ProjectBeginCancelCommand extends ProjectCommand {

    public ProjectBeginCancelCommand(long projectId) {
        super(projectId);
    }

    protected ProjectBeginCancelCommand() {
    }
}
