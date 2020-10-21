package com.yunseong.project.sagas.createproject;

import com.yunseong.project.api.command.CreateTeamReply;
import com.yunseong.project.sagaparticipants.ProjectProxyService;
import com.yunseong.project.sagaparticipants.TeamProxyService;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;

public class CreateProjectSaga implements SimpleSaga<CreateProjectSagaState> {

    private SagaDefinition<CreateProjectSagaState> sagaDefinition;

    public CreateProjectSaga(ProjectProxyService proxyService, TeamProxyService teamProxyService) {
        this.sagaDefinition =
                step()
                    .withCompensation(proxyService.cancel, CreateProjectSagaState::makeProjectConfirmCancelCommand)
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
