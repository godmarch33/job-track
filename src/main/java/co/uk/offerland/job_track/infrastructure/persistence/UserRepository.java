package co.uk.offerland.job_track.infrastructure.persistence;

import co.uk.offerland.job_track.domain.entity.nosql.UserEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository extends ReactiveMongoRepository<UserEntity, String> {
        Mono<UserEntity> findByUserId(UUID userId);
        Mono<UserEntity> findByUserIdAndJobsJobId(UUID userId, UUID jobId);
}
