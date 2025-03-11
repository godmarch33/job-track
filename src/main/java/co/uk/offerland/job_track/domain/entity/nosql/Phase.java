package co.uk.offerland.job_track.domain.entity.nosql;

import co.uk.offerland.job_track.domain.entity.PhaseStatus;
import co.uk.offerland.job_track.domain.entity.PhaseSubStatus;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class Phase {

    private UUID jobPhaseId = UUID.randomUUID();
    private String phaseName;
    private Instant lastUpdatedDate;
    private Instant interviewScheduleTime;
    private PhaseStatus status;
    private int orderIndex;
    private PhaseSubStatus subStatus;
}
