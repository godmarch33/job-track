package co.uk.offerland.job_track.application.usecases.phase;

import co.uk.offerland.job_track.domain.entity.JobPhase;
import co.uk.offerland.job_track.domain.entity.PhaseStatus;
import co.uk.offerland.job_track.domain.entity.PhaseSubStatus;
import co.uk.offerland.job_track.domain.entity.nosql.Phase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class AppliedPhaseHandler implements PhaseHandler {

    @Override
    public boolean isApplicable(Phase phase) {
        return JobPhase.APPLIED.getLabel().equals(phase.getPhaseName());
    }

    @Override
    public void handle(Phase currentPhase, Phase nextPhase) {
        if (PhaseSubStatus.ACTION_REQUIRED == currentPhase.getSubStatus()) {
            updateAppliedActionRequired(currentPhase);
        } else if (PhaseSubStatus.WAIT_RESPONSE == currentPhase.getSubStatus()) {
            updateAppliedWaitAnswer(currentPhase, nextPhase);
        }
    }

    private void updateAppliedActionRequired(Phase currentPhase) {
        currentPhase.setSubStatus(PhaseSubStatus.WAIT_RESPONSE);
        currentPhase.setLastUpdatedDate(Instant.now());
        log.info("Change phase:[{}], subStatusFrom :[{}], subStatusTo :[{}], phaseId: [{}]",
                JobPhase.APPLIED,
                PhaseSubStatus.ACTION_REQUIRED,
                PhaseSubStatus.WAIT_RESPONSE,
                currentPhase.getJobPhaseId());
    }

    private void updateAppliedWaitAnswer(Phase currentPhase, Phase nextPhase) {
        currentPhase.setStatus(PhaseStatus.COMPLETED);
        currentPhase.setSubStatus(PhaseSubStatus.DONE);
        currentPhase.setLastUpdatedDate(Instant.now());
        nextPhase.setStatus(PhaseStatus.IN_PROGRESS);
        nextPhase.setLastUpdatedDate(Instant.now());
        if (nextPhase.getInterviewScheduleTime() != null && nextPhase.getInterviewScheduleTime().isBefore(Instant.now())) {
            nextPhase.setSubStatus(PhaseSubStatus.TIME_FOR_PREPARE);
        } else {
            nextPhase.setSubStatus(PhaseSubStatus.WAIT_RESPONSE);
        }
    }
}
