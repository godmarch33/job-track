package co.uk.offerland.job_track.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyProgress {
    private int maxDailyProgress;
    private int currentProgress;
}
