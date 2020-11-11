package com.yunseong.project.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class ProjectRecommendStatistics {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private Date recommendDate;

    @NonNull
    private long value;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectboard_name")
    private ProjectBoard projectBoard;

    public void setValue(long value) {
        this.value = value;
    }
}
