package co.uk.offerland.job_track.infrastructure.persistence;

import co.uk.offerland.job_track.domain.entity.nosql.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
        Mono<User> findByUserId(UUID userId);

        @Query("{ 'jobs.phases.interviewScheduleTime': { $gte: ?0, $lt: ?1 } }")
        Flux<User> findUsersWithInterviewsBetween(Instant start, Instant end);
}
