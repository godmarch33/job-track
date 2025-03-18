package co.uk.offerland.job_track.application.usecases.phase.handlers;

import co.uk.offerland.job_track.domain.entity.JobPhase;
import co.uk.offerland.job_track.domain.entity.PhaseSubStatus;
import co.uk.offerland.job_track.domain.entity.nosql.Phase;
import co.uk.offerland.job_track.domain.entity.nosql.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static co.uk.offerland.job_track.application.usecases.phase.handlers.PhaseHandler.*;

@Slf4j
@Service
public class AppliedPhaseHandler implements PhaseHandler {

    @Override
    public boolean isApplicable(Phase phase) {
        return JobPhase.APPLIED.getLabel().equals(phase.getPhaseName());
    }

    @Override
    public void handle(User user, Phase currentPhase, Phase nextPhase) {
        if (PhaseSubStatus.NONE == currentPhase.getSubStatus()) {
            moveToWaitResponse(currentPhase);
            logChangePhase(currentPhase.getPhaseName(), currentPhase.getPhaseName(), PhaseSubStatus.ACTION_REQUIRED.getLabel(), PhaseSubStatus.WAIT_RESPONSE.getLabel(), currentPhase.getJobPhaseId());
        } else if (PhaseSubStatus.WAIT_RESPONSE == currentPhase.getSubStatus()) {
            handlePhaseWaitResponse(currentPhase, nextPhase);
            logChangePhase(currentPhase.getPhaseName(), nextPhase.getPhaseName(), PhaseSubStatus.WAIT_RESPONSE.getLabel(), nextPhase.getSubStatus().getLabel(), currentPhase.getJobPhaseId());
        }
    }
}
