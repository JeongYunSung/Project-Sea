package com.yunseong.project.sagaparticipants;

public class UndoBeginCancelProjectCommand extends ProjectCommand {

    public UndoBeginCancelProjectCommand(long projectId) {
        super(projectId);
    }

    public UndoBeginCancelProjectCommand() {
    }
}
