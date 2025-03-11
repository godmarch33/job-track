package co.uk.offerland.job_track.application.usecases.phase;

import co.uk.offerland.job_track.domain.entity.nosql.Phase;

public interface PhaseHandler {

    boolean isApplicable(Phase phase);
    void handle(Phase currentPhase, Phase nextPhase);
}
