package com.yunseong.project.sagaparticipants;

public class  CloseProjectCommand extends ProjectCommand {

    public CloseProjectCommand(long projectId) {
        super(projectId);
    }

    public CloseProjectCommand() {
    }
}
