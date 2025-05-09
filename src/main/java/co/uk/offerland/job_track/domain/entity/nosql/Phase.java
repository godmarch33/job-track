package co.uk.offerland.job_track.domain.entity.nosql;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Data
public class Phase {

    private UUID jobPhaseId = UUID.randomUUID();
    private int orderIndex;
    private String name;
    private Instant lastUpdatedDate;
    private Instant interviewScheduleTime;
    private PhaseStatusInfoEntity statusInfoEntity;
    private List<InterviewRemind> reminders;

}
