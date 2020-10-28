package com.yunseong.project.controller;

import com.yunseong.project.api.controller.CreateProjectRequest;
import com.yunseong.project.api.controller.CreateProjectResponse;
import com.yunseong.project.api.controller.ProjectDetailResponse;
import com.yunseong.project.api.event.ProjectEvent;
import com.yunseong.project.domain.Project;
import com.yunseong.project.domain.ProjectRevision;
import com.yunseong.project.service.ProjectService;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/projects", consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<CreateProjectResponse> createProject(@RequestBody CreateProjectRequest createProjectRequest) {
        ResultWithDomainEvents<Project, ProjectEvent> project = this.projectService.createProject(createProjectRequest);
        return new ResponseEntity<>(new CreateProjectResponse(project.result.getId()), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProjectDetailResponse> findProject(@PathVariable long id) {
        Project project = this.projectService.findProject(id);
        return ResponseEntity.ok(new ProjectDetailResponse(project.getId(), project.getSubject(), project.getContent(), project.getTeamId(), project.getWeClassId(), project.getProjectTheme(), project.getProjectState()));
    }

    @GetMapping(value = "/search")
    public ResponseEntity<PagedModel> searchProject(@ModelAttribute ProjectSearchCondition projectSearchCondition, @PageableDefault Pageable pageable) {
        Page<ProjectSearchResponse> page = this.projectService.findBySearch(projectSearchCondition, pageable).map(p -> new ProjectSearchResponse(p.getId(), p.getSubject(), p.getProjectTheme(), p.getProjectState(), p.getCreatedDate()));
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
        PagedModel model = PagedModel.of(page.getContent(), pageMetadata);
        return ResponseEntity.ok(model);
    }

    @PutMapping(value = "/cancel/{id}")
    public ResponseEntity<Long> cancelProject(@PathVariable long id) {
        Project project = this.projectService.cancel(id);
        return ResponseEntity.ok(project.getId());
    }

    @PutMapping(value = "/revise/{id}")
    public ResponseEntity<Long> reviseProject(@PathVariable long id, @RequestBody ProjectRevision projectRevision) {
        Project project = this.projectService.revise(id, projectRevision);
        return ResponseEntity.ok(project.getId());
    }
}
