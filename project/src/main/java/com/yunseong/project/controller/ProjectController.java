package com.yunseong.project.controller;

import com.yunseong.project.api.event.ProjectEvent;
import com.yunseong.project.domain.Project;
import com.yunseong.project.domain.ProjectRecommendStatistics;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/projects", consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping(value = "/{id}")
    @PreAuthorize("(isAnonymous() and @projectService.getCategory(#id).readPermission.name() == 'ANONYMOUS') or (#oauth2.hasScope('board_read') and hasRole('ROLE_' + @projectService.getCategory(#id).readPermission.name()))")
    public ResponseEntity<ProjectDetailResponse> findProject(@PathVariable long id, Principal principal) {
        Project project = this.projectService.findProject(id, principal.getName());
        return ResponseEntity.ok(new ProjectDetailResponse(project.getId(), project.getBoardId(), project.getTeamId(), project.getWeClassId(), project.getProjectState(), project.getCreatedDate()));
    }

    @GetMapping(value = "/search")
    public ResponseEntity<PagedModel<ProjectSearchResponse>> searchProject(@ModelAttribute ProjectSearchCondition projectSearchCondition, @PageableDefault Pageable pageable) {
        Page<ProjectSearchResponse> page = this.projectService.findBySearch(projectSearchCondition, pageable).map(p -> new ProjectSearchResponse(p.getId(), p.getBoard().getSubject(),
                p.getBoard().getBoardCategory(), p.getProjectState(), p.getBoard().getRecommendStatistics().stream().mapToLong(ProjectRecommendStatistics::getValue).sum(), p.isPublic(), p.getCreatedDate()));
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
        PagedModel<ProjectSearchResponse> model = PagedModel.of(page.getContent(), pageMetadata);
        return ResponseEntity.ok(model);
    }

    @GetMapping(value = "/best")
    public ResponseEntity<List<HotProjectSearchResponse>> searchHotProject(@ModelAttribute HotProjectSearchCondition projectSearchCondition) {
        return ResponseEntity.ok(this.projectService.findHotProjects(projectSearchCondition).stream().map(p -> new HotProjectSearchResponse(p.getId(), p.getBoard().getSubject(),
                p.getBoard().getBoardCategory(), p.getProjectState(), p.getBoard().getRecommendStatistics().stream().mapToLong(ProjectRecommendStatistics::getValue).sum())).collect(Collectors.toList()));
    }

    @GetMapping(value = "/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PagedModel<ProjectSearchResponse>> searchProject(Principal principal, @PageableDefault Pageable pageable) {
        Page<ProjectSearchResponse> page = this.projectService.findMyProjects(principal.getName(), pageable).map(p -> new ProjectSearchResponse(p.getId(), p.getBoard().getSubject(),
                p.getBoard().getBoardCategory(), p.getProjectState(), p.getBoard().getRecommendStatistics().stream().mapToLong(ProjectRecommendStatistics::getValue).sum(), p.isPublic(), p.getCreatedDate()));
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
        PagedModel<ProjectSearchResponse> model = PagedModel.of(page.getContent(), pageMetadata);
        return ResponseEntity.ok(model);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("#oauth2.hasScope('board_write') and hasRole('ROLE_' + #createProjectRequest.category.writePermission.name())")
    public ResponseEntity<Long> createProject(@ModelAttribute CreateProjectRequest createProjectRequest, @RequestPart(required = false, name = "file") MultipartFile[] files, Principal principal) {
        ResultWithDomainEvents<Project, ProjectEvent> project = this.projectService.createProject(principal.getName(), createProjectRequest, files);
        return new ResponseEntity<>(project.result.getId(), HttpStatus.CREATED);
    }

    @PutMapping(value = "/cancel/{id}")
    @PreAuthorize("hasRole('ROLE_' + @projectService.getCategory(#id).writePermission.name())")
    public ResponseEntity<Long> cancelProject(@PathVariable long id, Principal principal) {
        Project project = this.projectService.cancel(id, principal.getName());
        return ResponseEntity.ok(project.getId());
    }

    @PutMapping(value = "/revise/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_' + @projectService.getCategory(#id).writePermission.name())")
    public ResponseEntity<Long> reviseProject(@PathVariable long id, @ModelAttribute ProjectRevision projectRevision, @RequestPart(required = false, name = "file") MultipartFile[] files, Principal principal) {
        Project project = this.projectService.revise(id, projectRevision, principal.getName(), files);
        return ResponseEntity.ok(project.getId());
    }
}
