package com.yunseong.project.sagas.startproject;

import com.yunseong.project.api.command.ApproveTeamCommand;
import com.yunseong.project.api.command.RejectTeamCommand;
import com.yunseong.project.sagaparticipants.CloseProjectCommand;
import com.yunseong.project.sagaparticipants.RegisterWeClassCommand;
import com.yunseong.project.sagaparticipants.RejectProjectCommand;
import com.yunseong.project.sagaparticipants.StartProjectCommand;
import com.yunseong.weclass.api.command.CreateWeClassCommand;
import com.yunseong.weclass.api.command.CreateWeClassReply;
import lombok.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class StartProjectSagaState {

    @NonNull
    private Long projectId;
    private Long weClassId;
    @NonNull
    private Long teamId;

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public CloseProjectCommand makeCloseProjectCommand() {
        return new CloseProjectCommand(this.projectId);
    }

    public RejectProjectCommand makeRejectProjectCommand() {
        return new RejectProjectCommand(this.projectId);
    }

    public RejectTeamCommand makeRejectTeamCommand() {
        return new RejectTeamCommand(this.teamId);
    }

    public ApproveTeamCommand makeApproveTeamCommand() {
        return new ApproveTeamCommand(this.teamId);
    }

    public CreateWeClassCommand makeCreateWeClassCommand() {
        return new CreateWeClassCommand(this.projectId);
    }

    public RegisterWeClassCommand makeRegisterWeClassCommand() {
        return new RegisterWeClassCommand(this.projectId, this.weClassId);
    }

    public StartProjectCommand makeStartProjectCommand() {
        return new StartProjectCommand(this.projectId);
    }

    public void handleCreateWeClassReply(CreateWeClassReply reply) {
        this.weClassId = reply.getWeClassId();
    }
}
