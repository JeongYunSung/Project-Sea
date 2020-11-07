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
import java.util.stream.Collectors;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private int minSize;

    private int maxSize;

    private long projectId;

    @Enumerated(EnumType.STRING)
    private TeamState teamState;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    @OneToMany(cascade = CascadeType.ALL)
    private final List<TeamMember> teamMembers = new ArrayList<>();

    public Team(long projectId, String username, int minSize, int maxSize) {
        this.projectId = projectId;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.teamState = TeamState.RECRUIT_PENDING;
        this.teamMembers.add(new TeamMember(new TeamMemberDetail(username, TeamPermission.LEADER)));
    }

    public List<TeamEvent> join(String username) {
        if (teamState == TeamState.RECRUIT_PENDING) {
            if (this.teamMembers.stream().filter(tm -> tm.getTeamMemberDetail().getUsername().equals(username)).count() >= 1) {
                throw new TeamMemberReduplicationException(username + "유저는 이미 팀내에 존재합니다");
            }
            this.teamMembers.add(new TeamMember(new TeamMemberDetail(username, TeamPermission.USER)));
            if (this.getSize() == this.maxSize) {
                this.teamState = TeamState.VOTE_PENDING;
                return Collections.singletonList(new TeamAuthorizeVoteRequestedEvent(this.projectId, this.teamMembers.stream().map(TeamMember::getTeamMemberDetail).collect(Collectors.toList())));
            }
            return Collections.singletonList(new TeamJoinedEvent(this.projectId, this.teamMembers.get(0).getTeamMemberDetail()
                    , this.teamMembers.subList(1, this.teamMembers.size()).stream().map(TeamMember::getTeamMemberDetail).collect(Collectors.toList())));
        }
        throw new UnsupportedStateTransitionException(this.teamState);
    }

    public void cancel() {
        if (this.teamState == TeamState.RECRUIT_PENDING) {
            this.teamState = TeamState.CANCELLED;
            return;
        }
        throw new UnsupportedStateTransitionException(this.teamState);
    }

    public boolean voted() {
        int count = 0;
        for (TeamMember teamMember : this.teamMembers) {
            if(teamMember.getTeamMemberDetail().getTeamMemberState() == TeamMemberState.APPROVED) count++;
        }
        return this.minSize <= count && this.maxSize >= count;
    }

    public void approveTeam() throws TeamRejectException {
        if (this.teamState == TeamState.VOTED) {
            if (!voted()) {
                this.teamState = TeamState.REJECTED;
                throw new TeamRejectException("해당 프로젝트의 과반수가 거절하여 해당 프로젝트는 취소됩니다.");
            }
            this.teamState = TeamState.APPROVED;
            for (TeamMember teamMember : this.teamMembers) {
                if (teamMember.getTeamMemberDetail().getTeamMemberState() == TeamMemberState.APPROVED)
                    teamMember.getTeamMemberDetail().setTeamMemberState(TeamMemberState.JOINED);
            }
            return;
        }
        throw new UnsupportedStateTransitionException(this.teamState);
    }

    public void rejectTeam() {
        if (this.teamState == TeamState.VOTED) {
            this.teamState = TeamState.REJECTED;
//                for (TeamMember teamMember : this.teamMembers) {
//                    if(teamMember.getTeamMemberDetail().getTeamMemberState() == TeamMemberState.JOINED) teamMember.getTeamMemberDetail().setTeamMemberState(TeamMemberState.REJECTED);
//                }
            return;
        }
        throw new UnsupportedStateTransitionException(this.teamState);
    }

    private boolean changeMemberState(String username, TeamMemberState teamMemberState) {
        for (TeamMember teamMember : this.teamMembers) {
            TeamMemberDetail teamMemberDetail = teamMember.getTeamMemberDetail();
            if(teamMemberDetail.getUsername().equals(username)) {
                if(teamMemberDetail.getTeamMemberState() != TeamMemberState.JOIN_PENDING)
                    return false;
                teamMemberDetail.setTeamMemberState(teamMemberState);
                return true;
            }
        }
        return false;
    }

    public List<TeamEvent> memberQuit(String username) {
        if (this.teamState == TeamState.RECRUIT_PENDING) {
            for (int i = 0; i < this.teamMembers.size(); i++) {
                TeamMemberDetail teamMemberDetail = teamMembers.get(i).getTeamMemberDetail();
                if (teamMemberDetail.getUsername().equals(username)) {
                    if (teamMemberDetail.getTeamMemberState() != TeamMemberState.JOIN_PENDING)
                        return Collections.emptyList();
                    this.getTeamMembers().remove(teamMembers.get(i));
                }
            }
            return Collections.singletonList(new TeamQuitEvent(this.projectId, this.teamMembers.get(0).getTeamMemberDetail()
                    , this.teamMembers.subList(1, this.teamMembers.size()).stream().map(TeamMember::getTeamMemberDetail).collect(Collectors.toList())));
        }
        throw new UnsupportedStateTransitionException(this.teamState);
    }

    public List<TeamEvent> memberApprove(String username) {
        if (this.teamState == TeamState.VOTE_PENDING) {
            if (!this.changeMemberState(username, TeamMemberState.APPROVED))
                throw new TeamMemberVotedException("이미 투표를 진행하였습니다.");
            return isAllVoted();
        }
        throw new UnsupportedStateTransitionException(this.teamState);
    }

    public List<TeamEvent> memberReject(String username) {
        if (this.teamState == TeamState.VOTE_PENDING) {
            if (!this.changeMemberState(username, TeamMemberState.REJECTED))
                throw new TeamMemberVotedException("이미 투표를 진행하였습니다.");
            return isAllVoted();
        }
        throw new UnsupportedStateTransitionException(this.teamState);
    }

    public boolean isUser(String username) {
        for (TeamMember teamMember : this.teamMembers) {
            if(teamMember.getTeamMemberDetail().getUsername().equals(username)) return true;
        }
        return false;
    }

    public boolean isLeader(String username) {
        for (TeamMember teamMember : this.teamMembers) {
            TeamMemberDetail teamMemberDetail = teamMember.getTeamMemberDetail();
            if(teamMemberDetail.getUsername().equals(username) && teamMemberDetail.getTeamPermission().equals(TeamPermission.LEADER)) return true;
        }
        return false;
    }

//    public void setProject(long projectId) {
//        switch (this.teamState) {
//            case NONE:
//                this.teamState = TeamState.RECRUIT_PENDING;
//                this.projectId = projectId;
//                return;
//            default:
//                throw new UnsupportedStateTransitionException(this.teamState);
//        }
//    }

    private List<TeamEvent> isAllVoted() {
        for(TeamMember teamMember : this.teamMembers) {
            if(teamMember.getTeamMemberDetail().getTeamMemberState() == TeamMemberState.JOIN_PENDING) return Collections.emptyList();
        }
        this.teamState = TeamState.VOTED;
        return Collections.singletonList(new TeamVotedEvent(this.projectId, this.id));
    }

    private int getSize() {
        int size = 0;
        for(TeamMember teamMember : this.teamMembers) {
            if(teamMember.getTeamMemberDetail().getTeamMemberState() == TeamMemberState.JOIN_PENDING) size++;
        }
        return size;
    }
}
