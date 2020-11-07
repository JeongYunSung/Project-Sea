package com.yunseong.project.sagas.cancelproject;

import com.yunseong.board.api.BoardServiceChannels;
import com.yunseong.board.api.command.DeleteBoardCommand;
import com.yunseong.project.api.ProjectServiceChannels;
import com.yunseong.project.api.TeamServiceChannels;
import com.yunseong.project.api.command.CancelTeamCommand;
import com.yunseong.project.api.command.IsLeaderTeamCommand;
import com.yunseong.project.sagaparticipants.BeginCancelProjectCommand;
import com.yunseong.project.sagaparticipants.ConfirmCancelProjectCommand;
import com.yunseong.project.sagaparticipants.UndoBeginCancelProjectCommand;
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
                    .invokeParticipant(this::deleteBoard)
                .step()
                    .invokeParticipant(this::confirmCancelProject)
                .build();
    }

    @Override
    public SagaDefinition<CancelProjectSagaData> getSagaDefinition() {
        return this.sagaDefinition;
    }

//    private CommandWithDestination isLeaderTeam(CancelProjectSagaData data) {
//        return send(new IsLeaderTeamCommand(data.getTeamId(), data.getUsername()))
//                .to(TeamServiceChannels.teamServiceChannel)
//                .build();
//    }

    private CommandWithDestination deleteBoard(CancelProjectSagaData data) {
        return send(new DeleteBoardCommand(data.getProjectId(), data.getUsername()))
                .to(BoardServiceChannels.boardServiceChannel)
                .build();
    }

    private CommandWithDestination cancelProject(CancelProjectSagaData data) {
        return send(new BeginCancelProjectCommand(data.getProjectId()))
                .to(ProjectServiceChannels.projectServiceChannel)
                .build();
    }

    private CommandWithDestination undoCancelProject(CancelProjectSagaData data) {
        return send(new UndoBeginCancelProjectCommand(data.getProjectId()))
                .to(ProjectServiceChannels.projectServiceChannel)
                .build();
    }

    private CommandWithDestination cancelTeam(CancelProjectSagaData data) {
        return send(new CancelTeamCommand(data.getTeamId()))
                .to(TeamServiceChannels.teamServiceChannel)
                .build();
    }

    private CommandWithDestination confirmCancelProject(CancelProjectSagaData data) {
        return send(new ConfirmCancelProjectCommand(data.getProjectId()))
                .to(ProjectServiceChannels.projectServiceChannel)
                .build();
    }
}
