package com.yunseong.project.sagas.createproject;

import com.yunseong.project.api.command.CreateTeamCommand;
import com.yunseong.project.api.command.CreateTeamReply;
import com.yunseong.project.sagaparticipants.CreateProjectCommand;
import com.yunseong.project.sagaparticipants.ConfirmCancelProjectCommand;
import com.yunseong.project.sagaparticipants.RegisterTeamCommand;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CreateProjectSagaState {

    private long teamId;
    @NonNull
    private Long projectId;
    @NonNull
    private String username;
    @NonNull
    private Integer minSize;
    @NonNull
    private Integer maxSize;

    public ConfirmCancelProjectCommand makeProjectConfirmCancelCommand() {
        return new ConfirmCancelProjectCommand(this.projectId);
    }

    public CreateTeamCommand makeCreateTeamCommand() {
        return new CreateTeamCommand(this.projectId, this.username, this.minSize, this.maxSize);
    }

    public RegisterTeamCommand makeRegisterTeamCommand() {
        return new RegisterTeamCommand(this.teamId, this.teamId);
    }

    public CreateProjectCommand makeCreateProjectCommand() {
        return new CreateProjectCommand(this.projectId);
    }

    public void handleCreateTeamReply(CreateTeamReply createTeamReply) {
        this.teamId = createTeamReply.getTeamId();
    }
}
