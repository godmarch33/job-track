package co.uk.offerland.job_track.application.usecases.phase.handlers;

import co.uk.offerland.job_track.domain.entity.PhaseName;
import co.uk.offerland.job_track.domain.entity.PhaseStatus;
import co.uk.offerland.job_track.domain.entity.PhaseSubStatus;
import co.uk.offerland.job_track.domain.entity.nosql.Phase;
import co.uk.offerland.job_track.domain.entity.nosql.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static co.uk.offerland.job_track.application.usecases.phase.handlers.PhaseHandler.*;

@Slf4j
@Service
public class SavePhaseHandler implements PhaseHandler {

    @Override
    public boolean isApplicable(Phase phase) {
        return PhaseName.SAVED.getLabel().equals(phase.getPhaseName());
    }

    @Override
    public void handle(User user, Phase currentPhase, Phase nextPhase) {
        currentPhase.getStatusInfoEntity().setStatus(PhaseStatus.COMPLETED);
        currentPhase.getStatusInfoEntity().setSubStatus(PhaseSubStatus.DONE);
        currentPhase.setLastUpdatedDate(Instant.now());
        nextPhase.getStatusInfoEntity().setStatus(PhaseStatus.IN_PROGRESS);
        nextPhase.setLastUpdatedDate(Instant.now());
        nextPhase.getStatusInfoEntity().setSubStatus(PhaseSubStatus.PENDING_HR_REPLY);
        logChangePhase(currentPhase.getPhaseName(), nextPhase.getPhaseName(), PhaseSubStatus.ACTION_REQUIRED.getLabel(), nextPhase.getStatusInfoEntity().getSubStatus().getLabel(), currentPhase.getJobPhaseId());
    }
}
