package com.yunseong.project.service;

import com.yunseong.project.api.controller.CreateProjectRequest;
import com.yunseong.project.api.event.ProjectDetails;
import com.yunseong.project.domain.Project;
import com.yunseong.project.domain.ProjectRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public ResultWithEvents<Project> createProject(CreateProjectRequest request) {
        ProjectDetails projectDetails = new ProjectDetails(request.getSubject(), request.getContent(), request.getMaxPeople());
        if(request.getMinPeople() == null || request.getMinPeople() == 0) {
            projectDetails.setMinPeople(projectDetails.getMaxPeople());
        }
        ResultWithEvents<Project> rwe = Project.create(request.getUsername(), projectDetails);
        this.projectRepository.save(rwe.result);
        this.domainEventPublisher.publish(Project.class, rwe.result.getId(), rwe.events);
        return rwe;
    }
}
