package co.uk.offerland.job_track.application.dto.job;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobPhasesStatusEntity {
    private int savedCount;
    private int appliedCount;
    private int interviewCount;
    private int rejectedCount;
    private int declinedCount;
    private int acceptedCount;
}
