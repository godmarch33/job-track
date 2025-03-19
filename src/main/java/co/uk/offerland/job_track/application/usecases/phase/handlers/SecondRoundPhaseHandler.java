package co.uk.offerland.job_track.application.usecases.phase.handlers;

import co.uk.offerland.job_track.domain.entity.PhaseSubStatus;
import co.uk.offerland.job_track.domain.entity.nosql.Phase;
import co.uk.offerland.job_track.domain.entity.nosql.User;
import org.springframework.stereotype.Service;

import static co.uk.offerland.job_track.application.usecases.phase.handlers.PhaseHandler.*;
import static co.uk.offerland.job_track.application.usecases.phase.handlers.PhaseHandler.handlePhaseWaitResponse;
import static co.uk.offerland.job_track.application.usecases.phase.handlers.PhaseHandler.logChangePhase;
import static co.uk.offerland.job_track.domain.entity.JobPhase.SECOND_ROUND;

@Service
public class SecondRoundPhaseHandler implements PhaseHandler {
    @Override
    public boolean isApplicable(Phase phase) {
        return SECOND_ROUND.getLabel().equals(phase.getPhaseName());
    }

    @Override
    public void handle(User user, Phase currentPhase, Phase nextPhase) {
        if (PhaseSubStatus.ACTION_REQUIRED == currentPhase.getStatusInfoEntity().getSubStatus()) {
            moveToWaitResponse(currentPhase);
            logChangePhase(currentPhase.getPhaseName(), currentPhase.getPhaseName(), PhaseSubStatus.ACTION_REQUIRED.getLabel(), PhaseSubStatus.WAIT_RESPONSE.getLabel(), currentPhase.getJobPhaseId());
        } else {
            handlePhaseWaitResponse(currentPhase, nextPhase);
            logChangePhase(currentPhase.getPhaseName(), nextPhase.getPhaseName(), PhaseSubStatus.WAIT_RESPONSE.getLabel(), nextPhase.getStatusInfoEntity().getSubStatus().getLabel(), currentPhase.getJobPhaseId());
        }
    }


}
