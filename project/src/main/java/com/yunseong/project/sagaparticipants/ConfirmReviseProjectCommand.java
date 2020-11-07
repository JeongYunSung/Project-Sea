package com.yunseong.project.sagaparticipants;

import com.yunseong.project.domain.ProjectRevision;
import lombok.Getter;

@Getter
public class ConfirmReviseProjectCommand extends ProjectCommand {

    private String subject;
    private boolean isPublic;

    public ConfirmReviseProjectCommand(long projectId, String subject, boolean isPublic) {
        super(projectId);
        this.subject = subject;
        this.isPublic = isPublic;
    }

    public ConfirmReviseProjectCommand() {
    }
}
