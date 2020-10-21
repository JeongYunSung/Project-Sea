package com.yunseong.project.sagaparticipants;

public class ProjectConfirmCancelCommand extends ProjectCommand {

    public ProjectConfirmCancelCommand(long projectId) {
        super(projectId);
    }

    public ProjectConfirmCancelCommand() {
    }
}
