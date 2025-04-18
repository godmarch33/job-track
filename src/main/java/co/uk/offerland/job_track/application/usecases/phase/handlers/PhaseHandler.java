package co.uk.offerland.job_track.application.usecases.phase.handlers;

import co.uk.offerland.job_track.domain.entity.PhaseName;
import co.uk.offerland.job_track.domain.entity.PhaseStatus;
import co.uk.offerland.job_track.domain.entity.PhaseSubStatus;
import co.uk.offerland.job_track.domain.entity.nosql.Phase;
import co.uk.offerland.job_track.domain.entity.nosql.User;
import co.uk.offerland.job_track.infrastructure.controller.PhaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.UUID;


public interface PhaseHandler {

    String MARK_AS_APPLIED = "Mark as applied";
    String MSG_TOOLTIP_WAIT_FINAL_RESPONSE = "Waiting final status";
    String MSG_TOOLTIP_WAIT_RESPONSE = "Waiting for HR’s reply. Once you hear back, you can update the stage, add a contact, or schedule the next interview.";
    String MSG_TOOLTIP_TIME_FOR_PREPARE = "Time to prepare for interview";
    String MARK_AS_APPLIED_TOOLTIP = "Press 'Mark as Applied' to confirm that you have applied for this vacancy";
    String PROCEED_TO_NEXT_INTERVIEW_STAGE = "Proceed to Next Stage";

    Logger log = LoggerFactory.getLogger(PhaseController.class);

    boolean isApplicable(Phase phase);

    void handle(User user, Phase currentPhase, Phase nextPhase);

    static void handlePhaseWaitResponse(Phase currentPhase, Phase nextPhase) {
        currentPhase.getStatusInfoEntity().setStatus(PhaseStatus.COMPLETED);
        currentPhase.getStatusInfoEntity().setSubStatus(PhaseSubStatus.DONE);
        currentPhase.setLastUpdatedDate(Instant.now());

        nextPhase.getStatusInfoEntity().setStatus(PhaseStatus.IN_PROGRESS);
        nextPhase.setLastUpdatedDate(Instant.now());

        if (nextPhase.getName().equals(PhaseName.OFFER_STATUS.getLabel())) {
            nextPhase.getStatusInfoEntity().setNextStageButtonName("none");
            nextPhase.getStatusInfoEntity().setSubStatus(PhaseSubStatus.WAIT_OFFER_DESICION);
            nextPhase.getStatusInfoEntity().setMsgTooltip(MSG_TOOLTIP_WAIT_FINAL_RESPONSE);
        } else {
            nextPhase.getStatusInfoEntity().setNextStageButtonName(PROCEED_TO_NEXT_INTERVIEW_STAGE);
            if (nextPhase.getInterviewScheduleTime() == null) {
                nextPhase.getStatusInfoEntity().setSubStatus(PhaseSubStatus.TIME_FOR_PREPARE);
                nextPhase.getStatusInfoEntity().setMsgTooltip(MSG_TOOLTIP_TIME_FOR_PREPARE);
            } else if (nextPhase.getInterviewScheduleTime().isBefore(Instant.now())) {
                nextPhase.getStatusInfoEntity().setSubStatus(PhaseSubStatus.TIME_FOR_PREPARE);
                nextPhase.getStatusInfoEntity().setMsgTooltip(MSG_TOOLTIP_TIME_FOR_PREPARE);
            } else {
                nextPhase.getStatusInfoEntity().setSubStatus(PhaseSubStatus.PENDING_HR_REPLY);
            }
            logChangePhase(currentPhase.getName(), nextPhase.getName(), currentPhase.getStatusInfoEntity().getSubStatus().getLabel(), nextPhase.getStatusInfoEntity().getSubStatus().getLabel(), currentPhase.getJobPhaseId());
        }
    }

    static void logChangePhase(String phaseFrom, String phaseTo, String subStatusFrom, String subStatusTo, UUID phaseId) {
        log.info("Change phase from:[{}], to:[{}] subStatusFrom :[{}], subStatusTo :[{}], phaseId: [{}]", phaseFrom, phaseTo, subStatusFrom, subStatusTo, phaseId);
    }
}
