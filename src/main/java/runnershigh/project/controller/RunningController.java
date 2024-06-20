package runnershigh.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import runnershigh.project.domain.member.Member;
import runnershigh.project.domain.running.Route;
import runnershigh.project.domain.running.RunningRecord;
import runnershigh.project.domain.running.RunningRoute;
import runnershigh.project.domain.running.SegmentTime;
import runnershigh.project.dto.running.RunningRecordDTO;
import runnershigh.project.dto.running.RunningResultDTO;
import runnershigh.project.dto.running.RunningRouteDTO;
import runnershigh.project.dto.running.SegmentTimeDTO;
import runnershigh.project.handler.CommonResDTO;
import runnershigh.project.service.MemberService;
import runnershigh.project.service.RunningService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RunningController {

    private final RunningService runningService;
    private final MemberService memberService;
    @PostMapping("/api/v1/running")
    public ResponseEntity runningCon(@RequestBody RunningRecordDTO runningRecordDTO) {


        List<SegmentTime> segmentTimeList = runningRecordDTO.getSegmentTimeDTO().stream().map(m -> new SegmentTime(m.getKm(), m.getAveragePace(), m.getDifference(), m.getElevation())).collect(Collectors.toList());

        List<RunningRoute> runningRouteList = runningRecordDTO.getRunningRouteDTO().stream().map(m -> new RunningRoute(m.getKm(),
                        m.getRouteDTOList().stream().map(mm -> new Route(mm.getLatitude(), mm.getLongitude())).collect(Collectors.toList()))).
                collect(Collectors.toList());


        RunningRecord runningRecord = RunningRecord.runningDTOToEntity(runningRecordDTO);
//        runningRecord.setSegmentTimeList(segmentTimeList);
//        runningRecord.setRunningRouteList(runningRouteList);



//      멤버설정!
        String email = runningRecordDTO.getEmail();
        Optional<Member> byEmail = memberService.findByEmail(email);
        Member member = byEmail.get();
        runningRecord.setMember(member);


        for (SegmentTime segmentTime : segmentTimeList) {
            runningRecord.addSegmentTimeList(segmentTime);
        }
        for (RunningRoute runningRoute : runningRouteList) {
//            for (Route route : runningRoute.getRouteList()) {
//                runningRoute.addRouteList(route);
//            }
            runningRecord.addRunningRouteList(runningRoute);
        }


        runningService.save(runningRecord);


        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDTO<>("1", "기록 저장 성공", null));
    }

    @PostMapping("/api/v1/runningResult")
    public ResponseEntity runningResult(@RequestBody RunningResultDTO runningResultDTO) {
        Optional<RunningRecord> findingRunningRecord = runningService.findById(runningResultDTO.getId());
        RunningRecord runningRecord = findingRunningRecord.get();

        RunningRecordDTO runningRecordDTO = new RunningRecordDTO();
        runningRecordDTO.setTotalKm(runningRecord.getTotalKm());
        runningRecordDTO.setAveragePace(runningRecord.getAveragePace());
        runningRecordDTO.setBestPace(runningRecord.getBestPace());
        runningRecordDTO.setRunningTime(runningRecord.getRunningTime());
        runningRecordDTO.setKcal(runningRecord.getKcal());
        runningRecordDTO.setAverageCadence(runningRecord.getAverageCadence());
        runningRecordDTO.setElevationGain(runningRecord.getElevationGain());
        runningRecordDTO.setElevationLoss(runningRecord.getElevationLoss());
        runningRecordDTO.setAverageHeartRate(runningRecord.getAverageHeartRate());
        runningRecordDTO.setMaximumHeartRate(runningRecord.getMaximumHeartRate());
        runningRecordDTO.setEmail(runningRecord.getMember().getEmail());
        List<SegmentTimeDTO> segmentTimeDTOList = runningRecord.getSegmentTimeList().stream().map(m -> new SegmentTimeDTO(m.getKm(), m.getAveragePace(), m.getDifference(), m.getElevation()))
                .collect(Collectors.toList());

        List<RunningRouteDTO> runningRouteDTOList = runningRecord.getRunningRouteList().stream().map(m -> new RunningRouteDTO(m))
                .collect(Collectors.toList());



        runningRecordDTO.setSegmentTimeDTO(segmentTimeDTOList);
        runningRecordDTO.setRunningRouteDTO(runningRouteDTOList);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDTO<>("1", "기록 조회 성공", runningRecordDTO));
    }

}
