package runnershigh.project.dto.running;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RunningRecordDTO {

    private String email;

    private Integer totalKm;
    private Integer averagePace;
    private Integer bestPace;
    private Integer runningTime; // 칼로리
    private Integer kcal; // 평균 케이던스
    private Integer averageCadence; // 고도 상승
    private Integer elevationGain; // 고도 하강
    private Integer elevationLoss; // 평균 심박수
    private Integer averageHeartRate; // 최대 심박수
    private Integer maximumHeartRate; // 구간

    private List<SegmentTimeDTO> segmentTimeDTO;
    private List<RunningRouteDTO> runningRouteDTO;


}
