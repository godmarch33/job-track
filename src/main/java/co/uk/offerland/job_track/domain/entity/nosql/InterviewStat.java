package co.uk.offerland.job_track.domain.entity.nosql;

import co.uk.offerland.job_track.domain.entity.PhaseName;
import co.uk.offerland.job_track.domain.entity.PhaseStatus;
import lombok.Data;

import static co.uk.offerland.job_track.domain.entity.PhaseName.SCREENING_CALL;

@Data

public class InterviewStat {
    private DailyProgressEntity dailyProgress;
    private int savedCount;
    private int appliedCount;
    private int screeningСallCount;
    private int assessmentTestCount;
    private int firstRoundCount;
    private int secondRoundCount;
    private int thirdRoundCount;
    private int finalRoundCount;
    private int negotiationCount;
    private int rejectedCount;
    private int declinedCount;
    private int acceptedCount;


    public InterviewStat() {
        dailyProgress = new DailyProgressEntity();
    }

    public void increaseOfferStatus(PhaseStatus status) {
        switch (status) {
            case ACCEPTED -> increaseAccepted();
            case DECLINED -> increaseDeclined();
            case REJECTED -> increaseRejected();
            default -> throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    public void increaseInterviewCounts(String phaseName) {
        switch (PhaseName.fromCode(phaseName)) {
            case SCREENING_CALL -> increaseScreeningСall();
            case ASSESSMENT_TEST -> increaseAssessmentTest();
            case FIRST_ROUND -> increaseFirstRound();
            case SECOND_ROUND -> increaseSecondRound();
            case THIRD_ROUND -> increaseThirdRound();
            case FINAL_ROUND -> increaseFinalRound();
            case NEGOTIATION -> increaseNegotiating();
            case OFFER_STATUS -> dumm();
            default -> throw new IllegalArgumentException("Invalid phase name: " + phaseName);
        }
    }

    private void dumm() {
    }

    public int increaseDayProgress() {
        return dailyProgress.updateCounter();
    }

    public int increaseSaved() {
        return ++savedCount;
    }

    public int increaseApplied() {
        return ++appliedCount;
    }

    public int increaseScreeningСall() {
        return ++screeningСallCount;
    }

    public int increaseAssessmentTest() {
        return ++assessmentTestCount;
    }

    public int increaseFirstRound() {
        return ++firstRoundCount;
    }

    public int increaseSecondRound() {
        return ++secondRoundCount;
    }

    public int increaseThirdRound() {
        return ++thirdRoundCount;
    }

    public int increaseFinalRound() {
        return ++finalRoundCount;
    }

    public int increaseNegotiating() {
        return ++negotiationCount;
    }

    public int increaseRejected() {
        return ++rejectedCount;
    }

    public int increaseDeclined() {
        return ++declinedCount;
    }

    public int increaseAccepted() {
        return ++acceptedCount;
    }

    public void increase(String phaseName) {
        increaseInterviewCounts(phaseName);
    }
}
