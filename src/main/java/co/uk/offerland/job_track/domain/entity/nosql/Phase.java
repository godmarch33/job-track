package co.uk.offerland.job_track.domain.entity.nosql;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Data
public class Phase {

    private UUID jobPhaseId = UUID.randomUUID();
    private String name;
    private Instant lastUpdatedDate;
    private Instant interviewScheduleTime;

    private PhaseStatusInfoEntity statusInfoEntity;
    private int orderIndex;

    public LocalDateTime getInterviewDate() {
        if (interviewScheduleTime == null) {
            return null;
        }
        return LocalDateTime.ofInstant(interviewScheduleTime, ZoneOffset.UTC);
    }
}
