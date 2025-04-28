package co.uk.offerland.job_track.infrastructure.persistence;

import co.uk.offerland.job_track.application.dto.channel.telegram.InterviewRemindNotification;
import co.uk.offerland.job_track.domain.entity.nosql.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

public interface RemindersQueueRepository extends ReactiveMongoRepository<InterviewRemindNotification, String> {

    @Query("{ 'interviewTime': { $gte: ?0, $lt: ?1 } }")
    Flux<InterviewRemindNotification> findRemindersBetween(Instant start, Instant end);

    Mono<Long> countByUserIdAndJobIdAndPhaseId(UUID userId, UUID jobId, UUID phaseId);

}
