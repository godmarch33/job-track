package co.uk.offerland.job_track.infrastructure.persistence;

import co.uk.offerland.job_track.application.usecases.telegram.UserInterviewProjection;
import co.uk.offerland.job_track.domain.entity.nosql.User;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

public interface UserRepository extends ReactiveMongoRepository<User, String>, UserRepositoryNotification {

    Mono<User> findByUserId(UUID userId);

    @Query("{ 'jobs.phases.interviewScheduleTime': { $gte: ?0, $lt: ?1 } }")
    Flux<User> findUsersWithInterviewsBetween(Instant start, Instant end);


    @Aggregation(pipeline = {
            """
                    { $unwind: { path: '$jobs', preserveNullAndEmptyArrays: true } }
                    """,
            """
                    { $unwind: { path: '$jobs.uses_phase', preserveNullAndEmptyArrays: true } }
                    """,
            """
                    { $match: {
                        'jobs.phases.interviewScheduleTime': { $exists: true, $gte: ?0, $lt: ?1 },
                        'jobs.uses_phase.notificationSend': { $ne: true }
                    } }
                    """,
            """
                    { $project: {
                        userId: 1,
                        telegramUserId: 1,
                        company: '$jobs.company',
                        jobId: '$jobs.jobId',
                        title: '$jobs.title',
                        phaseName: '$jobs.uses_phase.name',
                        phaseId: '$jobs.uses_phase.jobPhaseId',
                        interviewTime: '$jobs.uses_phase.interviewScheduleTime',
                        reminders: '$jobs.uses_phase.reminders'
                    } }
                    """,
            """
                    { $project: {
                        userId: 1,
                        telegramUserId: 1,
                        company: 1,
                        jobId: 1,
                        title: 1,
                        phaseName: 1,
                        phaseId: 1,
                        interviewTime: 1,
                        notificationReminders: {
                            $map: {
                                input: "$reminders",
                                as: "reminder",
                                in: { "$toInt": "$$reminder.timeBefore" }
                            }
                        }
                    } }
                    """
    })
    Flux<UserInterviewProjection> findUpcomingInterviewPhasesBetweenDates(Instant from, Instant to);


}
