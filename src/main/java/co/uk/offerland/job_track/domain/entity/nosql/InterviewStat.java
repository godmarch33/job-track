package co.uk.offerland.job_track.domain.entity.nosql;

import lombok.Data;

@Data
public class InterviewStat {
    private int savedCount;
    private int appliedCount;
    private int interviewCount;
    private int rejectedCount;
    private int acceptedCount;
    private DailyProgressEntity dailyProgress;

    public InterviewStat() {
        savedCount = 0;
        appliedCount = 0;
        interviewCount = 0;
        rejectedCount = 0;
        acceptedCount = 0;
        dailyProgress = new DailyProgressEntity();
    }

    public int increaseSavedAndProgress() {
        dailyProgress.updateCounter();
        return ++this.savedCount;
    }

    public void increaseApplied() {
        this.appliedCount++;
    }

    public void increaseInterview() {
        this.interviewCount++;
    }

    public void increaseReject() {
        this.rejectedCount++;
    }

    public void increaseAccepted() {
        this.acceptedCount++;
    }
}
