package com.yunseong.project.sagas.startproject;

import com.yunseong.project.sagaparticipants.ProjectProxyService;
import com.yunseong.project.sagaparticipants.TeamProxyService;
import com.yunseong.project.sagaparticipants.WeClassProxyService;
import com.yunseong.weclass.api.command.CreateWeClassReply;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;

public class StartProjectSaga implements SimpleSaga<StartProjectSagaState> {

    private final SagaDefinition<StartProjectSagaState> sagaSagaDefinition;

    public StartProjectSaga(ProjectProxyService projectService, TeamProxyService teamProxyService, WeClassProxyService weClassProxyService) {
        this.sagaSagaDefinition =
                step()
                    .withCompensation(projectService.reject, StartProjectSagaState::makeRejectProjectCommand)
                .step()
                    .invokeParticipant(projectService.close, StartProjectSagaState::makeCloseProjectCommand)
                .step()
                    .invokeParticipant(teamProxyService.approve, StartProjectSagaState::makeApproveTeamCommand)
                    .withCompensation(teamProxyService.reject, StartProjectSagaState::makeRejectTeamCommand)
                .step()
                    .invokeParticipant(weClassProxyService.create, StartProjectSagaState::makeCreateWeClassCommand)
                    .onReply(CreateWeClassReply.class, StartProjectSagaState::handleCreateWeClassReply)
                .step()
                    .invokeParticipant(projectService.registerWeClass, StartProjectSagaState::makeRegisterWeClassCommand)
                .step()
                    .invokeParticipant(projectService.start, StartProjectSagaState::makeStartProjectCommand)
                .build();
    }

    @Override
    public SagaDefinition<StartProjectSagaState> getSagaDefinition() {
        return this.sagaSagaDefinition;
    }
}
