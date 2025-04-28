package co.uk.offerland.job_track.infrastructure.persistence;

import co.uk.offerland.job_track.domain.entity.nosql.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryNotificationImpl implements UserRepositoryNotification {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Void> updateNotificationSend(UUID userId, UUID jobId, UUID phaseId) {
        var query = new Query(
                Criteria.where("userId").is(userId)
                        .and("jobs.jobId").is(jobId)
                        .and("jobs.uses_phase.jobPhaseId").is(phaseId)
        );

        var update = new Update()
                .set("jobs.$[job].uses_phase.$[phase].notificationSend", true)
                .filterArray(Criteria.where("job.jobId").is(jobId))
                .filterArray(Criteria.where("phase.jobPhaseId").is(phaseId));

        return mongoTemplate.updateFirst(query, update, User.class).then();
    }
}
