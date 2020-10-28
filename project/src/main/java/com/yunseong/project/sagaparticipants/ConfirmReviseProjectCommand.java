package com.yunseong.project.sagaparticipants;

import com.yunseong.project.domain.ProjectRevision;
import lombok.Getter;

@Getter
public class ConfirmReviseProjectCommand extends ProjectCommand {

    private ProjectRevision projectRevision;

    public ConfirmReviseProjectCommand(long projectId, ProjectRevision projectRevision) {
        super(projectId);
        this.projectRevision = projectRevision;
    }

    public ConfirmReviseProjectCommand() {
    }
}
