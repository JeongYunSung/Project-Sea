package com.yunseong.project.service;

import com.yunseong.project.api.controller.CreateProjectRequest;
import com.yunseong.project.api.event.ProjectEvent;
import com.yunseong.project.domain.Project;
import com.yunseong.project.domain.ProjectDomainEventPublisher;
import com.yunseong.project.domain.ProjectRepository;
import com.yunseong.project.sagas.startproject.StartProjectSagaState;
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
    private SagaManager<StartProjectSagaState> createWeClassSagaSagaManager;

    public ResultWithDomainEvents<Project, ProjectEvent> createProject(CreateProjectRequest request) {
        ResultWithDomainEvents<Project, ProjectEvent> rwe = Project.create(request.getTeamId(), request.getSubject(), request.getContent(), request.getProjectTheme());
        this.projectRepository.save(rwe.result);
        this.projectDomainEventPublisher.publish(rwe.result, rwe.events);
        return rwe;
    }

    public void startProject(long projectId) {
        closeProject(projectId);
        StartProjectSagaState data = new StartProjectSagaState(projectId);
        this.createWeClassSagaSagaManager.create(data, Project.class, projectId);
    }


    private Project updateProject(long projectId, Function<Project, List<ProjectEvent>> func) {
        Project project = findProject(projectId);
        this.projectDomainEventPublisher.publish(project, func.apply(project));
        return project;
    }

    private void closeProject(long projectId) {
        updateProject(projectId, Project::close);
    }

    public void approveProject(long projectId) {
        updateProject(projectId, Project::start);
    }

    public void rejectProject(long projectId) {
        updateProject(projectId, Project::reject);
    }

    public void registerWeClass(long projectId, long weClassId) {
        Project project = findProject(projectId);
        this.projectDomainEventPublisher.publish(project, project.register(weClassId));
    }

    public void cancelProject(long projectId) {
    }

    public Project findProject(long projectId) {
        Project project = this.projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트는 존재하지않습니다"));
        return project;
    }
}
