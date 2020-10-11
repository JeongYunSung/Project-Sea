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
public class ProjectMember {

    private String username;
    private boolean isLeader;
    @Enumerated(EnumType.STRING)
    private ProjectMemberState projectMemberState;

    public ProjectMember(String username, boolean isLeader) {
        this.username = username;
        this.isLeader = isLeader;
        this.projectMemberState = ProjectMemberState.JOIN;
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
