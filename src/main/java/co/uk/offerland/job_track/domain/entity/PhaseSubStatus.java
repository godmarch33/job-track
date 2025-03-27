package co.uk.offerland.job_track.domain.entity;

public enum PhaseSubStatus {

    NONE(""),
    ACTION_REQUIRED("Action required"),
    PENDING_HR_REPLY("Pending Hr Reply"),
    WAIT_OFFER_DESICION("Wait Offer Decision"),
    TIME_FOR_PREPARE("Time For Prepare"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected"),
    DECLINED("Declined"),
    DONE("Done");

    private final String label;

    PhaseSubStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
