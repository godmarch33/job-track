package co.uk.offerland.job_track.application.usecases.phase.handlers;

import co.uk.offerland.job_track.domain.entity.PhaseStatus;
import co.uk.offerland.job_track.domain.entity.PhaseSubStatus;
import co.uk.offerland.job_track.domain.entity.nosql.Phase;
import co.uk.offerland.job_track.domain.entity.nosql.User;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static co.uk.offerland.job_track.application.usecases.phase.handlers.AppliedPhaseHandler.MSG_TOOLTIP_TIME_FOR_PREPARE;
import static co.uk.offerland.job_track.application.usecases.phase.handlers.AppliedPhaseHandler.PROCEED_TO_NEXT_INTERVIEW_STAGE;
import static co.uk.offerland.job_track.application.usecases.phase.handlers.PhaseHandler.*;
import static co.uk.offerland.job_track.domain.entity.JobPhase.ASSESSMENT_TEST;
import static co.uk.offerland.job_track.domain.entity.PhaseSubStatus.TIME_FOR_PREPARE;

@Service
public class AssesmentTestPhaseHandler implements PhaseHandler {
    @Override
    public boolean isApplicable(Phase phase) {
        return ASSESSMENT_TEST.getLabel().equals(phase.getPhaseName());
    }

    @Override
    public void handle(User user, Phase currentPhase, Phase nextPhase) {
        if (TIME_FOR_PREPARE == currentPhase.getStatusInfoEntity().getSubStatus()) {
            currentPhase.getStatusInfoEntity().setSubStatus(PhaseSubStatus.WAIT_RESPONSE);
            currentPhase.getStatusInfoEntity().setMsgTooltip(MSG_TOOLTIP_TIME_FOR_PREPARE);
            currentPhase.getStatusInfoEntity().setNextStageButtonName(PROCEED_TO_NEXT_INTERVIEW_STAGE);
            currentPhase.setLastUpdatedDate(Instant.now());

            logChangePhase(currentPhase.getPhaseName(),
                    currentPhase.getPhaseName(),
                    TIME_FOR_PREPARE.getLabel(),
                    PhaseSubStatus.WAIT_RESPONSE.getLabel(),
                    currentPhase.getJobPhaseId());
        } else if (PhaseSubStatus.WAIT_RESPONSE == currentPhase.getStatusInfoEntity().getSubStatus()) {
            handlePhaseWaitResponse(currentPhase, nextPhase);
        }
    }
}
