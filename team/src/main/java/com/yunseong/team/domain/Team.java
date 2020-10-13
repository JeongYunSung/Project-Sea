package com.yunseong.team.domain;

import com.yunseong.common.UnsupportedStateTransitionException;
import com.yunseong.project.api.event.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.yunseong.team.domain.TeamState.APPROVAL_PENDING;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    private int minSize;

    private int maxSize;

    private Long projectId;

    @Enumerated(EnumType.STRING)
    private TeamState teamState;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    @ElementCollection
    @CollectionTable(name = "team_members",
            uniqueConstraints = @UniqueConstraint(columnNames = "username"))
    private List<TeamMember> teamMembers = new ArrayList<>();

    public Team(String username, int minSize, int maxSize) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.teamState = TeamState.NONE;
        this.teamMembers.add(new TeamMember(username, TeamPermission.LEADER));
    }

    public List<TeamEvent> join(String username) {
        switch (teamState) {
            case RECRUIT_PENDING:
                this.teamMembers.add(new TeamMember(username, TeamPermission.USER));
                if(this.getSize() == maxSize) {
                    this.teamState = APPROVAL_PENDING;
                    return Collections.singletonList(new TeamAuthorizeVoteRequested(this.projectId, this.teamMembers));
                }
                return Collections.singletonList(new TeamJoinedRequestEvent(this.projectId, this.teamMembers));
            default:
                throw new UnsupportedStateTransitionException(this.teamState);
        }
    }

    public List<TeamEvent> quit(String username) {
        switch(this.teamState) {
            case RECRUIT_PENDING:
                this.changeState(username, TeamMemberState.QUIT);
                return Collections.singletonList(new TeamQuitEvent(this.projectId, username));
            default:
                throw new UnsupportedStateTransitionException(this.teamState);
        }
    }

    private boolean changeState(String username, TeamMemberState teamMemberState) {
        for (TeamMember teamMember : this.teamMembers) {
            if(teamMember.getUsername().equals(username)) {
                teamMember.setTeamMemberState(teamMemberState);
                return true;
            }
        }
        return false;
    }

    private boolean voted() {
        int count = 0;
        for (TeamMember teamMember : this.teamMembers) {
            if(teamMember.getTeamMemberState() == TeamMemberState.APPROVED) count++;
            else if(teamMember.getTeamMemberState() == TeamMemberState.REJECTED) count--;
        }
        if(this.minSize <= count && this.maxSize >= count) return true;
        return false;
    }

    public List<TeamEvent> approve(String username) {
        switch(this.teamState) {
            case APPROVAL_PENDING:
                this.changeState(username, TeamMemberState.APPROVED);
                return isAllVoted();
            default:
                throw new UnsupportedStateTransitionException(this.teamState);
        }
    }

    public List<TeamEvent> reject(String username) {
        switch(this.teamState) {
            case APPROVAL_PENDING:
                this.changeState(username, TeamMemberState.REJECTED);
                return isAllVoted();
            default:
                throw new UnsupportedStateTransitionException(this.teamState);
        }
    }

    public boolean isUser(String username) {
        for (TeamMember teamMember : this.teamMembers) {
            if(teamMember.getUsername().equals(username)) return true;
        }
        return false;
    }

    public void setProject(long projectId) {
        if(this.projectId == null)
            this.teamState = TeamState.RECRUIT_PENDING;
        this.projectId = projectId;
    }

    private List<TeamEvent> isAllVoted() {
        for(TeamMember teamMember : this.teamMembers) {
            if(teamMember.getTeamMemberState() == TeamMemberState.JOIN_PENDING) return Collections.emptyList();
        }
        return Collections.singletonList(new TeamVotedEvent(this.projectId));
    }

    private int getSize() {
        int size = 0;
        for(TeamMember teamMember : this.teamMembers) {
            if(teamMember.getTeamMemberState() == TeamMemberState.JOINED) size++;
        }
        return size;
    }
}
