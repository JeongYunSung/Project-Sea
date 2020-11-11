package com.yunseong.project.service;

import com.yunseong.board.api.BoardCategory;
import com.yunseong.board.api.BoardDetail;
import com.yunseong.common.CannotReviseBoardIfWriterNotWereException;
import com.yunseong.common.UnsupportedStateTransitionException;
import com.yunseong.project.controller.CreateProjectRequest;
import com.yunseong.project.api.event.ProjectEvent;
import com.yunseong.project.api.event.ProjectState;
import com.yunseong.project.controller.HotProjectSearchCondition;
import com.yunseong.project.controller.ProjectSearchCondition;
import com.yunseong.project.domain.*;
import com.yunseong.project.sagas.batchproject.BatchProjectSagaData;
import com.yunseong.project.sagas.cancelproject.CancelProjectSagaData;
import com.yunseong.project.sagas.createproject.CreateProjectSagaState;
import com.yunseong.project.sagas.reviseproject.ReviseProjectSagaData;
import com.yunseong.project.sagas.startproject.StartProjectSagaData;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectDomainEventPublisher projectDomainEventPublisher;
    private final SagaManager<CreateProjectSagaState> createProjectSagaSagaManager;
    private final SagaManager<StartProjectSagaData> startProjectSagaStateSagaManager;
    private final SagaManager<CancelProjectSagaData> cancelProjectSagaDataSagaManager;
    private final SagaManager<ReviseProjectSagaData> reviseProjectSagaDataSagaManager;
    private final SagaManager<BatchProjectSagaData> batchProjectSagaDataSagaManager;

    public ResultWithDomainEvents<Project, ProjectEvent> createProject(String username, CreateProjectRequest request, MultipartFile[] files) {
        ResultWithDomainEvents<Project, ProjectEvent> rwe = Project.create(request.isOpen(), request.getLastDate());
        Project project = this.projectRepository.save(rwe.result);
        this.projectDomainEventPublisher.publish(rwe.result, rwe.events);

        this.createProjectSagaSagaManager.create(new CreateProjectSagaState(project.getId(), username, request.getMinSize(), request.getMaxSize(),
                new BoardDetail(username, request.getSubject(), request.getContent(), request.getCategory(), files)), Project.class, project.getId());

        return rwe;
    }

    public Project cancel(long projectId, String username) {
        Project project = this.getProject(projectId);
        if(project.isWriter(username)) throw new CannotReviseBoardIfWriterNotWereException("작성자가 아니면 수정할 수 없습니다");
        if(project.getProjectState() != ProjectState.POSTED) throw new NotReviseForUnsupportedException("프로젝트가 취소할 수 없는 상태입니다");
        CancelProjectSagaData data = new CancelProjectSagaData(projectId, project.getTeamId(), username);
        this.cancelProjectSagaDataSagaManager.create(data);
        return project;
    }

    public Project revise(long projectId, ProjectRevision projectRevision, String username, MultipartFile[] files) {
        Project project = this.getProject(projectId);
        if(project.isWriter(username)) throw new CannotReviseBoardIfWriterNotWereException("작성자가 아니면 수정할 수 없습니다");
        if(project.getProjectState() != ProjectState.POSTED) throw new NotReviseForUnsupportedException("프로젝트가 수정할 수 없는 상태입니다");
        ReviseProjectSagaData data = new ReviseProjectSagaData(projectId, project.getBoardId(), username, projectRevision, files);
        this.reviseProjectSagaDataSagaManager.create(data);
        return project;
    }

    @Transactional(readOnly = true)
    public Page<Project> findBySearch(ProjectSearchCondition projectSearchCondition, Pageable pageable) {
        return this.projectRepository.findBySearch(projectSearchCondition, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Project> findMyProjects(String username, Pageable pageable) {
        return this.projectRepository.findMyProjects(username, pageable);
    }

    @Transactional(readOnly = true)
    public List<Project> findHotProjects(HotProjectSearchCondition projectSearchCondition) {
        return this.projectRepository.findHotProjects(projectSearchCondition);
    }

    public void startProject(long projectId, long teamId) {
        StartProjectSagaData data = new StartProjectSagaData(projectId, teamId);
        this.startProjectSagaStateSagaManager.create(data);
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
        } catch (Exception e) {
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
            this.projectDomainEventPublisher.publish(project, project.revised(projectRevision));
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

    public void registerTeam(long projectId, long teamId, String username) {
        Project project = getProject(projectId);
        project.registerTeam(teamId, username);
    }

    public void registerBoard(long projectId, long boardId, BoardDetail boardDetail) {
        Project project = getProject(projectId);
        project.registerBoard(boardId, boardDetail);
    }

    public void registerWeClass(long projectId, long weClassId) {
        Project project = getProject(projectId);
        project.registerWeClass(weClassId);
    }

    @Transactional(readOnly = true)
    public Project findProject(long projectId, String username) {
        Project project = this.getProject(projectId);
        if(project.getProjectState() != ProjectState.POSTED && !project.isPublic() && project.getMembers().contains(username)) {
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

    public void startBatch(Date now) {
        List<Project> targetBatch = this.projectRepository.findTargetBatch(now);
        if(targetBatch.size() == 0) {
            return;
        }
        BatchProjectSagaData data = new BatchProjectSagaData(targetBatch.stream().map(Project::getId).collect(Collectors.toList()), targetBatch.stream().map(Project::getBoardId).collect(Collectors.toList()));
        this.batchProjectSagaDataSagaManager.create(data);
    }

    public boolean batchPending(List<Long> ids) {
        try {
            this.projectRepository.batchPendingUpdate(ids);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void batchUndo(List<Long> ids) {
        this.projectRepository.batchUndoUpdate(ids);
    }

    public boolean batched(List<Long> ids) {
        try {
            this.projectRepository.batchUpdate(ids);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void addRecommend(long boardId, Date now, long value) {
        Project project = this.projectRepository.findByBoardId(boardId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트는 존재하지않습니다"));
        project.getBoard().addRecommend(now, value);
    }

    @Transactional(readOnly = true)
    public BoardCategory getCategory(long id) {
        return this.getProject(id).getBoard().getBoardCategory();
    }

    private Project getProject(long projectId) {
        return this.projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트는 존재하지않습니다"));
    }
}
