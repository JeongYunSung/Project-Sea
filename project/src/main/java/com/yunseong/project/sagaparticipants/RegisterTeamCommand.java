package com.yunseong.project.sagaparticipants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisterTeamCommand extends ProjectCommand {

    private long teamId;
    private String username;

    public RegisterTeamCommand(long projectId, String username, long teamId) {
        super(projectId);
        this.username = username;
        this.teamId = teamId;
    }
}
