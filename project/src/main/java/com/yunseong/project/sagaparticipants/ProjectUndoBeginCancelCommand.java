package com.yunseong.project.sagaparticipants;

public class ProjectUndoBeginCancelCommand extends ProjectCommand {

    public ProjectUndoBeginCancelCommand(long projectId) {
        super(projectId);
    }

    public ProjectUndoBeginCancelCommand() {
    }
}
