package co.uk.offerland.job_track.infrastructure.persistence;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepositoryNotification {

    Mono<Void> updateNotificationSend(UUID userId, UUID jobId, UUID phaseId);
}
