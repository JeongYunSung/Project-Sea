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
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping(value = "/projects", consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("#oauth2.hasScope('board_write') and hasRole('ROLE_' + #createProjectRequest.category.writePermission.name())")
    public ResponseEntity<CreateProjectResponse> createProject(@ModelAttribute CreateProjectRequest createProjectRequest, @RequestPart(required = false, name = "file") MultipartFile[] files, Principal principal) {
        ResultWithDomainEvents<Project, ProjectEvent> project = this.projectService.createProject(principal.getName(), createProjectRequest, files);
        return new ResponseEntity<>(new CreateProjectResponse(project.result.getId()), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("(isAnonymous() and @projectService.getCategory(#id).readPermission.name() == 'ANONYMOUS') or (#oauth2.hasScope('board_read') and hasRole('ROLE_' + @projectService.getCategory(#id).readPermission.name()))")
    public ResponseEntity<ProjectDetailResponse> findProject(@PathVariable long id, Principal principal) {
        Project project = this.projectService.findProject(id, principal.getName());
        return ResponseEntity.ok(new ProjectDetailResponse(project.getId(), project.getBoardId(), project.getTeamId(), project.getWeClassId(), project.getProjectState()));
    }

    @GetMapping(value = "/search")
    public ResponseEntity<PagedModel<ProjectSearchResponse>> searchProject(@ModelAttribute ProjectSearchCondition projectSearchCondition, @PageableDefault Pageable pageable) {
        Page<ProjectSearchResponse> page = this.projectService.findBySearch(projectSearchCondition, pageable).map(p -> new ProjectSearchResponse(p.getId(), p.getBoard().getSubject(), p.getBoard().getBoardCategory(), p.getProjectState(), p.getCreatedDate()));
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
        PagedModel<ProjectSearchResponse> model = PagedModel.of(page.getContent(), pageMetadata);
        return ResponseEntity.ok(model);
    }

    @GetMapping(value = "/me")
    public ResponseEntity<PagedModel<ProjectSearchResponse>> searchProject(Principal principal, @PageableDefault Pageable pageable) {
        Page<ProjectSearchResponse> page = this.projectService.findBySearch(new ProjectSearchCondition(null, null, null, principal.getName()), pageable).map(p -> new ProjectSearchResponse(p.getId(), p.getBoard().getSubject(), p.getBoard().getBoardCategory(), p.getProjectState(), p.getCreatedDate()));
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
        PagedModel<ProjectSearchResponse> model = PagedModel.of(page.getContent(), pageMetadata);
        return ResponseEntity.ok(model);
    }

    @PutMapping(value = "/cancel/{id}")
    @PreAuthorize("hasRole('ROLE_' + @projectService.getCategory(#id).writePermission.name())")
    public ResponseEntity<Long> cancelProject(@PathVariable long id, Principal principal) {
        Project project = this.projectService.cancel(id, principal.getName());
        return ResponseEntity.ok(project.getId());
    }

    @PutMapping(value = "/revise/{id}")
    @PreAuthorize("hasRole('ROLE_' + @projectService.getCategory(#id).writePermission.name())")
    public ResponseEntity<Long> reviseProject(@PathVariable long id, @RequestBody ProjectRevision projectRevision, Principal principal) {
        Project project = this.projectService.revise(id, projectRevision, principal.getName());
        return ResponseEntity.ok(project.getId());
    }
}
