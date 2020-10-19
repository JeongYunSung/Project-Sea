package com.yunseong.project.controller;

import com.yunseong.project.api.controller.CreateProjectRequest;
import com.yunseong.project.api.controller.CreateProjectResponse;
import com.yunseong.project.api.controller.ProjectResponse;
import com.yunseong.project.api.event.ProjectEvent;
import com.yunseong.project.domain.Project;
import com.yunseong.project.service.ProjectService;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/projects", consumes = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<CreateProjectResponse> createProject(@RequestBody CreateProjectRequest createProjectRequest) {
        ResultWithDomainEvents<Project, ProjectEvent> project = this.projectService.createProject(createProjectRequest);
        return new ResponseEntity<>(new CreateProjectResponse(project.result.getId()), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProjectResponse> findProject(@PathVariable Long id) {
        Project project = this.projectService.findProject(id);
        return ResponseEntity.ok(new ProjectResponse(project.getId(), project.getSubject(), project.getContent(), project.getTeamId(), project.getWeClassId(), project.getProjectTheme(), project.getProjectState()));
    }
}
