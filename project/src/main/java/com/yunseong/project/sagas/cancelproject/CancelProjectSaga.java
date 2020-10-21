package com.yunseong.project.sagas.cancelproject;

import com.yunseong.project.api.ProjectServiceChannels;
import com.yunseong.project.api.TeamServiceChannels;
import com.yunseong.project.api.command.CancelTeamCommand;
import com.yunseong.project.api.event.TeamState;
import com.yunseong.project.sagaparticipants.ProjectBeginCancelCommand;
import com.yunseong.project.sagaparticipants.ProjectConfirmCancelCommand;
import com.yunseong.project.sagaparticipants.ProjectUndoBeginCancelCommand;
import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;

import javax.annotation.PostConstruct;

import static io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send;

public class CancelProjectSaga implements SimpleSaga<CancelProjectSagaData> {

    private SagaDefinition<CancelProjectSagaData> sagaDefinition;

    @PostConstruct
    public void init() {
        this.sagaDefinition =
                step()
                    .invokeParticipant(this::cancelProject)
                    .withCompensation(this::undoCancelProject)
                .step()
                    .invokeParticipant(this::cancelTeam)
                .step()
                    .invokeParticipant(this::confirmCancelProject)
                .build();
    }

    @Override
    public SagaDefinition<CancelProjectSagaData> getSagaDefinition() {
        return this.sagaDefinition;
    }

    private CommandWithDestination cancelProject(CancelProjectSagaData data) {
        return send(new ProjectBeginCancelCommand(data.getProjectId()))
                .to(ProjectServiceChannels.projectServiceChannel)
                .build();
    }

    private CommandWithDestination undoCancelProject(CancelProjectSagaData data) {
        return send(new ProjectUndoBeginCancelCommand(data.getProjectId()))
                .to(ProjectServiceChannels.projectServiceChannel)
                .build();
    }

    private CommandWithDestination cancelTeam(CancelProjectSagaData data) {
        return send(new CancelTeamCommand(data.getProjectId()))
                .to(TeamServiceChannels.teamServiceChannel)
                .build();
    }

    private CommandWithDestination confirmCancelProject(CancelProjectSagaData data) {
        return send(new ProjectConfirmCancelCommand(data.getProjectId()))
                .to(ProjectServiceChannels.projectServiceChannel)
                .build();
    }
}
