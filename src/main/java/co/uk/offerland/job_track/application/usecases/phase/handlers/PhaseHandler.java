package co.uk.offerland.job_track.application.usecases.phase.handlers;

import co.uk.offerland.job_track.domain.entity.PhaseStatus;
import co.uk.offerland.job_track.domain.entity.PhaseSubStatus;
import co.uk.offerland.job_track.domain.entity.nosql.Phase;
import co.uk.offerland.job_track.domain.entity.nosql.User;
import co.uk.offerland.job_track.infrastructure.controller.PhaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;


public interface PhaseHandler {

    Logger log = LoggerFactory.getLogger(PhaseController.class);

    boolean isApplicable(Phase phase);

    void handle(User user, Phase currentPhase, Phase nextPhase);

    static void moveToWaitResponse(Phase currentPhase) {
        currentPhase.setSubStatus(PhaseSubStatus.WAIT_RESPONSE);
        currentPhase.setLastUpdatedDate(Instant.now());
    }

    static void handlePhaseWaitResponse(Phase currentPhase, Phase nextPhase) {
        currentPhase.setStatus(PhaseStatus.COMPLETED);
        currentPhase.setSubStatus(PhaseSubStatus.DONE);
        currentPhase.setLastUpdatedDate(Instant.now());
        nextPhase.setStatus(PhaseStatus.IN_PROGRESS);
        nextPhase.setLastUpdatedDate(Instant.now());
        if (nextPhase.getInterviewScheduleTime() == null) {
            nextPhase.setSubStatus(PhaseSubStatus.ACTION_REQUIRED);
            nextPhase.setInterviewScheduleTime(Instant.now());
        }
        if (nextPhase.getInterviewScheduleTime().isBefore(Instant.now())) {
            nextPhase.setSubStatus(PhaseSubStatus.TIME_FOR_PREPARE);
            LocalDateTime localDateTime = LocalDateTime.of(2026, 1, 1, 0, 0, 0); // January 1, 2026, 00:00:00
            ZoneId zoneId = ZoneId.of("UTC");
            ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
            nextPhase.setInterviewScheduleTime(zonedDateTime.toInstant());
        } else {
            nextPhase.setSubStatus(PhaseSubStatus.WAIT_RESPONSE);
        }
    }

    static void logChangePhase(String phaseFrom, String phaseTo, String subStatusFrom, String subStatusTo, UUID phaseId) {
        log.info("Change phase from:[{}], to:[{}] subStatusFrom :[{}], subStatusTo :[{}], phaseId: [{}]", phaseFrom, phaseTo, subStatusFrom, subStatusTo, phaseId);
    }
}
