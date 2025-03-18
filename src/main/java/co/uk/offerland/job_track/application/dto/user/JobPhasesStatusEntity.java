package co.uk.offerland.job_track.application.dto.user;

import co.uk.offerland.job_track.domain.entity.nosql.DailyProgressEntity;
import lombok.Builder;
import lombok.Data;
import lombok.With;

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