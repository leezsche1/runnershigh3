package runnershigh.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import runnershigh.project.domain.running.RunningRecord;
import runnershigh.project.repository.RunningRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RunningService {

    private final RunningRepository runningRepository;

    @Transactional
    public void save(RunningRecord runningRecord) {
        runningRepository.save(runningRecord);
    }

    public Optional<RunningRecord> findById(Long id) {

        return runningRepository.findById(id);

    }


}
