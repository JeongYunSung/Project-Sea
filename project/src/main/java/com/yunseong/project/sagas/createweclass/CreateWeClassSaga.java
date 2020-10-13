package com.yunseong.project.sagas.createweclass;

import com.yunseong.project.sagaparticipants.ProjectProxyService;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;

public class CreateWeClassSaga implements SimpleSaga<CreateWeClassSagaState> {

    private SagaDefinition<CreateWeClassSagaState> sagaSagaDefinition;

    public CreateWeClassSaga(ProjectProxyService projectService) {
        this.sagaSagaDefinition =
                step()
                    .withCompensation(projectService.reject, CreateWeClassSagaState::makeRejectProjectCommand)
                .step()
                    .invokeParticipant(projectService.approve, CreateWeClassSagaState::makeApproveProjectCommand)
                .build();
    }

    @Override
    public SagaDefinition<CreateWeClassSagaState> getSagaDefinition() {
        return this.sagaSagaDefinition;
    }
}
