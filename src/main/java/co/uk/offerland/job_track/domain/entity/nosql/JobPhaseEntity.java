package co.uk.offerland.job_track.domain.entity.nosql;

import co.uk.offerland.job_track.domain.entity.JobPhaseStatus;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class JobPhaseEntity {

    private UUID jobPhaseId = UUID.randomUUID();
    private String phaseName;
    private Instant lastUpdatedDate;
    private Instant interviewScheduleTime;
    private JobPhaseStatus status;
    private int orderIndex;
    private String activeStatus;
}
