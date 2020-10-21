package com.yunseong.project.sagaparticipants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisterTeamCommand extends ProjectCommand {

    private long teamId;

    public RegisterTeamCommand(long projectId, long teamId) {
        super(projectId);
        this.teamId = teamId;
    }
}
