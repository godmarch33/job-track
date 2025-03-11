package co.uk.offerland.job_track.application.usecases.phase;

import co.uk.offerland.job_track.domain.entity.nosql.Phase;
import org.springframework.stereotype.Service;

@Service
public class ThirdRoundPhaseHandler implements PhaseHandler {
    @Override
    public boolean isApplicable(Phase phase) {
        return false;
    }

    @Override
    public void handle(Phase currentPhase, Phase nextPhase) {

    }

}
