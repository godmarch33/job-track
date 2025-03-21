package co.uk.offerland.job_track.application.usecases.phase.handlers;

import co.uk.offerland.job_track.domain.entity.JobPhase;
import co.uk.offerland.job_track.domain.entity.PhaseSubStatus;
import co.uk.offerland.job_track.domain.entity.nosql.Phase;
import co.uk.offerland.job_track.domain.entity.nosql.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static co.uk.offerland.job_track.application.usecases.phase.handlers.PhaseHandler.handlePhaseWaitResponse;
import static co.uk.offerland.job_track.application.usecases.phase.handlers.PhaseHandler.logChangePhase;

@Slf4j
@Service
public class AppliedPhaseHandler implements PhaseHandler {

    @Override
    public boolean isApplicable(Phase phase) {
        return JobPhase.APPLIED.getLabel().equals(phase.getPhaseName());
    }

    @Override
    public void handle(User user, Phase currentPhase, Phase nextPhase) {
        if (PhaseSubStatus.ACTION_REQUIRED == currentPhase.getStatusInfoEntity().getSubStatus()) {
            currentPhase.getStatusInfoEntity().setSubStatus(PhaseSubStatus.WAIT_RESPONSE);
            currentPhase.getStatusInfoEntity().setMsgTooltip(MSG_TOOLTIP_WAIT_RESPONSE);
            currentPhase.getStatusInfoEntity().setNextStageButtonName(PROCEED_TO_NEXT_INTERVIEW_STAGE);
            currentPhase.setLastUpdatedDate(Instant.now());
            logChangePhase(currentPhase.getPhaseName(), currentPhase.getPhaseName(), PhaseSubStatus.ACTION_REQUIRED.getLabel(), PhaseSubStatus.WAIT_RESPONSE.getLabel(), currentPhase.getJobPhaseId());
        } else if (PhaseSubStatus.WAIT_RESPONSE == currentPhase.getStatusInfoEntity().getSubStatus()) {
            handlePhaseWaitResponse(currentPhase, nextPhase);
        }
    }
}
