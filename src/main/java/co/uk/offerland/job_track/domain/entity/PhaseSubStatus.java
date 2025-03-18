package co.uk.offerland.job_track.domain.entity;

public enum PhaseSubStatus {

    NONE(""),
    ACTION_REQUIRED("Action required"),
    WAIT_RESPONSE("Wait Response"),
    TIME_FOR_PREPARE("Time For Prepare"),
    DONE("Done");

    private final String label;

    PhaseSubStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
