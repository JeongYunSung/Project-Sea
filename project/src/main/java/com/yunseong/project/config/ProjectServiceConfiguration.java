package com.yunseong.project.config;

import com.yunseong.project.domain.ProjectDomainEventPublisher;
import com.yunseong.project.sagaparticipants.ProjectProxyService;
import com.yunseong.project.sagaparticipants.TeamProxyService;
import com.yunseong.project.sagaparticipants.WeClassProxyService;
import com.yunseong.project.sagas.cancelproject.CancelProjectSaga;
import com.yunseong.project.sagas.cancelproject.CancelProjectSagaData;
import com.yunseong.project.sagas.createproject.CreateProjectSaga;
import com.yunseong.project.sagas.createproject.CreateProjectSagaState;
import com.yunseong.project.sagas.reviseproject.ReviseProjectSaga;
import com.yunseong.project.sagas.reviseproject.ReviseProjectSagaData;
import com.yunseong.project.sagas.startproject.StartProjectSaga;
import com.yunseong.project.sagas.startproject.StartProjectSagaState;
import io.eventuate.tram.commands.producer.CommandProducer;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.sagas.common.SagaLockManager;
import io.eventuate.tram.sagas.orchestration.SagaCommandProducer;
import io.eventuate.tram.sagas.orchestration.SagaInstanceRepository;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import io.eventuate.tram.sagas.orchestration.SagaManagerImpl;
import io.eventuate.tram.sagas.spring.common.EventuateTramSagaCommonConfiguration;
import io.eventuate.tram.sagas.spring.orchestration.SagaOrchestratorConfiguration;
import io.eventuate.tram.spring.commands.producer.TramCommandProducerConfiguration;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TramEventsPublisherConfiguration.class, ProjectServiceMessageHandlersConfiguration.class, TramCommandProducerConfiguration.class, EventuateTramSagaCommonConfiguration.class, SagaOrchestratorConfiguration.class, TramJdbcKafkaConfiguration.class})
public class ProjectServiceConfiguration {

    @Bean
    public ProjectDomainEventPublisher projectDomainEventPublisher(DomainEventPublisher domainEventPublisher) {
        return new ProjectDomainEventPublisher(domainEventPublisher);
    }

    @Bean
    public SagaManager<StartProjectSagaState> startProjectSagaSagaManager(StartProjectSaga saga, SagaInstanceRepository sagaInstanceRepository, CommandProducer commandProducer, MessageConsumer messageConsumer,
                                                                           SagaCommandProducer sagaCommandProducer, SagaLockManager sagaLockManager) {
        return new SagaManagerImpl<>(saga, sagaInstanceRepository, commandProducer, messageConsumer, sagaLockManager, sagaCommandProducer);
    }

    @Bean
    public SagaManager<CreateProjectSagaState> createProjectSagaStateSagaManager(CreateProjectSaga saga, SagaInstanceRepository sagaInstanceRepository, CommandProducer commandProducer, MessageConsumer messageConsumer,
                                                                                 SagaCommandProducer sagaCommandProducer, SagaLockManager sagaLockManager) {
        return new SagaManagerImpl<>(saga, sagaInstanceRepository, commandProducer, messageConsumer, sagaLockManager, sagaCommandProducer);
    }

    @Bean
    public SagaManager<ReviseProjectSagaData> reviseProjectSagaDataSagaManager(ReviseProjectSaga saga, SagaInstanceRepository sagaInstanceRepository, CommandProducer commandProducer, MessageConsumer messageConsumer,
                                                                               SagaCommandProducer sagaCommandProducer, SagaLockManager sagaLockManager) {
        return new SagaManagerImpl<>(saga, sagaInstanceRepository, commandProducer, messageConsumer, sagaLockManager, sagaCommandProducer);
    }

    @Bean
    public SagaManager<CancelProjectSagaData> cancelProjectSagaDataSagaManager(CancelProjectSaga saga, SagaInstanceRepository sagaInstanceRepository, CommandProducer commandProducer, MessageConsumer messageConsumer,
                                                                               SagaCommandProducer sagaCommandProducer, SagaLockManager sagaLockManager) {
        return new SagaManagerImpl<>(saga, sagaInstanceRepository, commandProducer, messageConsumer, sagaLockManager, sagaCommandProducer);
    }

    @Bean
    public StartProjectSaga startProjectSaga(ProjectProxyService proxyService, TeamProxyService teamProxyService, WeClassProxyService weClassProxyService) {
        return new StartProjectSaga(proxyService, teamProxyService, weClassProxyService);
    }

    @Bean
    public CreateProjectSaga createProjectSaga(ProjectProxyService proxyService, TeamProxyService teamProxyService) {
        return new CreateProjectSaga(proxyService, teamProxyService);
    }

    @Bean
    public ReviseProjectSaga reviseProjectSaga() {
        return new ReviseProjectSaga();
    }

    @Bean
    public CancelProjectSaga cancelProjectSaga() {
        return new CancelProjectSaga();
    }
}
