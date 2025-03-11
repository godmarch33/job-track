package co.uk.offerland.job_track.application.usecases.phase.handlers;

import co.uk.offerland.job_track.domain.entity.PhaseSubStatus;
import co.uk.offerland.job_track.domain.entity.nosql.Phase;
import co.uk.offerland.job_track.domain.entity.nosql.User;
import org.springframework.stereotype.Service;

import static co.uk.offerland.job_track.application.usecases.phase.handlers.PhaseHandler.*;
import static co.uk.offerland.job_track.domain.entity.JobPhase.*;

@Service
public class FinalRoundPhaseHandler implements PhaseHandler {
    @Override
    public boolean isApplicable(Phase phase) {
        return FINAL_ROUND.getLabel().equals(phase.getPhaseName());
    }

    @Override
    public void handle(User user, Phase currentPhase, Phase nextPhase) {
        log.info("HANDLE FINAL ROUND:{}", currentPhase.getSubStatus());
        log.info("HANDLE FINAL ROUND IS:{}", PhaseSubStatus.ACTION_REQUIRED == currentPhase.getSubStatus());
        if (PhaseSubStatus.ACTION_REQUIRED == currentPhase.getSubStatus()) {
            handlePhaseActionRequired(currentPhase);
            logChangePhase(currentPhase.getPhaseName(), currentPhase.getPhaseName(), PhaseSubStatus.ACTION_REQUIRED.getLabel(), PhaseSubStatus.WAIT_RESPONSE.getLabel(), currentPhase.getJobPhaseId());
        } else if (PhaseSubStatus.ACTION_REQUIRED != currentPhase.getSubStatus()) {
            handlePhaseWaitResponse(currentPhase, nextPhase);
            user.getInterviewStat().increaseInterview();
            logChangePhase(currentPhase.getPhaseName(), nextPhase.getPhaseName(), PhaseSubStatus.WAIT_RESPONSE.getLabel(), nextPhase.getSubStatus().getLabel(), currentPhase.getJobPhaseId());
        }
    }
}
