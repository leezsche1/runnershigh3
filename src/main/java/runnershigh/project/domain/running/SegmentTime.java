package runnershigh.project.domain.running;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SegmentTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer km;
    private Integer averagePace;
    private Integer difference;
    private Integer elevation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "runningRecord_id")
    private RunningRecord runningRecord;

    public SegmentTime(Integer km, Integer averagePace, Integer difference, Integer elevation) {
        this.km = km;
        this.averagePace = averagePace;
        this.difference = difference;
        this.elevation = elevation;
    }

    //    public static SegmentTime segmentTimeDTOToEntity(SegmentTimeDTO segmentTimeDTO) {
//
//        SegmentTime segmentTime = new SegmentTime();
//        segmentTime.setKm(segmentTimeDTO.getKm());
//        segmentTime.setAveragePace(segmentTimeDTO.getAveragePace());
//        segmentTime.setDifference(segmentTimeDTO.getDifference());
//        segmentTime.setElevation(segmentTimeDTO.getElevation());
//
//        return segmentTime;
//    }
}
