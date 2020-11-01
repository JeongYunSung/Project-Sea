package com.yunseong.project.service;

import com.yunseong.common.UnsupportedStateTransitionException;
import com.yunseong.project.api.controller.CreateProjectRequest;
import com.yunseong.project.api.event.ProjectEvent;
import com.yunseong.project.api.event.ProjectState;
import com.yunseong.project.controller.ProjectSearchCondition;
import com.yunseong.project.domain.Project;
import com.yunseong.project.domain.ProjectDomainEventPublisher;
import com.yunseong.project.domain.ProjectRepository;
import com.yunseong.project.domain.ProjectRevision;
import com.yunseong.project.sagas.cancelproject.CancelProjectSagaData;
import com.yunseong.project.sagas.createproject.CreateProjectSagaState;
import com.yunseong.project.sagas.reviseproject.ReviseProjectSagaData;
import com.yunseong.project.sagas.startproject.StartProjectSagaState;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.function.Function;

@Service
@Transactional
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectDomainEventPublisher projectDomainEventPublisher;

    private final SagaManager<CreateProjectSagaState> createProjectSagaSagaManager;

    private final SagaManager<StartProjectSagaState> createWeClassSagaSagaManager;

    private final SagaManager<CancelProjectSagaData> cancelProjectSagaDataSagaManager;

    public ResultWithDomainEvents<Project, ProjectEvent> createProject(String username, CreateProjectRequest request) {
        ResultWithDomainEvents<Project, ProjectEvent> rwe = Project.create(request.getSubject(), request.getContent(), username, request.getProjectTheme(), request.isPublic());
        Project project = this.projectRepository.save(rwe.result);
        this.projectDomainEventPublisher.publish(rwe.result, rwe.events);

        this.createProjectSagaSagaManager.create(new CreateProjectSagaState(project.getId(), username, request.getMinSize(), request.getMaxSize()), Project.class, project.getId());

        return rwe;
    }

    public Project cancel(long projectId, String username) throws EntityNotFoundException {
        Project project = this.getProject(projectId);
        CancelProjectSagaData data = new CancelProjectSagaData(projectId, project.getTeamId(), username);
        this.cancelProjectSagaDataSagaManager.create(data);
        return project;
    }

    public Project revise(long projectId, ProjectRevision projectRevision, String username) throws EntityNotFoundException {
        Project project = this.getProject(projectId);
        project.revised(username, projectRevision);
//        ReviseProjectSagaData data = new ReviseProjectSagaData(projectId, project.getTeamId(), projectRevision, username);
//        this.reviseProjectSagaDataSagaManager.create(data);
        return project;
    }

    @Transactional(readOnly = true)
    public Page<Project> findBySearch(ProjectSearchCondition projectSearchCondition, Pageable pageable) {
        return this.projectRepository.findBySearch(projectSearchCondition, pageable);
    }

    public void startProject(long projectId, long teamId) {
        StartProjectSagaState data = new StartProjectSagaState(projectId, teamId);
        this.createWeClassSagaSagaManager.create(data, Project.class, projectId);
    }


    private void updateProject(long projectId, Function<Project, List<ProjectEvent>> func) {
        Project project = getProject(projectId);
        this.projectDomainEventPublisher.publish(project, func.apply(project));
    }

    public boolean closeProject(long projectId) {
        try {
            updateProject(projectId, Project::close);
            return true;
        } catch(UnsupportedStateTransitionException e) {
            return false;
        }
    }

    public void approveProject(long projectId) {
        updateProject(projectId, Project::start);
    }

    public void rejectProject(long projectId) {
        updateProject(projectId, Project::reject);
    }

    public boolean cancelProject(long projectId) {
        try {
            updateProject(projectId, Project::cancel);
            return true;
        }catch (UnsupportedStateTransitionException e) {
            return false;
        }
    }

    public boolean reviseProject(long projectId) {
        try {
            updateProject(projectId, Project::revise);
            return true;
        }catch (UnsupportedStateTransitionException e) {
            return false;
        }
    }

    public void undoCancelOrUndoReviseOrPostedProject(long projectId) {
        updateProject(projectId, Project::undoCancelOrPostedOrRevision);
    }

    public boolean revisedProject(long projectId, ProjectRevision projectRevision) {
        try {
            Project project = getProject(projectId);
            this.projectDomainEventPublisher.publish(project, project.revised("", projectRevision));
            return true;
        }catch (UnsupportedStateTransitionException e) {
            return false;
        }
    }

    public boolean cancelledProject(long projectId) {
        try {
            updateProject(projectId, Project::cancelled);
            return true;
        }catch (UnsupportedStateTransitionException e) {
            return false;
        }
    }

    public void registerTeam(long projectId, long teamId) {
        Project project = getProject(projectId);
        project.registerTeam(teamId);
    }

    public void registerWeClass(long projectId, long weClassId) {
        Project project = getProject(projectId);
        project.registerWeClass(weClassId);
    }

    @Transactional(readOnly = true)
    public Project findProject(long projectId, String username) {
        Project project = this.getProject(projectId);
        if(project.getProjectState() != ProjectState.POSTED && !project.isPublic() && !project.getWriter().equals(username)) {
            throw new CannotReadBecausePrivateProjectException("해당 프로젝트는 비공개 이므로 열람할 수 없습니다");
        }
        return project;
    }

    public void addMember(long projectId, String username) {
        this.getProject(projectId).addMember(username);
    }

    public void removeMember(long projectId, String username) {
        this.getProject(projectId).removeMember(username);
    }

    private Project getProject(long projectId) {
        return this.projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트는 존재하지않습니다"));
    }
}
