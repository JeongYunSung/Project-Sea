package com.yunseong.project.service;

import com.yunseong.project.api.controller.CreateProjectRequest;
import com.yunseong.project.api.event.ProjectEvent;
import com.yunseong.project.domain.Project;
import com.yunseong.project.domain.ProjectDomainEventPublisher;
import com.yunseong.project.domain.ProjectRepository;
import com.yunseong.project.sagas.createweclass.CreateWeClassSagaState;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.function.Function;

@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectDomainEventPublisher projectDomainEventPublisher;

    @Autowired
    private SagaManager<CreateWeClassSagaState> createWeClassSagaSagaManager;

    public ResultWithDomainEvents<Project, ProjectEvent> createProject(CreateProjectRequest request) {
        ResultWithDomainEvents<Project, ProjectEvent> rwe = Project.create(request.getTeamId(), request.getWeClassId(), request.getSubject(), request.getContent());
        this.projectRepository.save(rwe.result);
        this.projectDomainEventPublisher.publish(rwe.result, rwe.events);
        return rwe;
    }

    public boolean createWeClass(long projectId) {
        CreateWeClassSagaState data = new CreateWeClassSagaState(projectId);
        this.createWeClassSagaSagaManager.create(data, Project.class, projectId);

        if(data.getWeClassId() != 0) {
            this.findProject(projectId).changeWeClassId(data.getWeClassId());
            return true;
        }
        return false;
    }

    private Project updateProject(long projectId, Function<Project, List<ProjectEvent>> func) {
        Project project = findProject(projectId);
        this.projectDomainEventPublisher.publish(project, func.apply(project));
        return project;
    }

    public void rejectProject(long projectId) {
        updateProject(projectId, Project::rejected);
    }

    public void cancelProject(long projectId) {

    }

    private Project findProject(long projectId) {
        return this.projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트는 존재하지않습니다"));
    }
}
