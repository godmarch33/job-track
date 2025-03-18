package co.uk.offerland.job_track.domain.entity.nosql;

import lombok.Data;

@Data
public class InterviewStat {
    private DailyProgressEntity dailyProgress;

    public InterviewStat() {
        dailyProgress = new DailyProgressEntity();
    }

    public int increaseSavedAndProgress() {
        return dailyProgress.updateCounter();
    }
}
