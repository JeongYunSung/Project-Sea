package com.yunseong.project.domain;

import com.yunseong.project.api.event.ProjectMember;
import com.yunseong.project.api.event.ProjectMemberState;
import lombok.*;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.UniqueConstraint;
import java.util.List;

@Getter
@Setter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProjectMembers {

    @ElementCollection
    @CollectionTable(name = "project_members",
        uniqueConstraints = @UniqueConstraint(columnNames = "username"))
    private List<ProjectMember> projectMembers;

    public boolean projectReject(String username) {
        return userModifyState(username, ProjectMemberState.REJECTED);
    }

    public boolean projectApprove(String username) {
        return userModifyState(username, ProjectMemberState.APPROVED);
    }

    public int isApprovedCount() {
        int current = 0;
        for(ProjectMember projectMember : this.projectMembers) {
            ProjectMemberState projectMemberState = projectMember.getProjectMemberState();
            if(projectMemberState == ProjectMemberState.APPROVED) current++;
            else if(projectMemberState == ProjectMemberState.REJECTED) current--;
        }
        return current;
    }

    private boolean userModifyState(String username, ProjectMemberState state) {
        for (ProjectMember projectMember : this.projectMembers) {
            if (projectMember.getUsername().equals(username)) {
                if (projectMember.getProjectMemberState() == ProjectMemberState.JOINED) {
                    projectMember.setProjectMemberState(state);
                    return true;
                }
                break;
            }
        }
        return false;
    }
}
