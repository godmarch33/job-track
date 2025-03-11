package co.uk.offerland.job_track.infrastructure.persistence;

import co.uk.offerland.job_track.domain.entity.nosql.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
        Mono<User> findByUserId(UUID userId);
}
