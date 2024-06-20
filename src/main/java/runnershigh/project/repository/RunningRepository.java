package runnershigh.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import runnershigh.project.domain.running.RunningRecord;

public interface RunningRepository extends JpaRepository<RunningRecord, Long> {

}
