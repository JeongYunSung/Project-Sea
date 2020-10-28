package com.yunseong.project.sagaparticipants;

public class UndoBeginReviseProjectCommand extends ProjectCommand {

    public UndoBeginReviseProjectCommand(long projectId) {
        super(projectId);
    }

    public UndoBeginReviseProjectCommand() {
    }
}
