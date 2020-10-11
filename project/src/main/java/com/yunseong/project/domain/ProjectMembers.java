package com.yunseong.project.domain;

import com.yunseong.project.api.event.ProjectMember;
import lombok.*;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.List;

@Getter
@Setter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProjectMembers {

    @ElementCollection
    @CollectionTable(name = "project_members")
    private List<ProjectMember> projectMembers;
}
