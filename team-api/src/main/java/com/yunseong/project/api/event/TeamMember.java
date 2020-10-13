package com.yunseong.project.api.event;

import lombok.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamMember {

    private String username;
    @Enumerated(EnumType.STRING)
    private TeamPermission teamPermission;
    @Enumerated(EnumType.STRING)
    private TeamMemberState teamMemberState;

    public TeamMember(String username, TeamPermission teamPermission) {
        this.username = username;
        this.teamPermission = teamPermission;
        this.teamMemberState = TeamMemberState.JOIN_PENDING;
    }

    public void setTeamMemberState(TeamMemberState teamMemberState) {
        this.teamMemberState = teamMemberState;
    }

    public void setTeamPermission(TeamPermission teamPermission) {
        this.teamPermission = teamPermission;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
