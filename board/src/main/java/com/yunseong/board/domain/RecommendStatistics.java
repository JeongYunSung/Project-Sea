package com.yunseong.board.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RecommendStatistics {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private Date recommendDate;

    private long value = 1;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommender_name")
    private Recommender recommender;

    public void addValue() {
        this.value += 1;
    }
}
