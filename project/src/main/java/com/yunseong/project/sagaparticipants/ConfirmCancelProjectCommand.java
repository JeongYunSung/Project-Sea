package com.yunseong.project.sagaparticipants;

public class ConfirmCancelProjectCommand extends ProjectCommand {

    public ConfirmCancelProjectCommand(long projectId) {
        super(projectId);
    }

    public ConfirmCancelProjectCommand() {
    }
}
