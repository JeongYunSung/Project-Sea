package com.yunseong.project.sagas.createproject;

import com.yunseong.board.api.command.CreateBoardReply;
import com.yunseong.project.api.command.CreateTeamReply;
import com.yunseong.project.sagaparticipants.BoardProxyService;
import com.yunseong.project.sagaparticipants.ProjectProxyService;
import com.yunseong.project.sagaparticipants.TeamProxyService;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;

public class CreateProjectSaga implements SimpleSaga<CreateProjectSagaState> {

    private final SagaDefinition<CreateProjectSagaState> sagaDefinition;

    public CreateProjectSaga(ProjectProxyService proxyService, BoardProxyService boardProxyService, TeamProxyService teamProxyService) {
        this.sagaDefinition =
                step()
                    .withCompensation(proxyService.cancel, CreateProjectSagaState::makeProjectConfirmCancelCommand)
                .step()
                    .invokeParticipant(boardProxyService.create, CreateProjectSagaState::makeCreateBoardCommand)
                    .onReply(CreateBoardReply.class, CreateProjectSagaState::handleCreateBoardReply)
                .step()
                    .invokeParticipant(proxyService.registerBoard, CreateProjectSagaState::makeRegisterBoardCommand)
                .step()
                    .invokeParticipant(teamProxyService.create, CreateProjectSagaState::makeCreateTeamCommand)
                    .onReply(CreateTeamReply.class, CreateProjectSagaState::handleCreateTeamReply)
                .step()
                    .invokeParticipant(proxyService.registerTeam, CreateProjectSagaState::makeRegisterTeamCommand)
                .step()
                    .invokeParticipant(proxyService.create, CreateProjectSagaState::makeCreateProjectCommand)
                .build();
    }

    @Override
    public SagaDefinition<CreateProjectSagaState> getSagaDefinition() {
        return this.sagaDefinition;
    }
}
