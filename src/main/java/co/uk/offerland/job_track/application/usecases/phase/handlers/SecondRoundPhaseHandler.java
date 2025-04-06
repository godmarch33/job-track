package co.uk.offerland.job_track.application.usecases.phase.handlers;

import co.uk.offerland.job_track.domain.entity.PhaseSubStatus;
import co.uk.offerland.job_track.domain.entity.nosql.Phase;
import co.uk.offerland.job_track.domain.entity.nosql.User;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static co.uk.offerland.job_track.application.usecases.phase.handlers.PhaseHandler.handlePhaseWaitResponse;
import static co.uk.offerland.job_track.application.usecases.phase.handlers.PhaseHandler.logChangePhase;
import static co.uk.offerland.job_track.domain.entity.PhaseName.SECOND_ROUND;

@Service
public class SecondRoundPhaseHandler implements PhaseHandler {
    @Override
    public boolean isApplicable(Phase phase) {
        return SECOND_ROUND.getLabel().equals(phase.getPhaseName());
    }

    @Override
    public void handle(User user, Phase currentPhase, Phase nextPhase) {
        if (PhaseSubStatus.TIME_FOR_PREPARE == currentPhase.getStatusInfoEntity().getSubStatus()) {
            currentPhase.getStatusInfoEntity().setSubStatus(PhaseSubStatus.PENDING_HR_REPLY);
            currentPhase.getStatusInfoEntity().setMsgTooltip(MSG_TOOLTIP_TIME_FOR_PREPARE);
            currentPhase.getStatusInfoEntity().setNextStageButtonName(PROCEED_TO_NEXT_INTERVIEW_STAGE);
            currentPhase.setLastUpdatedDate(Instant.now());

            logChangePhase(currentPhase.getPhaseName(),
                    currentPhase.getPhaseName(),
                    PhaseSubStatus.TIME_FOR_PREPARE.getLabel(),
                    PhaseSubStatus.PENDING_HR_REPLY.getLabel(),
                    currentPhase.getJobPhaseId());
        } else if (PhaseSubStatus.PENDING_HR_REPLY == currentPhase.getStatusInfoEntity().getSubStatus() ) {
            handlePhaseWaitResponse(currentPhase, nextPhase);
            user.getInterviewStat().increase(nextPhase.getPhaseName());
        }
    }


}
