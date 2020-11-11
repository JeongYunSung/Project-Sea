package com.yunseong.project.sagas.startproject;

import lombok.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class StartProjectSagaData {

    @NonNull
    private Long projectId;
    @NonNull
    private Long teamId;
    private Long weClassId;

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
