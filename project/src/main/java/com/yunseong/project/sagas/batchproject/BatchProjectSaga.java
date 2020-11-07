package com.yunseong.project.sagas.batchproject;

import com.yunseong.project.api.ProjectServiceChannels;
import com.yunseong.project.api.TeamServiceChannels;
import com.yunseong.project.api.command.BatchTeamCommand;
import com.yunseong.project.api.command.BatchUndoTeamCommand;
import com.yunseong.project.sagaparticipants.BatchFinishedProjectCommand;
import com.yunseong.project.sagaparticipants.BatchStartProjectCommand;
import com.yunseong.project.sagaparticipants.BatchUndoProjectCommand;
import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;

import javax.annotation.PostConstruct;

import static io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send;

public class BatchProjectSaga implements SimpleSaga<BatchProjectSagaData> {

    private SagaDefinition<BatchProjectSagaData> sagaDataSagaDefinition;

    @PostConstruct
    private void init() {
        this.sagaDataSagaDefinition =
                step()
                    .invokeParticipant(this::batchPending)
                    .withCompensation(this::batchUndo)
                .step()
                    .invokeParticipant(this::batchTeam)
                    .withCompensation(this::batchUndoTeam)
                .step()
                    .invokeParticipant(this::batchStart)
                .build();
    }

    @Override
    public SagaDefinition<BatchProjectSagaData> getSagaDefinition() {
        return this.sagaDataSagaDefinition;
    }

    private CommandWithDestination batchPending(BatchProjectSagaData data) {
        return send(new BatchStartProjectCommand(data.getProjectIds()))
                .to(ProjectServiceChannels.projectServiceChannel)
                .build();
    }

    private CommandWithDestination batchUndo(BatchProjectSagaData data) {
        return send(new BatchUndoProjectCommand(data.getProjectIds()))
                .to(ProjectServiceChannels.projectServiceChannel)
                .build();
    }

    private CommandWithDestination batchTeam(BatchProjectSagaData data) {
        return send(new BatchTeamCommand(data.getProjectIds()))
                .to(TeamServiceChannels.teamServiceChannel)
                .build();
    }

    private CommandWithDestination batchUndoTeam(BatchProjectSagaData data) {
        return send(new BatchUndoTeamCommand(data.getProjectIds()))
                .to(ProjectServiceChannels.projectServiceChannel)
                .build();
    }

    private CommandWithDestination batchStart(BatchProjectSagaData data) {
        return send(new BatchFinishedProjectCommand(data.getProjectIds()))
                .to(ProjectServiceChannels.projectServiceChannel)
                .build();
    }

}
