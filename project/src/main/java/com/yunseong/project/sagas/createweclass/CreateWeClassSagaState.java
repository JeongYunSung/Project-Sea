package com.yunseong.project.sagas.createweclass;

import com.yunseong.project.sagaparticipants.ApproveProjectCommand;
import com.yunseong.project.sagaparticipants.RejectProjectCommand;
import lombok.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateWeClassSagaState {

    @NonNull
    private Long projectId;
    private Long weClassId;

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public ApproveProjectCommand makeApproveProjectCommand() {
        return new ApproveProjectCommand(this.projectId);
    }

    public RejectProjectCommand makeRejectProjectCommand() {
        return new RejectProjectCommand(this.getProjectId());
    }
}
