package co.uk.offerland.job_track.application.dto.job;

import co.uk.offerland.job_track.application.dto.user.DailyProgress;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobPhasesStatus {

    private int savedCount;
    private int appliedCount;
    private int interviewCount;
    private int rejectedCount;
    private int declinedCount;
    private int acceptedCount;
    private DailyProgress dailyProgress;
}
