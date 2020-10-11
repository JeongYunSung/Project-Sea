package com.yunseong.project.service;

import com.yunseong.project.api.controller.CreateProjectRequest;
import com.yunseong.project.api.event.ProjectEvent;
import com.yunseong.project.domain.Project;
import com.yunseong.project.domain.ProjectDomainEventPublisher;
import com.yunseong.project.domain.ProjectInfo;
import com.yunseong.project.domain.ProjectRepository;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectDomainEventPublisher projectDomainEventPublisher;

    public ResultWithDomainEvents<Project, ProjectEvent> createProject(CreateProjectRequest request) {
        ProjectInfo projectInfo = new ProjectInfo(request.getSubject(), request.getContent(), request.getMaxPeople());
        if(request.getMinPeople() == null || request.getMinPeople() == 0) {
            projectInfo.setMinPeople(projectInfo.getMaxPeople());
        }
        ResultWithDomainEvents<Project, ProjectEvent> rwe = Project.create(request.getUsername(), projectInfo);
        this.projectRepository.save(rwe.result);
        this.projectDomainEventPublisher.publish(rwe.result, rwe.events);
        return rwe;
    }
}
