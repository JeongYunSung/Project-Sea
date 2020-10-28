package com.yunseong.project.sagas.reviseproject;

import com.yunseong.project.api.ProjectServiceChannels;
import com.yunseong.project.sagaparticipants.BeginReviseProjectCommand;
import com.yunseong.project.sagaparticipants.ConfirmReviseProjectCommand;
import com.yunseong.project.sagaparticipants.UndoBeginReviseProjectCommand;
import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;

import javax.annotation.PostConstruct;

import static io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send;

public class ReviseProjectSaga implements SimpleSaga<ReviseProjectSagaData> {

    private SagaDefinition<ReviseProjectSagaData> sagaDefinition;

    @PostConstruct
    private void init() {
        this.sagaDefinition =
                step()
                    .invokeParticipant(this::makeBeginReviseProjectCommand)
                    .withCompensation(this::makeUndoBeginReviseProjectCommand)
                .step()
                    .invokeParticipant(this::makeConfirmReviseProjectCommand)
                .build();
    }

    @Override
    public SagaDefinition<ReviseProjectSagaData> getSagaDefinition() {
        return this.sagaDefinition;
    }

    private CommandWithDestination makeBeginReviseProjectCommand(ReviseProjectSagaData data) {
        return send(new BeginReviseProjectCommand(data.getProjectId()))
                .to(ProjectServiceChannels.projectServiceChannel)
                .build();
    }

    private CommandWithDestination makeUndoBeginReviseProjectCommand(ReviseProjectSagaData data) {
        return send(new UndoBeginReviseProjectCommand(data.getProjectId()))
                .to(ProjectServiceChannels.projectServiceChannel)
                .build();
    }

    public CommandWithDestination makeConfirmReviseProjectCommand(ReviseProjectSagaData data) {
        return send(new ConfirmReviseProjectCommand(data.getProjectId(), data.getProjectRevision()))
                .to(ProjectServiceChannels.projectServiceChannel)
                .build();
    }
}
