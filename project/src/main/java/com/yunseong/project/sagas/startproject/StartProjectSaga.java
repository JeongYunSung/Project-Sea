package com.yunseong.project.sagas.startproject;

import com.yunseong.project.api.ProjectServiceChannels;
import com.yunseong.project.api.TeamServiceChannels;
import com.yunseong.project.api.command.ApproveTeamCommand;
import com.yunseong.project.api.command.RejectTeamCommand;
import com.yunseong.project.sagaparticipants.*;
import com.yunseong.weclass.api.WeClassServiceChannels;
import com.yunseong.weclass.api.command.CreateWeClassCommand;
import com.yunseong.weclass.api.command.CreateWeClassReply;
import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;

import javax.annotation.PostConstruct;

import static io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send;

public class StartProjectSaga implements SimpleSaga<StartProjectSagaData> {

    private SagaDefinition<StartProjectSagaData> sagaSagaDefinition;

    @PostConstruct
    public void init() {
        this.sagaSagaDefinition =
                step()
                    .withCompensation(this::makeRejectProjectCommand)
                .step()
                    .invokeParticipant(this::makeApproveTeamCommand)
                    .withCompensation(this::makeRejectTeamCommand)
                .step()
                    .invokeParticipant(this::makeCreateWeClassCommand)
                    .onReply(CreateWeClassReply.class, this::handleCreateWeClassReply)
                .step()
                    .invokeParticipant(this::makeCreateWeClassCommand)
                    .onReply(CreateWeClassReply.class, this::handleCreateWeClassReply)
                .step()
                    .invokeParticipant(this::makeRegisterWeClassCommand)
                .step()
                    .invokeParticipant(this::makeStartProjectCommand)
                .build();
    }

    @Override
    public SagaDefinition<StartProjectSagaData> getSagaDefinition() {
        return this.sagaSagaDefinition;
    }

    public CommandWithDestination makeRejectProjectCommand(StartProjectSagaData data) {
        return send(new RejectProjectCommand(data.getProjectId()))
                .to(ProjectServiceChannels.projectServiceChannel)
                .build();
    }

    public CommandWithDestination makeRejectTeamCommand(StartProjectSagaData data) {
        return send(new RejectTeamCommand(data.getTeamId()))
                .to(TeamServiceChannels.teamServiceChannel)
                .build();
    }

    public CommandWithDestination makeApproveTeamCommand(StartProjectSagaData data) {
        return send(new ApproveTeamCommand(data.getTeamId()))
                .to(TeamServiceChannels.teamServiceChannel)
                .build();
    }

    public CommandWithDestination makeCreateWeClassCommand(StartProjectSagaData data) {
        return send(new CreateWeClassCommand(data.getProjectId()))
                .to(WeClassServiceChannels.weClassServiceChannel)
                .build();
    }

    public CommandWithDestination makeRegisterWeClassCommand(StartProjectSagaData data) {
        return send(new RegisterWeClassCommand(data.getProjectId(), data.getWeClassId()))
                .to(ProjectServiceChannels.projectServiceChannel)
                .build();
    }

    public CommandWithDestination makeStartProjectCommand(StartProjectSagaData data) {
        return send(new StartProjectCommand(data.getProjectId()))
                .to(ProjectServiceChannels.projectServiceChannel)
                .build();
    }

    public void handleCreateWeClassReply(StartProjectSagaData data, CreateWeClassReply reply) {
        data.setWeClassId(reply.getWeClassId());
    }
}
