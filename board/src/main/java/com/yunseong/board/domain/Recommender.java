package com.yunseong.board.domain;

import com.yunseong.board.api.events.BoardAddRecommendEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor
public class Recommender {

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "recommenders", joinColumns = @JoinColumn(name = "board_id"))
    private final Set<String> recommender = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recommender", cascade = CascadeType.ALL)
    private final List<RecommendStatistics> recommendStatistics = new ArrayList<>();

    public BoardAddRecommendEvent addRecommender(String username) throws ParseException {
        this.recommender.add(username);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date now = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        for(RecommendStatistics recommendStatistics : this.recommendStatistics) {
            if(recommendStatistics.getRecommendDate().compareTo(now)==0) {
                recommendStatistics.addValue();
                return new BoardAddRecommendEvent(this.id, now, recommendStatistics.getValue());
            }
        }
        RecommendStatistics recommendStatistics = new RecommendStatistics(now, this);
        this.recommendStatistics.add(recommendStatistics);
        return new BoardAddRecommendEvent(this.id, now, recommendStatistics.getValue());
    }
}
