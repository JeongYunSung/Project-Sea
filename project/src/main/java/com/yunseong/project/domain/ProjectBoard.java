package com.yunseong.project.domain;

import com.yunseong.board.api.BoardCategory;
import com.yunseong.board.api.events.BoardAddRecommendEvent;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ProjectBoard {

    @Id
    @GeneratedValue
    @Column(name = "projectboard_id")
    private Long id;

    @NonNull
    private String writer;

    @NonNull
    private String subject;

    @NonNull
    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "projectBoard")
    private List<ProjectRecommendStatistics> recommendStatistics = new ArrayList<>();

    public void changeSubject(String subject) {
        this.subject = subject;
    }

    public void addRecommend(Date now, long value) {
        for(ProjectRecommendStatistics recommendStatistics : this.recommendStatistics) {
            System.out.println(recommendStatistics.getRecommendDate().compareTo(now));
            if(recommendStatistics.getRecommendDate().compareTo(now)==0) {
                recommendStatistics.setValue(value);
                return;
            }
        }
        this.recommendStatistics.add(new ProjectRecommendStatistics(now, value, this));
    }
}
