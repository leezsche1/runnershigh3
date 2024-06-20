package runnershigh.project.dto.running;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SegmentTimeDTO {

    private Integer km;
    private Integer averagePace;
    private Integer difference;
    private Integer elevation;

}
