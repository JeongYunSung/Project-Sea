package com.yunseong.project.config;

import com.yunseong.project.domain.ProjectDomainEventPublisher;
import com.yunseong.project.sagaparticipants.ProjectProxyService;
import com.yunseong.project.sagas.createweclass.CreateWeClassSaga;
import com.yunseong.project.sagas.createweclass.CreateWeClassSagaState;
import io.eventuate.common.id.IdGenerator;
import io.eventuate.common.jdbc.EventuateJdbcStatementExecutor;
import io.eventuate.common.jdbc.EventuateSchema;
import io.eventuate.tram.commands.producer.CommandProducer;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.sagas.common.SagaLockManager;
import io.eventuate.tram.sagas.orchestration.*;
import io.eventuate.tram.sagas.spring.common.EventuateTramSagaCommonConfiguration;
import io.eventuate.tram.spring.commands.producer.TramCommandProducerConfiguration;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TramEventsPublisherConfiguration.class, ProjectServiceMessageHandlersConfiguration.class, TramCommandProducerConfiguration.class, EventuateTramSagaCommonConfiguration.class})
public class ProjectServiceConfiguration {

    @Bean
    public SagaInstanceRepository sagaInstanceRepository(EventuateJdbcStatementExecutor eventuateJdbcStatementExecutor,
                                                         IdGenerator idGenerator,
                                                         EventuateSchema eventuateSchema) {
        return new SagaInstanceRepositoryJdbc(eventuateJdbcStatementExecutor, idGenerator, eventuateSchema);
    }


    @Bean
    public SagaCommandProducer sagaCommandProducer(CommandProducer commandProducer) {
        return new SagaCommandProducer(commandProducer);
    }

    @Bean
    public ProjectDomainEventPublisher projectDomainEventPublisher(DomainEventPublisher domainEventPublisher) {
        return new ProjectDomainEventPublisher(domainEventPublisher);
    }

    @Bean
    public SagaManager<CreateWeClassSagaState> createWeClassSagaSagaManager(CreateWeClassSaga saga, SagaInstanceRepository sagaInstanceRepository, CommandProducer commandProducer, MessageConsumer messageConsumer,
                                                                            SagaCommandProducer sagaCommandProducer, SagaLockManager sagaLockManager) {
        return new SagaManagerImpl<>(saga, sagaInstanceRepository, commandProducer, messageConsumer, sagaLockManager, sagaCommandProducer);
    }

    @Bean
    public CreateWeClassSaga createWeClassSaga(ProjectProxyService proxyService) {
        return new CreateWeClassSaga(proxyService);
    }
}
